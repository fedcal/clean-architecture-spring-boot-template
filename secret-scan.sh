#!/usr/bin/env bash
# secret-scan.sh - pre-push secret scanner for this template.
#
# Portable, no-new-dependency check. Run it BEFORE the first `git push` to the
# public remote. `gitleaks detect --source . --no-git` (or an equivalent
# dedicated scanner) is a stronger optional upgrade.
#
# Unlike a naive keyword grep, this scanner is tuned for a real code repository:
# it flags actual secret VALUES and production markers, and it deliberately does
# NOT flag framework identifiers (SecretKey, PasswordEncoder, ...), Spring config
# KEYS that reference environment variables (`${APP_JWT_SECRET:...}`), code
# comments, `*.example` files, or build artifacts under `target/`.
#
# Usage:   ./secret-scan.sh [directory]     # defaults to the current directory
# Exit:    0 = clean, 1 = at least one potential real secret / marker found.

set -euo pipefail

TARGET_DIR="${1:-.}"

COMMON_EXCLUDES=(--exclude-dir=.git --exclude-dir=target --exclude='*.example' --exclude='secret-scan.sh')

# Patterns that indicate a REAL leaked secret or a production marker:
#   - PEM private key headers (RSA / EC / OPENSSH / DSA)
#   - JDBC connection strings with embedded credentials (user:pass@host)
#   - AWS access key ids
#   - Bearer tokens with a real high-entropy value
#   - production markers specific to the source monorepo this was extracted from
REAL_SECRET_PATTERNS='-----BEGIN (RSA |EC |OPENSSH |DSA )?PRIVATE KEY-----|jdbc:[a-z]+://[^ "'"'"'`]*:[^ "'"'"'`@]*@|AKIA[0-9A-Z]{16}|Bearer [A-Za-z0-9._-]{24,}|178\.104|portfolio-prod|/opt/portfolio'

# NOTE: `-e` is required because the pattern begins with dashes (PEM header),
# which grep would otherwise parse as command-line options.
HITS=$(grep -rInIE "${COMMON_EXCLUDES[@]}" -e "$REAL_SECRET_PATTERNS" "$TARGET_DIR" 2>/dev/null || true)

# Secret/password/token/apikey assigned to a QUOTED LITERAL value (not a
# ${ENV...} reference and not an obvious placeholder). This catches
# `secret = "abc123realvalue"` while ignoring `${APP_JWT_SECRET:...}`,
# `change-me-local-only`, `REPLACE_WITH_...`, etc.
ASSIGN_HITS=$(grep -rInIE "${COMMON_EXCLUDES[@]}" \
  -e '(secret|password|passwd|api[_-]?key|access[_-]?token)[[:space:]]*[:=][[:space:]]*"[^"$][^"]{11,}"' \
  "$TARGET_DIR" 2>/dev/null \
  | grep -vE 'REPLACE_WITH|change-me|example|placeholder|CHANGEME|your-|xxxx' || true)

ALL_HITS=$(printf '%s\n%s\n' "$HITS" "$ASSIGN_HITS" | grep -vE '^$' || true)

if [ -n "$ALL_HITS" ]; then
  echo "secret-scan.sh: potential real secret(s) / production marker(s) found - review before pushing:" >&2
  echo "$ALL_HITS" >&2
  exit 1
fi

echo "secret-scan.sh: no real secrets or production markers found in $TARGET_DIR"
exit 0

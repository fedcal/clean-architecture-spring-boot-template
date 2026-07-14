package com.example.cleanarch.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architecture-fitness gate: enforces the 4-layer Clean Architecture dependency
 * rules for this template.
 *
 * <p><strong>Why plain JUnit {@code @Test} methods and NOT the ArchUnit
 * annotated static-field pattern:</strong> the {@code archunit-junit5} engine is
 * not discovered by the Surefire/JUnit-Platform combination that Spring Boot 3.4
 * ships. The annotated static-field discovery therefore silently yields
 * {@code Tests run: 0} - a false-green gate that protects nothing. Importing the
 * classes once via {@link ClassFileImporter} in {@code @BeforeAll} and calling
 * {@code rule.check(IMPORTED)} inside ordinary {@code @Test} methods makes the
 * gate execute deterministically with the engine Surefire already runs. Verify
 * with {@code mvn test -Dtest=CleanArchitectureTest} and confirm
 * {@code Tests run: N} with N &gt; 0.
 */
class CleanArchitectureTest {

    private static final String BASE_PACKAGE = "com.example.cleanarch";

    private static JavaClasses importedClasses;

    @BeforeAll
    static void importClasses() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    /** The domain layer must be pure: no Spring, no JPA. */
    @Test
    void domainMustNotDependOnFrameworks() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta.persistence..")
                .as("domain must stay pure (no Spring, no JPA)")
                .allowEmptyShould(true);
        rule.check(importedClasses);
    }

    /** The domain layer must not depend on any outer layer. */
    @Test
    void domainMustNotDependOnOuterLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..infrastructure..", "..presentation..")
                .as("domain must not depend on application, infrastructure or presentation")
                .allowEmptyShould(true);
        rule.check(importedClasses);
    }

    /** The application layer must not depend on infrastructure or presentation. */
    @Test
    void applicationMustNotDependOnInfrastructureOrPresentation() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infrastructure..", "..presentation..")
                .as("application must depend inward only (domain), never on infrastructure or presentation")
                .allowEmptyShould(true);
        rule.check(importedClasses);
    }

    /** Controllers (presentation) must not depend on infrastructure directly. */
    @Test
    void presentationMustNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..presentation..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..")
                .as("presentation must go through application ports, never touch infrastructure directly")
                .allowEmptyShould(true);
        rule.check(importedClasses);
    }
}

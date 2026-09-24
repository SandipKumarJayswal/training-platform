package com.example.app;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter().importPackages("com.example");

    @Test
    void catalogCoreShouldNotReachIntoOtherCores() {
        noClasses().that().resideInAPackage("..catalog.core..")
                .should().dependOnClassesThat().resideInAnyPackage("..enrollments.core..", "..notifications.core..")
                .check(classes);
    }

    @Test
    void enrollmentsCoreShouldNotReachIntoOtherCores() {
        noClasses().that().resideInAPackage("..enrollments.core..")
                .should().dependOnClassesThat().resideInAnyPackage("..catalog.core..", "..notifications.core..")
                .check(classes);
    }

    @Test
    void notificationsCoreShouldNotReachIntoOtherCores() {
        noClasses().that().resideInAPackage("..notifications.core..")
                .should().dependOnClassesThat().resideInAnyPackage("..catalog.core..", "..enrollments.core..")
                .check(classes);
    }

    @Test
    void apiModulesShouldNotDependOnAnyCoreModule() {
        noClasses().that().resideInAnyPackage("..catalog.api..", "..enrollments.api..", "..notifications.api..")
                .should().dependOnClassesThat().resideInAnyPackage("..catalog.core..", "..enrollments.core..", "..notifications.core..")
                .check(classes);
    }
}
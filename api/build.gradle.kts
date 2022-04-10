plugins {
    application

    id("org.springframework.boot")
    id("com.netflix.dgs.codegen")

    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
}

dependencies {
    // Project
    implementation(project(":common"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${Versions.Spring.BOOT}")
    kapt("org.springframework.boot:spring-boot-configuration-processor:${Versions.Spring.BOOT}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // FasterXML
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    // Liquibase
    implementation("org.liquibase:liquibase-core:${Versions.LIQUIBASE_CORE}")

    // PostgreSQL JDBC Driver
    runtimeOnly("org.postgresql:postgresql")

    // Netflix DGS
    implementation(
        platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"),
    )
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")

    // kotlin-logging
    implementation("io.github.microutils:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING_JVM}")

    // MapStruct
    implementation("org.mapstruct:mapstruct:${Versions.MAPSTRUCT}")
    kapt("org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}")

    // JWT
    implementation("com.auth0:java-jwt:${Versions.AUTH0_JWT}")

    // Spreadsheet
    implementation("org.dhatim:fastexcel-reader:${Versions.FASTEXCEL}")
    implementation("de.siegmar:fastcsv:${Versions.FASTCSV}")

    // Keycloak client
    implementation("org.keycloak:keycloak-admin-client:${Versions.KEYCLOAK_ADMIN_CLIENT}")
}

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:${Versions.Spring.CLOUD_AWS}")
    }
}

kapt {
    arguments {
        arg("mapstruct.verbose", "true")
    }
}

java.sourceSets["main"].java {
    srcDir("${project.buildDir.absolutePath}/dgs-codegen/generated")
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    packageName = "com.briolink.verificationservice.api"
    language = "kotlin"
    typeMapping = mutableMapOf(
        "Url" to "java.net.URL",
        "Upload" to "org.springframework.web.multipart.MultipartFile",
        "Year" to "java.time.Year"
    )
    generatedSourcesDir = "${project.buildDir.absolutePath}/dgs-codegen"
}

tasks.compileJava {
    dependsOn(tasks.processResources)
}

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Versions.Spring.BOOT apply false
    id("io.spring.dependency-management") version Versions.Spring.DEPENDENCY_MANAGEMENT apply false

    id("com.diffplug.spotless") version Versions.Plugin.SPOTLESS
    id("com.netflix.dgs.codegen") version Versions.Plugin.DGS_CODEGEN apply false
    id("com.github.ben-manes.versions") version Versions.Plugin.VERSIONS

    kotlin("jvm") version Versions.KOTLIN apply false
    kotlin("kapt") version Versions.KOTLIN apply false
    kotlin("plugin.spring") version Versions.KOTLIN apply false
    kotlin("plugin.jpa") version Versions.KOTLIN apply false
    kotlin("plugin.allopen") version Versions.KOTLIN apply false
}

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    apply {
        plugin("com.diffplug.spotless")
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    group = Base.GROUP
    version = Base.VERSION_NAME

    tasks.withType<JavaCompile> {
        sourceCompatibility = Versions.JVM
        targetCompatibility = Versions.JVM
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = Versions.JVM
        }
    }

    spotless {
        kotlin {
            target("**/*.kt")

            // https://github.com/diffplug/spotless/issues/142
            ktlint(Versions.Plugin.KTLINT).userData(
                mapOf(
                    "indent_style" to "space",
                    "max_line_length" to "140",
                    "indent_size" to "4",
                    "ij_kotlin_code_style_defaults" to "KOTLIN_OFFICIAL",
                    "ij_kotlin_line_comment_at_first_column" to "false",
                    "ij_kotlin_line_comment_add_space" to "true",
                    "ij_kotlin_name_count_to_use_star_import" to "2147483647",
                    "ij_kotlin_name_count_to_use_star_import_for_members" to "2147483647",
                    "ij_kotlin_keep_blank_lines_in_declarations" to "1",
                    "ij_kotlin_keep_blank_lines_in_code" to "1",
                    "ij_kotlin_keep_blank_lines_before_right_brace" to "0",
                    "ij_kotlin_align_multiline_parameters" to "false",
                    "ij_continuation_indent_size" to "4",
                    "insert_final_newline" to "true",
                )
            )

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }

        kotlinGradle {
            target("**/*.gradle.kts", "*.gradle.kts")

            ktlint().userData(mapOf("indent_size" to "4", "continuation_indent_size" to "4"))

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
    }

    tasks.withType<KotlinCompile> {
        dependsOn("spotlessApply")
        dependsOn("spotlessCheck")
    }
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        // Internal GitLab library IDs
        setOf(
            29889174, // BL Event
            33688770, // BL Sync
            36319712, // BL Common
        ).forEach {
            maven {
                url = uri("https://gitlab.com/api/v4/projects/$it/packages/maven")
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
                credentials(HttpHeaderCredentials::class) {
                    name = "Deploy-Token"
                    value = System.getenv("CI_DEPLOY_PASSWORD")
                }
            }
        }
    }

    apply {
        plugin("kotlin")
        plugin("io.spring.dependency-management")
    }

    val implementation by configurations

    dependencies {
        implementation("com.briolink.lib:event:${Versions.Briolink.EVENT}")
        implementation("com.briolink.lib:sync:${Versions.Briolink.SYNC}")
        implementation("com.briolink.lib:common:${Versions.Briolink.COMMON}")
        implementation("me.paulschwarz:spring-dotenv:${Versions.Spring.DOTENV}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

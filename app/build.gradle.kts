plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialisation)
    alias(libs.plugins.sonar)
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.12"
}

android {
    namespace = "fr.pm1g1.arboris"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.pm1g1.arboris"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            enableUnitTestCoverage = true
            buildConfigField("String", "BASE_URL", "\"https://codefirst.iut.uca.fr/kubernetes/iut-inf63-projets-etudiants-arboris/arboris-api/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL", "\"https://codefirst.iut.uca.fr/kubernetes/iut-inf63-projets-etudiants-arboris/arboris-api/\"")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.jvmArgs("-noverify")
                it.extensions.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*", "android.*", "com.android.*", "org.robolectric.*")
                }
            }
        }
    }

    lint {
        abortOnError = false
        xmlReport = true
        htmlReport = true
        checkReleaseBuilds = false
    }
}

// Configuration Sonar adaptée à votre exemple
sonar {
    properties {
        property("sonar.projectKey", "SAE-2A-Arboris-KOTLIN")
        property("sonar.projectName", "Arboris Kotlin App")
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java")
        
        // Exclusions similaires à votre exemple
        property("sonar.exclusions", "**/R.class, **/BuildConfig.*, **/Manifest*.*, android/**/*.*")
        
        // Coverage report - Utilise le fichier coverage.xml généré à la racine du module
        property("sonar.coverage.jacoco.xmlReportPaths", "coverage.xml")
        
        // Requis pour Kotlin/Java
        property("sonar.java.binaries", "build/tmp/kotlin-classes/debug")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    group = "Reporting"
    
    reports {
        xml.required.set(true)
        html.required.set(true)
        // Génère le fichier coverage.xml à la racine du module app
        xml.outputLocation.set(file("${projectDir}/coverage.xml"))
    }
    
    val fileFilter = listOf(
        "**/R.class", "**/R\$*.class", "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*", "android/**/*.*", "**/*\$Lambda\$*.*", "**/*\$Companion\$*.*"
    )
    
    val kotlinClasses = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) { exclude(fileFilter) }
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(files(kotlinClasses))
    
    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.core.ktx)
    implementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

// 1. Centralizar la Gestión de Versiones de Dependencias
val sqliteJdbcVersion by extra { "3.51.0.0" }
val jbcryptVersion by extra { "0.4" }
val controlsfxVersion by extra { "11.1.2" }
val ikonliVersion by extra { "12.3.1" }
val retrofitVersion by extra { "2.11.0" }
val okhttpVersion by extra { "4.12.0" }
val gsonVersion by extra { "2.10.1" }
val kotlinVersion by extra { "1.8.22" }
val itextVersion by extra { "7.2.5" }
val slf4jVersion by extra { "1.7.36" }
val junitVersion by extra { "5.10.0" }
val hikariCPVersion by extra { "5.1.0" }


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.example.explorandoelcosmos")
    mainClass.set("com.example.explorandoelcosmos.Launcher")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // Todas las dependencias se añaden al classpath
    implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
    implementation("org.mindrot:jbcrypt:$jbcryptVersion")
    implementation("org.controlsfx:controlsfx:$controlsfxVersion")
    implementation("org.kordamp.ikonli:ikonli-javafx:$ikonliVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")


    // PDF Generation
    implementation("com.itextpdf:kernel:$itextVersion")
    implementation("com.itextpdf:layout:$itextVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")

    // 2. Añadir HikariCP
    implementation("com.zaxxer:HikariCP:$hikariCPVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("com.mysql:mysql-connector-j:9.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}

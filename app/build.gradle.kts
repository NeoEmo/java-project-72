plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.6.1"
    id("gg.jte.gradle") version "3.2.4"
    id("jacoco")
    id("checkstyle")
    id("org.sonarqube") version "7.5.0.8588"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Source: https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.20")
    testImplementation("org.slf4j:slf4j-simple:2.0.19")

    // Source: https://mvnrepository.com/artifact/io.javalin/javalin
    implementation("io.javalin:javalin:7.2.3")

    // Source: https://mvnrepository.com/artifact/gg.jte/jte
    implementation("gg.jte:jte:3.2.4")

    // Source: https://mvnrepository.com/artifact/io.javalin/javalin-rendering-jte
    implementation("io.javalin:javalin-rendering-jte:7.2.3")

    // Source: https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    implementation("com.zaxxer:HikariCP:7.1.0")

    // Source: https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.14")

    compileOnly("org.projectlombok:lombok:1.18.48")
    annotationProcessor("org.projectlombok:lombok:1.18.48")
    testCompileOnly("org.projectlombok:lombok:1.18.48")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.48")

    implementation("com.h2database:h2:2.5.250")

    // Source: https://mvnrepository.com/artifact/io.javalin/javalin-testtools
    testImplementation("io.javalin:javalin-testtools:7.2.3")

    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:4.0.0-M1")

    // Source: https://mvnrepository.com/artifact/com.konghq/unirest-java-core
    implementation("com.konghq:unirest-java-core:4.10.1")

    // Source: https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.23.2")
}

jte {
    sourceDirectory.set(file("src/main/resources/templates").toPath())
    generate()
    binaryStaticContent = true
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.12.4"
}

tasks.withType<Checkstyle>().configureEach {
    exclude("**/gg/jte/generated/**")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) { exclude("**/gg/jte/generated/**") }
        })
    )
}

sonar {
    properties {
        property("sonar.projectKey", "NeoEmo_java-project-72")
        property("sonar.organization", "neoemo")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
    }
}
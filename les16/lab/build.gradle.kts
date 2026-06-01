plugins {
    war
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.1")
    testImplementation("org.mockito:mockito-core:5.17.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.17.0")
    testImplementation("org.springframework:spring-test:6.2.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2:2.2.224")

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")
    implementation("org.springframework.data:spring-data-jpa:3.4.4")

    implementation("org.hibernate.orm:hibernate-core:6.2.0.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.2.0.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")

    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")

    runtimeOnly("com.h2database:h2:2.2.224")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.war {
    archiveFileName.set("product-table.war")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco/html")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

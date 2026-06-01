plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")
    implementation("org.springframework.data:spring-data-jpa:3.4.4")
    implementation("org.hibernate.orm:hibernate-core:6.2.0.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.2.0.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    runtimeOnly("com.h2database:h2:2.2.224")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.App"
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    jvmArgs(
        "-Dsun.stdout.encoding=Cp866",
        "-Dsun.stderr.encoding=Cp866"
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

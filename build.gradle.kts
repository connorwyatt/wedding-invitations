import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.5.2"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.5.20"
  kotlin("plugin.spring") version "1.5.20"
}

group = "io.connorwyatt.wedding"
java.sourceCompatibility = JavaVersion.VERSION_16

repositories {
  maven { url = uri("https://repo.spring.io/release") }
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.flywaydb:flyway-core")
  implementation("org.jooq:jooq:3.13.4")
  runtimeOnly("org.postgresql:postgresql")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.axonframework:axon-spring-boot-starter:4.5.3")
  implementation("org.axonframework.extensions.kotlin:axon-kotlin:0.1.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
}

tasks {
  bootJar {
    archiveFileName.set("invitations.jar")
  }
}


tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "16"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.gradleup.shadow") version "9.2.2"
  id("io.spring.dependency-management") version "1.1.4"
  id("com.google.cloud.tools.jib") version "3.5.2"
}

group = "com.learning.udemy"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "5.0.5"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.learning.udemy.vertx_starter.MainVerticle"
val launcherClassName = "io.vertx.launcher.application.VertxApplication"

application {
  mainClass.set(launcherClassName)
}

dependencyManagement {
  imports {
    mavenBom("org.apache.logging.log4j:log4j-bom:2.12.4")
  }
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-launcher-application")

  implementation("org.apache.logging.log4j:log4j-api")
  implementation("org.apache.logging.log4j:log4j-core")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl")
  implementation("org.slf4j:slf4j-api:1.7.30")

  implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

jib {
  from {
    image = "eclipse-temurin:17-jre"
  }
  to {
    image = "example/jib/vertx-starter"
  }
  container {
    mainClass = "io.vertx.launcher.application.VertxApplication"
    args = listOf(mainVerticleName)
    ports = listOf("8888")
  }
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf(mainVerticleName)
}

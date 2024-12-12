import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "3.3.6"
}
repositories {
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
}

dependencies {
    // expectation: dependency resolution fails with a conflict
    // because spring cloud 2024 is not compatible with spring 3.3.x
    // See https://spring.io/projects/spring-cloud for compatability
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2024.0.0"))
    implementation(enforcedPlatform(SpringBootPlugin.BOM_COORDINATES))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
}
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "3.3.6"
    id("io.spring.dependency-management") version "1.1.6"
}
repositories {
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
}

extra["springCloudVersion"] = "2024.0.0"

dependencyManagement {
    imports {
        // expectation: dependency resolution fails with a conflict
        // because spring cloud 2024 is not compatible with spring 3.3.x
        // See https://spring.io/projects/spring-cloud for compatability
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${extra["springCloudVersion"]}")
    }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
}
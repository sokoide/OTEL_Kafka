plugins {
    id("org.springframework.boot") version "2.2.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    java
}

group = "com.sokoide.java_kp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.apache.kafka:kafka-clients:2.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.3")
//    implementation("io.zipkin.brave:brave:5.12.7")
//    implementation("io.zipkin.brave:brave-bom:5.12.7")
//    implementation("io.zipkin.brave:brave-instrumentation-kafka-clients:5.12.7")
//    implementation("io.zipkin.reporter2:zipkin-sender-okhttp3:2.15.2");
//    implementation("io.zipkin.reporter2:zipkin-sender-kafka:2.15.2");
//    implementation(files("libs/brave-kafka-interceptor-0.5.5-SNAPSHOT.jar"))
    testCompile("junit", "junit", "4.12")
}

plugins {
    id("java")
    id("war")
}

group = "org.vaskozov.lab4"
version = "1.0"

repositories {
    mavenCentral()
}

val gsonVersion = "2.10.1"
val jakartaServletVersion = "6.1.0"
val jakartaEjbVersion = "4.0.1"
val jakartaApiVersion = "10.0.0"
val hibernateVersion = "6.6.3.Final"
val postgresqlVersion = "42.7.4"
val lombokVersion = "1.18.34"
val guavaVersion = "33.3.1-jre"
val jjwtVersion = "0.12.6"

dependencies {
    implementation("com.google.code.gson:gson:${gsonVersion}")
    compileOnly("jakarta.servlet:jakarta.servlet-api:${jakartaServletVersion}")
    implementation("jakarta.ejb:jakarta.ejb-api:${jakartaEjbVersion}")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.hibernate:hibernate-core:${hibernateVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    implementation("com.google.guava:guava:${guavaVersion}")
    implementation("jakarta.platform:jakarta.jakartaee-api:${jakartaApiVersion}")
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
}
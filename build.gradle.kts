plugins {
    id("java")
    id("war")
}

group = "org.vaskozov.lab4"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("javax.servlet:jstl:1.2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("javax.servlet.jsp:javax.servlet.jsp-api:2.3.3")
    implementation("jakarta.ejb:jakarta.ejb-api:4.0.1")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.hibernate:hibernate-core:6.6.3.Final")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")
}
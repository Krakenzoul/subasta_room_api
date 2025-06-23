plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("io.ktor.plugin") version "2.3.2"
    
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core-jvm:2.3.2")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.2")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.2")
    implementation("ch.qos.logback:logback-classic:1.5.13")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}
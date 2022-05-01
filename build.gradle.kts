plugins {
    kotlin("jvm") version "1.6.10"
}

group = "com.github.shaun-wild"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
}

tasks.test {
    useJUnitPlatform()
}

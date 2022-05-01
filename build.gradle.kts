plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
}

val groupId = "com.github.shaun-wild"
val packageVersion = "1.0.0"

group = groupId
version = packageVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = groupId
            artifactId = "kproxy"
            version = packageVersion

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

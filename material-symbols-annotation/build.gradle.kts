plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = "annotation"
            version = project.version.toString()

            from(components["java"])
        }
    }
}
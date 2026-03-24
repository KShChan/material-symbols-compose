plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(project(":material-symbols-annotation"))

    implementation(libs.google.ksp.api)
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.squareup.okhttp)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "ksp"
            version = project.version.toString()

            from(components["java"])
        }
    }
}
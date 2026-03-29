plugins {
    java
    alias(libs.plugins.runPaper)
}

dependencies {
    compileOnly(libs.paper)
    implementation(project(":celestial-api"))
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
    }
}
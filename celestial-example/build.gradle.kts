plugins {
    java
}

dependencies {
    compileOnly(libs.paper)
    implementation(project(":celestial-api"))
}
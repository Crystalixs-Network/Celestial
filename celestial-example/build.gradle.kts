plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.resourceFactory)
}

dependencies {
    compileOnly(libs.paper)
    implementation(project(":celestial-api"))
}

tasks {
    shadowJar {
        relocate("net.crystalixs.celestial.api", "net.crystalixs.celestial.example.api")
    }

    runServer {
        minecraftVersion("1.21.11")
    }

    bukkitPluginYaml {
        name = rootProject.property("plugin-name") as String
        description = rootProject.property("description") as String
        authors = pluginAuthors()
        main = "net.crystalixs.celestial.example.ExamplePlugin"
    }
}

fun pluginAuthors(): List<String> {
    return (rootProject.property("authors") as String)
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() && it.isNotBlank() }
}
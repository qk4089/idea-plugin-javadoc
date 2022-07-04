plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "com.qk.plugin"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.freemarker:freemarker:2.3.31")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2021.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("com.intellij.java", "Git4Idea"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(File(project.property("CERTIFICATE_CHAIN").toString()).readText(Charsets.UTF_8))
        privateKey.set(File(project.property("PRIVATE_KEY").toString()).readText(Charsets.UTF_8))
        password.set(project.property("PRIVATE_KEY_PASSWORD").toString())
    }

    publishPlugin {
        token.set(project.property("PUBLISH_TOKEN").toString())
    }
}

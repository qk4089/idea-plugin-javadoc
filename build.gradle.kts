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
    version.set("2022.1.3")
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
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

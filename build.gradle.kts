plugins {
    kotlin("jvm") version "1.9.23"
    alias(libs.plugins.userdev)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.bukkit)
    alias(libs.plugins.shadow)
}

group = "de.rechergg"
version = "1.0-SNAPSHOT"
description = "A plugin that allows you to play HotPotato in Minecraft"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    library(kotlin("stdlib"))
    implementation(libs.kspigot)
    implementation(libs.commandapi.bukkit.shade)
    implementation(libs.commandapi.bukkit.kotlin)
}

kotlin {
    jvmToolchain(21)
}

bukkit {
    name = "HotPotato"
    description = getDescription()
    apiVersion = "1.16"
    author = "RECHERGG"
    main = "$group.hotpotato.HotPotato"
    version = getVersion().toString()
}

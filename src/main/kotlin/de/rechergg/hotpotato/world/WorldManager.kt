package de.rechergg.hotpotato.world

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.game.GameManager
import de.rechergg.hotpotato.game.GameState
import de.rechergg.hotpotato.util.FileUtil
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.worlds
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File

object WorldManager {

    val world: World

    init {
        world = createWorld() ?: throw IllegalStateException("World could not be loaded!")
        world.apply {
            setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            setGameRule(GameRule.FALL_DAMAGE, false)
            setGameRule(GameRule.FIRE_DAMAGE, false)
            setGameRule(GameRule.DO_MOB_LOOT, false)
            setGameRule(GameRule.DO_MOB_SPAWNING, false)
            setGameRule(GameRule.DISABLE_RAIDS, false)
            setGameRule(GameRule.DO_ENTITY_DROPS, false)
            setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false)
            setGameRule(GameRule.DO_TRADER_SPAWNING, false)
            setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            setGameRule(GameRule.KEEP_INVENTORY, true)
        }

        //TODO eigene Cords pro map setzbar (mit config)
        val startX = 70
        val startZ = 100
        val endX = -70
        val endZ = -56

        preloadChunks(world.name, startX, startZ, endX, endZ)
        GameManager.gameState = GameState.WAITING
    }

    private fun createWorld(): World? {
        val directory = File("./hotpotato_game")
        if (directory.exists()) {
            worlds.firstOrNull { it.name == "hotpotato_game" }?.let {
                it.players.forEach { player -> player.kick() }
                Bukkit.unloadWorld(it, false)
                directory.deleteRecursively()
            }
        }

        val template = File("./hotpotato_template")
        if (!template.exists()) {
            console.sendMessage(cmp("Template Directory \"hotpotato_template\" not found!", NamedTextColor.RED))
            return null
        }

        FileUtil.copyDirectory(template, directory)
        return WorldCreator("hotpotato_game").createWorld()
    }

    private fun preloadChunks(worldName: String, startX: Int, startZ: Int, endX: Int, endZ: Int) {
        val world = Bukkit.getWorld(worldName) ?: return

        for (x in startX..endX) {
            for (z in startZ..endZ) {
                val chunk = world.getChunkAt(x, z)
                if (!chunk.isLoaded) {
                    chunk.load(true)
                }
            }
        }
    }
}
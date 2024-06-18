package de.rechergg.hotpotato.listener

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.extension.eliminated
import de.rechergg.hotpotato.extension.plus
import de.rechergg.hotpotato.extension.prefix
import de.rechergg.hotpotato.game.GameManager.bossBar
import de.rechergg.hotpotato.game.GameManager.eliminate
import de.rechergg.hotpotato.game.GameManager.gameState
import de.rechergg.hotpotato.game.GameManager.team
import de.rechergg.hotpotato.game.GameState
import de.rechergg.hotpotato.world.WorldManager
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object ConnectionListeners {

    private val onConnect = listen<AsyncPlayerPreLoginEvent> {
        if (gameState == GameState.PREPARING_WORLD) {
            it.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, cmp("Pleas wait for the World to initialized!", NamedTextColor.RED))
        }
    }

    private val onJoin = listen<PlayerJoinEvent> {
        val player = it.player

        player.teleportAsync(Location(WorldManager.world, 0.5, 81.0, 0.5))

        player.apply {
            exp = 0f
            level = 0

            inventory.clear()
            clearActivePotionEffects()
            gameMode = GameMode.ADVENTURE

            feedSaturate()
            heal()
        }

        if (!team.hasPlayer(player)) {
            team.addPlayer(player)
        }

        if (!bossBar.players.contains(player)) {
            bossBar.addPlayer(player)
        }

        if (gameState != GameState.WAITING) {
            eliminate(player)
            it.joinMessage(null)
            return@listen
        }

        it.joinMessage(prefix() + cmp("+", NamedTextColor.GREEN).decorate(TextDecoration.BOLD) + cmp(" ${player.name}", if (player.hasPermission("hotpotato.start")) NamedTextColor.GOLD else NamedTextColor.GRAY))
    }

    private val onQuit = listen<PlayerQuitEvent> {
        val player = it.player
        if (player.eliminated) return@listen

        it.quitMessage(prefix() + cmp("-", NamedTextColor.RED).decorate(TextDecoration.BOLD) + cmp(" ${player.name}", if (player.hasPermission("hotpotato.start")) NamedTextColor.GOLD else NamedTextColor.GRAY))
    }

}
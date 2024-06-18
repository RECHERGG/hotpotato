package de.rechergg.hotpotato.command

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.extension.plus
import de.rechergg.hotpotato.extension.prefix
import de.rechergg.hotpotato.game.GameManager
import de.rechergg.hotpotato.game.GameState
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.format.NamedTextColor

object StartCommand {

    val command = commandTree("start") {
        withPermission("hotpotato.start")
        playerExecutor { player, _ ->
            if (GameManager.gameState != GameState.WAITING) {
                player.sendMessage(prefix() + cmp("Das Spiel wurde bereits gestartet!", NamedTextColor.RED))
                return@playerExecutor
            }

            GameManager.start(player)
        }
    }
}
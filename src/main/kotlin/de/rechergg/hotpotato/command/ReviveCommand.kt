package de.rechergg.hotpotato.command

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.extension.eliminated
import de.rechergg.hotpotato.extension.plus
import de.rechergg.hotpotato.extension.prefix
import de.rechergg.hotpotato.game.GameManager
import de.rechergg.hotpotato.game.GameState
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

object ReviveCommand {

    val command = commandTree("revive") {
        withPermission("hotpotato.revive")
        playerArgument("target") {
            playerExecutor { player, commandArguments ->
                if (GameManager.gameState != GameState.PLAYING) {
                    player.sendMessage(prefix() + cmp("Du kannst jetzt niemanden wiederbeleben!", NamedTextColor.RED))
                    return@playerExecutor
                }

                val target = commandArguments["target"] as Player
                if (!target.eliminated) {
                    player.sendMessage(prefix() + cmp("Dieser Spieler lebt noch!", NamedTextColor.RED))
                    return@playerExecutor
                }

                player.sendMessage(prefix() + cmp("Du hast ") + cmp(target.name, NamedTextColor.GOLD) + cmp(" wiederbelebt!"))
                target.eliminated = false
            }
        }
    }
}
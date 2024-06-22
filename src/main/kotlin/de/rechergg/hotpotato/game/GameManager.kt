package de.rechergg.hotpotato.game

import de.rechergg.hotpotato.extension.*
import de.rechergg.hotpotato.game.GameEffect.updateBarProgress
import de.rechergg.hotpotato.game.GameEffect.winEffect
import de.rechergg.hotpotato.game.GameFunction.explode
import de.rechergg.hotpotato.game.GameFunction.explodeFireWork
import de.rechergg.hotpotato.game.GameFunction.getRandomPlayer
import de.rechergg.hotpotato.game.ServerManager.restartServer
import de.rechergg.hotpotato.world.WorldManager
import net.axay.kspigot.extensions.bukkit.title
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.taskRunLater
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import java.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object GameManager {

    var gameState = GameState.PREPARING_WORLD
    val bossBar = Bukkit.createBossBar(cmp("Warten...", NamedTextColor.RED).toLegacy(), BarColor.RED, BarStyle.SOLID)
    val team = Bukkit.getScoreboardManager().mainScoreboard.getTeam("hotpotato")
        ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("hotpotato").apply {
            setCanSeeFriendlyInvisibles(true)
            setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
        }

    fun start(player: Player) {
        player.sendMessage(prefix() + cmp("Du hast das Spiel erfolgreich gestartet.", NamedTextColor.GREEN))
        onlinePlayers.filter { it.uniqueId != player.uniqueId }.forEach {
            it.sendMessage(
                prefix() + cmp("Das Spiel wurde von ") + cmp(
                    player.name,
                    NamedTextColor.GOLD
                ) + cmp(" gestartet!")
            )
        }
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)

        prepare()
    }

    private fun prepare() {
        gameState = GameState.STARTING

        task(true, 20, 20, 5) { task ->
            if (task.counterDownToOne == 1L) {
                taskRunLater(20, false) {
                    teleportPlayers()
                    onlinePlayers.forEach {
                        it.title(
                            cmp("Runde startet"),
                            cmp("jetzt", NamedTextColor.GOLD),
                            Duration.ZERO,
                            1.seconds.toJavaDuration(),
                            1.seconds.toJavaDuration()
                        )
                        it.playSound(it, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f)
                    }
                }
            }

            onlinePlayers.forEach {
                it.title(
                    cmp("Runde startet in"),
                    cmp(task.counterDownToOne.toString(), NamedTextColor.GOLD),
                    Duration.ZERO,
                    2.seconds.toJavaDuration(),
                    Duration.ZERO
                )
                it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
            }
        }
    }

    private fun teleportPlayers() {
        gameState = GameState.SPREADING
        onlinePlayers.forEach {
            it.teleportAsync(Location(WorldManager.world, 0.5, 81.0, 0.5))
            it.sendMessage(prefix() + cmp("Ihr habt 10 Sekunden Zeit um euch zu verteilen!"))
        }

        task(false, 20, 20, 10) { task ->
            if (task.counterDownToOne == 1L) {
                taskRunLater(20, false) {
                    onlinePlayers.forEach {
                        it.sendMessage(prefix() + cmp("Das Spiel hat begonnen!"))
                        it.playSound(it, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f)
                    }
                    startGame()
                }
            }

            when (task.counterDownToOne) {
                1L, 2L, 3L, 5L, 10L -> {
                    onlinePlayers.forEach {
                        it.sendMessage(
                            prefix() + cmp("Das Spiel startet in ") + cmp(
                                "${task.counterDownToOne} Sekunde${if (task.counterDownToOne == 1L) "" else "n"}",
                                NamedTextColor.GOLD
                            ) + cmp("!")
                        )
                        it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                    }
                }
            }
        }
    }

    private fun startGame() {
        gameState = GameState.PLAYING

        startRound()
    }

    private fun startRound() {
        bossBar.setTitle(cmp("Heiße Kartoffel", NamedTextColor.RED).toLegacy())
        bossBar.progress = 1.0

        val target = getRandomPlayer().apply {
            potato = true
        }

        onlinePlayers.filter { it != target }.forEach {
            sync {
                it.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, false, false, false))
            }
        }

        task(false, 20, 20, 10) { task ->
            tickRound(task.counterDownToZero!!)

            if (task.counterDownToZero == 0L) {
                onlinePlayers.filter { it.potato && !it.eliminated }.forEach {
                    explode(it)
                }

                if (onlinePlayers.filter { !it.eliminated }.size <= 1) {
                    taskRunLater(40) {
                        stopGame(onlinePlayers.firstOrNull { !it.eliminated })
                    }
                    return@task
                }

                taskRunLater(60) {
                    startRound()
                }
                return@task
            }
        }
    }

    private fun stopGame(winner: Player?) {
        gameState = GameState.ENDED

        winEffect(winner!!)
        restartServer()
    }

    private fun tickRound(progress: Long) {
        updateBarProgress(progress)
        explodeFireWork()
    }
}
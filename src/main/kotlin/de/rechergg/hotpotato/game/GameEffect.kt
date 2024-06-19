package de.rechergg.hotpotato.game

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.extension.toLegacy
import de.rechergg.hotpotato.game.GameManager.bossBar
import net.axay.kspigot.extensions.bukkit.title
import net.axay.kspigot.extensions.onlinePlayers
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.entity.Player
import java.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object GameEffect {

    fun winEffect(winner: Player) {
        onlinePlayers.forEach {
            bossBar.setTitle(cmp("${winner.name} gewinnt").toLegacy())
            bossBar.progress = 1.0
            bossBar.color = BarColor.GREEN

            it.title(
                cmp(winner.name, NamedTextColor.GOLD),
                cmp("hat gewonnen"),
                Duration.ZERO,
                5.seconds.toJavaDuration(),
                1.seconds.toJavaDuration()
            )

            it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1.5f)
        }
    }

    fun updateBarProgress(progress: Long) {
        bossBar.progress = (0.1 * progress).coerceIn(0.0, 1.0)
    }
}
package de.rechergg.hotpotato.game.items

import de.rechergg.hotpotato.extension.cmp
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.utils.addEffect
import net.axay.kspigot.utils.fireworkItemStack
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemsCache {

    val compass = itemStack(Material.COMPASS) {
        meta {
            displayName(cmp("Teleporter", NamedTextColor.GOLD))
        }
    }

    val potato = itemStack(Material.POTATO) {
        meta {
            displayName(cmp("Heiße Kartoffel (Achtung Heiß)", NamedTextColor.RED).decorate(TextDecoration.BOLD))
        }
    }

    fun createFireWork(): ItemStack {
        val color = listOf(Color.RED, Color.GREEN, Color.BLUE).random()
        return fireworkItemStack {
            addEffect {
                withColor(color)
                withFade(color)
                trail(true)
                flicker(true)
            }
        }
    }
}
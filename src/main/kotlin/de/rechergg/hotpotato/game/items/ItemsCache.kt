package de.rechergg.hotpotato.game.items

import de.rechergg.hotpotato.extension.cmp
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

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
}
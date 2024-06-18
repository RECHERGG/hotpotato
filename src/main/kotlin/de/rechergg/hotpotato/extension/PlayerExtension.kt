package de.rechergg.hotpotato.extension

import de.rechergg.hotpotato.game.items.ItemsCache
import net.axay.kspigot.runnables.sync
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

val potatoes = mutableListOf<UUID>()
val eliminatedPlayers = mutableListOf<UUID>()

var Player.potato: Boolean
    get() = potatoes.contains(uniqueId)
    set(value) {
        if (value) {
            potatoes.add(uniqueId)
            Bukkit.broadcast(prefix() + cmp(name, NamedTextColor.GOLD) + cmp(" ist es!"))
            sync {
                inventory.setItem(EquipmentSlot.HEAD, ItemStack(Material.TNT))
                addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, false, false, false))
            }
            (0..8).filter { it != 4 }.forEach {
                inventory.setItem(it, ItemsCache.potato)
            }
        } else {
            potatoes.remove(uniqueId)
            inventory.remove(Material.POTATO)
            sync {
                inventory.setItem(EquipmentSlot.HEAD, ItemStack(Material.AIR))
                removePotionEffect(PotionEffectType.SPEED)
            }
        }
    }

var Player.eliminated: Boolean
    get() = eliminatedPlayers.contains(uniqueId)
    set(value) {
        if (value) {
            eliminatedPlayers.add(uniqueId)
            inventory.setItem(0, ItemsCache.compass)
        } else {
            eliminatedPlayers.remove(uniqueId)
            inventory.remove(Material.COMPASS)
        }
    }
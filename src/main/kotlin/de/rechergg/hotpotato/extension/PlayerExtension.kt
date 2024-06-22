package de.rechergg.hotpotato.extension

import de.rechergg.hotpotato.HotPotato.Companion.instance
import de.rechergg.hotpotato.game.items.ItemsCache
import de.rechergg.hotpotato.world.WorldManager
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.EntityEffect
import org.bukkit.Location
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
                addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 2, false, false, false))
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
                addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, false, false, false))
            }
        }
    }

var Player.eliminated: Boolean
    get() = eliminatedPlayers.contains(uniqueId)
    set(value) {
        if (value) {
            eliminatedPlayers.add(uniqueId)
            inventory.setItem(0, ItemsCache.compass)
            allowFlight = true
            sync {
                removePotionEffect(PotionEffectType.SPEED)
                addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false))
            }
            onlinePlayers.filter { it.uniqueId != uniqueId }.forEach {
                sync {
                    it.hidePlayer(instance, this)
                }
            }

            onlinePlayers.filter { it.eliminated }.forEach {
                sync {
                    hidePlayer(instance, it)
                }
            }
        } else {
            eliminatedPlayers.remove(uniqueId)
            inventory.remove(Material.COMPASS)
            allowFlight = false
            sync {
                addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 30, 0, false, false, false))
                removePotionEffect(PotionEffectType.INVISIBILITY)
                playEffect(EntityEffect.TOTEM_RESURRECT)
            }
            teleportAsync(Location(WorldManager.world, 0.5, 81.0, 0.5))
            sendMessage(prefix() + cmp("Du wurdest wiederbelebt!"))
            onlinePlayers.filter { it.uniqueId != uniqueId }.forEach {
                sync {
                    it.showPlayer(instance, this)
                }
            }
        }
    }
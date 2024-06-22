package de.rechergg.hotpotato.game

import de.rechergg.hotpotato.HotPotato.Companion.instance
import de.rechergg.hotpotato.extension.*
import de.rechergg.hotpotato.game.items.ItemsCache.createFireWork
import de.rechergg.hotpotato.world.WorldManager.world
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.taskRunLater
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object GameFunction {

    fun eliminate(player: Player) {
        player.apply {
            eliminated = true
            potato = false
        }


        onlinePlayers.filter { it.uniqueId != player.uniqueId }.forEach {
            sync {
                it.hidePlayer(instance, player)
            }
        }

        onlinePlayers.filter { it.eliminated }.forEach {
            sync {
                player.hidePlayer(instance, it)
            }
        }
    }

    fun explode(player: Player) {
        eliminate(player)

        Bukkit.broadcast(prefix() + cmp(player.name, NamedTextColor.GOLD) + cmp(" ist explodiert!"))
        sync {
            world.createExplosion(player.location, 4.0f, false, false)
        }
    }

    fun explodeFireWork() {
        onlinePlayers.filter { it.potato }.forEach {
            val fireworkStack = createFireWork()
            var firework: Firework? = null

            sync {
                firework = world.spawnEntity(it.location, EntityType.FIREWORK_ROCKET) as Firework
                firework!!.fireworkMeta = fireworkStack.itemMeta as FireworkMeta
            }

            taskRunLater(4, false) {
                sync {
                    firework!!.detonate()
                }
            }
        }
    }

    fun getRandomPlayer(): Player = onlinePlayers.filter { !it.eliminated }.random()

}
package de.rechergg.hotpotato.listener

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.extension.eliminated
import de.rechergg.hotpotato.extension.plus
import de.rechergg.hotpotato.extension.prefix
import de.rechergg.hotpotato.game.items.ItemsCache
import de.rechergg.hotpotato.instance
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.items.customModel
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

object InteractListener {

    val key = NamespacedKey(instance, "hotpotato-gui")

    private val onInteract = listen<PlayerInteractEvent> {

        if (it.action != Action.LEFT_CLICK_AIR || it.action != Action.LEFT_CLICK_BLOCK) { //testen
            it.isCancelled = true
        }

        if (it.item != ItemsCache.compass) {
            return@listen
        }

        openInventory(it.player)
    }

    private val onDrop = listen<PlayerDropItemEvent> {
        it.isCancelled = true
    }

    private fun openInventory(player: Player) {
        if (onlinePlayers.none { !it.eliminated }) {
            player.sendMessage(prefix() + cmp("There are no players alive.", NamedTextColor.RED))
            return
        }

        val inventory = Bukkit.createInventory(null, ((onlinePlayers.filter { !it.eliminated }.size / 9) * 9).coerceAtLeast(9), cmp("Teleporter", NamedTextColor.DARK_GRAY))

        onlinePlayers.filter { !it.eliminated }.forEach {
            inventory.addItem(createPlayerHead(it))
        }

        player.openInventory(inventory)
    }

    private fun createPlayerHead(player: Player) = itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                owningPlayer = player
                displayName(cmp(player.name, NamedTextColor.GOLD))
                customModel = 1
                persistentDataContainer.set(key, PersistentDataType.STRING, player.uniqueId.toString())
            }
        }
}
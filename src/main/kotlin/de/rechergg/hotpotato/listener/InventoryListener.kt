package de.rechergg.hotpotato.listener

import de.rechergg.hotpotato.listener.InteractListener.key
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.customModel
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*

object InventoryListener {

    private val onClick = listen<InventoryClickEvent> {
        it.isCancelled = true
        val item = it.currentItem ?: return@listen
        val meta = item.itemMeta ?: return@listen
        if (meta.customModel == 1) {
            val uuid = meta.persistentDataContainer.get(key, PersistentDataType.STRING) ?: return@listen
            val player = Bukkit.getPlayer(UUID.fromString(uuid)) ?: return@listen
            it.whoClicked.teleportAsync(player.location)
            it.inventory.close()
        }
    }

    private val onSlotChange = listen<InventoryMoveItemEvent> {
        it.isCancelled = true
    }

    private val onDrag = listen<InventoryDragEvent> {
        it.isCancelled = true
    }

    private val onSwap = listen<PlayerSwapHandItemsEvent> {
        it.isCancelled = true
    }
}
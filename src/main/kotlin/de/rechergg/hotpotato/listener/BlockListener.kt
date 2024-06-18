package de.rechergg.hotpotato.listener

import net.axay.kspigot.event.listen
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityChangeBlockEvent

object BlockListener {

    private val onBreak = listen<BlockBreakEvent> {
        it.isCancelled = true
    }

    private val onPlace = listen<BlockPlaceEvent> {
        it.isCancelled = true
    }

    private val onPhysics = listen<BlockPhysicsEvent> {
        it.isCancelled = true
    }

    private val onChangeBlock = listen<EntityChangeBlockEvent> {
        it.isCancelled = true
    }
}
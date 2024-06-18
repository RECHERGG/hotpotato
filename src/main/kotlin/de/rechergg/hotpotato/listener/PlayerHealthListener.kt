package de.rechergg.hotpotato.listener

import net.axay.kspigot.event.listen
import org.bukkit.event.entity.FoodLevelChangeEvent

object PlayerHealthListener {

    private val onFoodHealth = listen<FoodLevelChangeEvent> {
        it.isCancelled = true
    }
}
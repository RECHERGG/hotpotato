package de.rechergg.hotpotato.listener

import de.rechergg.hotpotato.extension.eliminated
import de.rechergg.hotpotato.extension.potato
import de.rechergg.hotpotato.game.GameManager.gameState
import de.rechergg.hotpotato.game.GameState
import net.axay.kspigot.event.listen
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object DamageListener {

    private val onEntityDamage = listen<EntityDamageByEntityEvent> {
        if (it.damager.type != EntityType.PLAYER) {
            it.isCancelled = true
            return@listen
        }

        it.damage = 0.0

        val damager = it.damager as? Player ?: return@listen
        val target = it.entity as? Player ?: return@listen

        if (damager.eliminated) {
            it.isCancelled = true
            return@listen
        }

        if (damager.potato && !target.potato) {
            target.potato = true
            damager.potato = false
        }
    }

    private val onDamage = listen<EntityDamageEvent> {
        if (it.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK || gameState != GameState.PLAYING) {
            it.isCancelled = true
        }
    }
}
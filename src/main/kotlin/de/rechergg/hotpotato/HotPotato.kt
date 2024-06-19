package de.rechergg.hotpotato

import de.rechergg.hotpotato.command.StartCommand
import de.rechergg.hotpotato.game.GameEffect
import de.rechergg.hotpotato.game.GameFunction
import de.rechergg.hotpotato.game.GameManager
import de.rechergg.hotpotato.listener.*
import de.rechergg.hotpotato.world.WorldManager
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.axay.kspigot.main.KSpigot

class HotPotato: KSpigot() {

    companion object {
        lateinit var instance: HotPotato
    }

    override fun load() {
        instance = this
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
    }

    override fun startup() {
        CommandAPI.onEnable()
        GameEffect
        GameFunction
        GameManager
        WorldManager
        StartCommand
        ConnectionListeners
        DamageListener
        BlockListener
        InteractListener
        PlayerHealthListener
        InventoryListener
    }

    override fun shutdown() {
        CommandAPI.onDisable()
    }

}

val instance by lazy {
    HotPotato.instance
}
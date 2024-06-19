package de.rechergg.hotpotato.game

import de.rechergg.hotpotato.extension.cmp
import de.rechergg.hotpotato.extension.plus
import de.rechergg.hotpotato.extension.prefix
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.taskRunLater
import org.bukkit.Bukkit

object ServerManager {

    fun restartServer() {
        Bukkit.broadcast(prefix() + cmp("Der Server startet in 10 Sekunden neu!"))

        task(false, 0, 20, 10) { task ->
            if (task.counterDownToZero == 0L) {
                taskRunLater(20) {
                    onlinePlayers.forEach {
                        it.apply {
                            exp = 0f
                            level = 0
                        }
                    }
                }

                taskRunLater(40) {
                    Bukkit.shutdown()
                }
                return@task
            }

            onlinePlayers.forEach {
                it.apply {
                    exp = 0.1f * task.counterDownToOne!!
                    level = task.counterDownToOne!!.toInt()
                }
            }
        }
    }
}
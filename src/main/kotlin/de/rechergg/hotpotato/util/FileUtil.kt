package de.rechergg.hotpotato.util

import java.io.File

object FileUtil {

    fun copyDirectory(source: File, destination: File) {
        source.walk().forEach { src ->
            val target = File(destination, source.toPath().relativize(src.toPath()).toString())
            if (src.isDirectory) {
                target.mkdirs()
            } else {
                src.copyTo(target, overwrite = true)
            }
        }
    }
}
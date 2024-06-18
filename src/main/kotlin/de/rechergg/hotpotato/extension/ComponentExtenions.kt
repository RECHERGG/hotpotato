package de.rechergg.hotpotato.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun cmp(text: String, color: TextColor = NamedTextColor.GRAY) = Component.text(text, color)

operator fun Component.plus(other: Component) = append(other)

fun prefix() = cmp("[", NamedTextColor.DARK_GRAY) + "<gradient:#fae41b:#fa9a1b>HotPotato</gradient>".minimessage() + cmp("]", NamedTextColor.DARK_GRAY) + cmp(" ")

fun String.minimessage() = MiniMessage.miniMessage().deserialize(this)

fun Component.toLegacy() = LegacyComponentSerializer.legacy('§').serialize(this)
package org.lanzoz.hGVoteKotlin.util

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabComplete: TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        arg: String,
        args: Array<out String>
    ): List<String?>? {
        return when (args.size) {
            1 -> {
                listOf("kick","ban").filter { it.startsWith(args[0], ignoreCase = true) }
            }
            2 -> {
                Bukkit.getOnlinePlayers().map { it.name }
                    .filter { it.startsWith(args[1], ignoreCase = true) }
                    .filterNot { it.equals(sender.name, ignoreCase = true) }
            }
            else -> null
        }
    }
}
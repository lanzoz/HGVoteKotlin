package org.lanzoz.hGvote

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import org.lanzoz.hGCore.ClassicTranslate
import org.lanzoz.hGVoteKotlin.HGVoteKotlin.Companion.log
import org.lanzoz.hGVoteKotlin.PREFIX
import org.lanzoz.hGVoteKotlin.util.VoteEvent

class VoteCommand : CommandExecutor {

    private val voteEvent = VoteEvent()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            log.info(ClassicTranslate("$PREFIX You can't use player command"))
            return true
        }
        if (args.size < 2) {
            sender.sendMessage(ClassicTranslate("$PREFIX &cคุณต้องระบุคำสั่งและชื่อผู้เล่น: /vote <kick|ban> <ชื่อผู้เล่น>"))
            return true
        }

        val action = args[0]
        val targetName = args[1]
        val target = Bukkit.getPlayer(targetName)
        when {
            action.equals("kick", ignoreCase = true) -> {
                handleVote(sender, target, "เตะ") { s, t -> voteEvent.voteKick(s, t) }
            }
            action.equals("ban", ignoreCase = true) -> {
                handleVote(sender, target, "แบน") { s, t -> voteEvent.voteBan(s, t) }
            }
            else -> {
                sender.sendMessage(ClassicTranslate("$PREFIX &cคำสั่งไม่ถูกต้อง ใช้ได้แค่ /vote kick หรือ /vote ban"))
            }
        }

        return true
    }
    private fun handleVote(sender: Player, target: Player?, actionName: String, callback: (Player, Player) -> Unit) {
        when {
            target == null -> {
                sender.sendMessage(ClassicTranslate("$PREFIX &cไม่พบผู้เล่นที่ต้องการจะโหวต$actionName"))
            }
            target == sender -> {
                sender.sendMessage(ClassicTranslate("$PREFIX &cคุณระบุชื่อตัวเองเพื่อให้คนอื่นโหวต เหร่อ?"))
            }
            else -> {
                callback(sender, target)
            }
        }
    }
}
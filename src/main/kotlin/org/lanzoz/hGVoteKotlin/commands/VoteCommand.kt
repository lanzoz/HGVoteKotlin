package org.lanzoz.hGvote

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.lanzoz.hGCore.CT
import org.lanzoz.hGVoteKotlin.HGVoteKotlin.Companion.log
import org.lanzoz.hGVoteKotlin.PREFIX
import org.lanzoz.hGVoteKotlin.util.VoteEvent

class VoteCommand : CommandExecutor {

    private val voteEvent = VoteEvent()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        // 1. ตรวจสอบว่าเป็นผู้เล่นหรือไม่
        if (sender !is Player) {
            log.info(CT("$PREFIX You can't use player command"))
            return true
        }

        // 2. ตรวจสอบว่าใส่ Argument มาครบไหม (อย่างน้อยต้องมี 2 ตัวคือ kick/ban และ ชื่อผู้เล่น)
        if (args.size < 2) {
            sender.sendMessage(CT("$PREFIX &cคุณต้องระบุคำสั่งและชื่อผู้เล่น: /vote <kick|ban> <ชื่อผู้เล่น>"))
            return true
        }

        val action = args[0]
        val targetName = args[1]
        val target = Bukkit.getPlayer(targetName)

        // 3. จัดการคำสั่งด้วย when (สะอาดกว่า if-else)
        when {
            action.equals("kick", ignoreCase = true) -> {
                handleVote(sender, target, "เตะ") { s, t -> voteEvent.voteKick(s, t) }
            }
            action.equals("ban", ignoreCase = true) -> {
                handleVote(sender, target, "แบน") { s, t -> voteEvent.voteBan(s, t) }
            }
            else -> {
                sender.sendMessage(CT("$PREFIX &cคำสั่งไม่ถูกต้อง ใช้ได้แค่ /vote kick หรือ /vote ban"))
            }
        }

        return true
    }

    /**
     * ฟังก์ชันช่วยตรวจสอบเงื่อนไขพื้นฐานก่อนเริ่มโหวต เพื่อลดโค้ดซ้ำซ้อน
     */
    private fun handleVote(sender: Player, target: Player?, actionName: String, callback: (Player, Player) -> Unit) {
        when {
            target == null -> {
                sender.sendMessage(CT("$PREFIX &cไม่พบผู้เล่นที่ต้องการจะโหวต$actionName"))
            }
            target == sender -> {
                sender.sendMessage(CT("$PREFIX &cคุณระบุชื่อตัวเองเพื่อให้คนอื่นโหวต เหร่อ?"))
            }
            else -> {
                callback(sender, target)
            }
        }
    }
}
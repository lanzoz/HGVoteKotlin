package org.lanzoz.hGVoteKotlin.util

import io.papermc.paper.ban.BanListType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.lanzoz.hGCore.AT
import org.lanzoz.hGCore.CT
import org.lanzoz.hGVoteKotlin.HGVoteKotlin
import org.lanzoz.hGVoteKotlin.PREFIX
import java.util.UUID
import kotlin.math.ceil

class VoteEvent {

    val voteData = mutableMapOf<UUID, MutableSet<UUID>>()
    val voteDataBan = mutableMapOf<UUID, MutableSet<UUID>>()

    fun voteKick(voter: Player, target: Player) {
        val targetId = target.uniqueId
        val voterId = voter.uniqueId

        val voterForTarget = voteData.getOrPut(targetId) { mutableSetOf() }

        Bukkit.getScheduler().runTaskLater(HGVoteKotlin.main, Runnable{
            if (voteData.containsKey(targetId)) {
                voteData.remove(targetId)
                Bukkit.broadcast(AT("$PREFIX <yellow>การโหวตเตะ ${target.name} หมดเวลาแล้ว"))
            }
        },1200L)

        if (voterForTarget.contains(voterId)) {
            voter.sendMessage(CT("$PREFIX <yellow>คุณได้โหวตเตะ ${target.name}) ไปแล้ว"))
            return
        }
        voterForTarget.add(voterId)
        val cVote = voterForTarget.size
        val tPlay = Bukkit.getOnlinePlayers().count { it.uniqueId != targetId }

        if (tPlay < 2) {
            voter.sendMessage(CT("$PREFIX <red>ผู้เล่นต้องมีอย่างน้อย 2 คน(ไม่นับผู้ถูกโหวต)"))
            voterForTarget.remove(voterId)
            return
        }
        val reqVote = ceil(Bukkit.getOnlinePlayers().size / 2.0).toInt()
        Bukkit.broadcast(AT("$PREFIX <white>${voter.name} <yellow>ได้เริ่มโหวตเตะ <white>${target.name} <yellow>($cVote/$reqVote)"))

        if (cVote >= reqVote) {
            Bukkit.broadcast(AT("$PREFIX <red>ผู้เล่น ${target.name} ถูกเตะออกเรียบร้อย"))
            target.kick(AT("$PREFIX <red>คุณถูกเตะออกจากเซิร์ฟเวอร์จากการโหวต"))
            voteData.remove(targetId)
        }
    }

    fun voteBan(voter: Player, target: Player) {
        val targetId = target.uniqueId
        val voterId = voter.uniqueId
        val banList = Bukkit.getBanList(BanListType.PROFILE)
        val profile = Bukkit.createProfile(target.name)
        val voterForTarget = voteDataBan.getOrPut(targetId) { mutableSetOf() }

        Bukkit.getScheduler().runTaskLater(HGVoteKotlin.main, Runnable{
            if (voteDataBan.containsKey(targetId)) {
                voteDataBan.remove(targetId)
                Bukkit.broadcast(AT("$PREFIX <green>การโหวตแบน ${target.name} หมดเวลาแล้ว"))
            }
        },1200L)

        if (voterForTarget.contains(voterId)) {
            voter.sendMessage(CT("$PREFIX <yellow>คุณได้โหวตแบน ${target.name}) ไปแล้ว"))
            return
        }
        voterForTarget.add(voterId)
        val cVote = voterForTarget.size
        val tPlay = Bukkit.getOnlinePlayers().count { it.uniqueId != targetId }
        if (tPlay < 3) {
            voter.sendMessage(CT("$PREFIX <red>ผู้เล่นต้องมีอย่างน้อย 2 คน(ไม่นับผู้ถูกโหวต)"))
            voterForTarget.remove(voterId)
            return
        }
        val reqVote = ceil(Bukkit.getOnlinePlayers().size / 2.0).toInt()
        Bukkit.broadcast(AT("$PREFIX <white>${voter.name} <yellow>ได้เริ่มโหวตแบน <white>${target.name} <yellow>($cVote/$reqVote)"))

        if (cVote >= reqVote) {
            Bukkit.broadcast(AT("$PREFIX <green>ผู้เล่น ${target.name} ถูกแบนออกเรียบร้อย"))
            target.kick(AT("$PREFIX &cคุณถูกโหวตแบน $cVote เสียง เสียใจด้วย"))
            banList.addBan(profile,CT("$PREFIX &cคุณถูกแบนออกจากเซิร์ฟเวอร์จากการโหวต"),null as java.util.Date?, voter.name)
            voteDataBan.remove(targetId)
        }
    }

}
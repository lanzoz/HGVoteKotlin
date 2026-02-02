package org.lanzoz.hGVoteKotlin

import org.bukkit.plugin.java.JavaPlugin
import org.lanzoz.hGCore.HGCore
import org.lanzoz.hGVoteKotlin.util.TabComplete
import org.lanzoz.hGVoteKotlin.util.VoteEvent
import org.lanzoz.hGvote.VoteCommand
import java.util.logging.Logger
val PREFIX = "<yellow>[Vote]</yellow>"
class HGVoteKotlin : JavaPlugin() {
    private val voteEvent = VoteEvent()

    companion object {
        lateinit var main: HGVoteKotlin
            private set
        lateinit var log: Logger
            private set
        lateinit var core: HGCore
            private set
    }

    override fun onEnable() {
        main = this;
        log = this.logger;
        core = HGCore.main;
        getCommand("vote")?.apply {
            setExecutor(VoteCommand())
            tabCompleter = TabComplete()
        }

        log.info("HGVoteKotlin Plugin enabled!")

    }

    override fun onDisable() {
        log.info("HGVoteKotlin Plugin disabled!")
        runCatching {
            voteEvent.voteData.clear()
            voteEvent.voteDataBan.clear()
        }.onSuccess {
            log.info("Clear All Data Success!")
        }.onFailure { e ->
            log.warning("Cant Clear Data: ${e.message}")
        }
    }
}

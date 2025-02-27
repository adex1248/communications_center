@file:JvmName("Main")

package dev.slimevr

import dev.slimevr.osc.VRCOSCHandler
import dev.slimevr.tracking.trackers.Tracker
import io.eiren.util.logging.LogManager
import java.lang.Thread.sleep

fun main() {
    val testHandler : VRCOSCHandler = VRCOSCHandler("test", listOf(Tracker("data/tracker1.txt")))
    testHandler.updateOscSender(ip="127.0.0.1", portOut = 9000)

    while (true) {
        testHandler.update()
        try {
            sleep(1) // 1000Hz
        } catch (error: InterruptedException) {
            LogManager.info("VRServer thread interrupted")
            break
        }
    }

}


@file:JvmName("Main")

package dev.slimevr

import dev.slimevr.osc.VRCOSCHandler
import dev.slimevr.tracking.trackers.Tracker
import io.eiren.util.logging.LogManager
import java.lang.Thread.sleep

fun main() {
    val bones = listOf("head", "hip", "chest")

    val testHandler : VRCOSCHandler = VRCOSCHandler(bones)
    testHandler.updateOscSender(ip="127.0.0.1", portOut = 9000)
    testHandler.receiveDataPath("data/motion")

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


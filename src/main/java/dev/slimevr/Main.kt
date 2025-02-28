@file:JvmName("Main")

package dev.slimevr

import dev.slimevr.osc.VRCOSCHandler
import dev.slimevr.tracking.trackers.Tracker
import io.eiren.util.logging.LogManager
import java.lang.Thread.sleep

fun main() {
    val bones = listOf("hip", "leftFoot", "rightFoot", "leftUpLeg",
        "rightUpLeg", "chest", "leftUpperArm", "rightUpperArm", "head")

    val testHandler : VRCOSCHandler = VRCOSCHandler(bones)
    testHandler.updateOscSender(ip="127.0.0.1", portOut = 9000)
    testHandler.receiveDataPath("data/motion")

    while (true) {
        testHandler.update()
        try {
            sleep(33) // Actual millisecond per frame: 33
        } catch (error: InterruptedException) {
            LogManager.info("VRServer thread interrupted")
            break
        }
    }

}


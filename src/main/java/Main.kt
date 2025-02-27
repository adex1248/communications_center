package org.example

import com.illposed.osc.transport.OSCPortOut
import java.net.InetAddress
import java.net.InetSocketAddress
import dev.slimevr.osc.VRCOSCHandler
import dev.slimevr.tracking.trackers.Tracker

fun main() {
    val testHandler : VRCOSCHandler = VRCOSCHandler("test", listOf(Tracker("data/tracker1.txt")))

    testHandler.updateOscSender(ip="127.0.0.1", portOut = 9000)
    println("yayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy")

}


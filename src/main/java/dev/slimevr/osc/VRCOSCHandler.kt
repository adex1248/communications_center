package dev.slimevr.osc

import com.illposed.osc.transport.OSCPortOut
import com.illposed.osc.OSCBundle
import com.illposed.osc.OSCMessage
import com.illposed.osc.OSCMessageEvent
import com.illposed.osc.OSCMessageListener
import java.net.InetAddress
import java.net.InetSocketAddress
import java.io.IOException
import io.eiren.util.logging.LogManager

class VRCOSCHandler (
    private val server: String, // temporary
) {
    private var oscSender: OSCPortOut? = null
    private var oscPortIn = 0
    private var oscPortOut = 0
    private var oscIp: InetAddress? = null
    private var oscMessage: OSCMessage? = null

    fun updateOscSender(portOut: Int, ip: String) {
        val addr = InetAddress.getByName(ip)
        try {
            oscSender = OSCPortOut(InetSocketAddress(addr, portOut))
            LogManager.info("[VRCOSCHandler] Sending to port $portOut at address $ip")
            oscIp = addr
            oscSender?.connect()
        } catch (e: IOException) {
            return
        }
    }
    fun update() {
        val bundle = OSCBundle()

    }
}
package dev.slimevr.osc

import com.illposed.osc.*
import com.illposed.osc.transport.OSCPortOut
import com.jme3.math.FastMath
import java.net.InetAddress
import java.net.InetSocketAddress
import java.io.IOException
import dev.slimevr.tracking.trackers.Tracker
import io.eiren.util.collections.FastList
import io.eiren.util.logging.LogManager
import io.github.axisangles.ktmath.EulerAngles
import io.github.axisangles.ktmath.EulerOrder
import io.github.axisangles.ktmath.Quaternion
import io.github.axisangles.ktmath.Vector3

class VRCOSCHandler (
    private val server: String, // temporary
    private val Trackers: List<Tracker>,
) {
    private var oscSender: OSCPortOut? = null
    private var oscPortIn = 0
    private var oscPortOut = 0
    private var oscIp: InetAddress? = null
    private var oscMessage: OSCMessage? = null
    private val oscArgs = FastList<Float?>(3)
    init {
        println("here")
    }

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

        for (i in Trackers.indices) {
            val data = Trackers[i].getValues()
            if (data == null) return
            oscArgs.clear()
            oscArgs.add(data[0])
            oscArgs.add(data[1])
            oscArgs.add(-data[2])
            bundle.addPacket(
                OSCMessage(
                    "/tracking/trackers/${i}/position",
                    oscArgs.clone(),
                ),
            )

            // Assume Rotation data is in Quaternion
            val (_, x, y, z) = Quaternion(
                data[3],
                -data[4],
                -data[5],
                data[6],
            ).toEulerAngles(EulerOrder.YXZ)
            oscArgs.clear()
            oscArgs.add(x * FastMath.RAD_TO_DEG)
            oscArgs.add(y * FastMath.RAD_TO_DEG)
            oscArgs.add(z * FastMath.RAD_TO_DEG)
            bundle.addPacket(
                OSCMessage(
                    "/tracking/trackers/${i}/rotation",
                    oscArgs.clone(),
                ),
            )
            try {
                oscSender?.send(bundle)
            } catch (e: IOException) {
                // Avoid spamming AsynchronousCloseException too many
                // times per second
                LogManager.warning("[VRCOSCHandler] Error sending OSC message to VRChat: $e")
            } catch (e: OSCSerializeException) {
                LogManager.warning("[VRCOSCHandler] Error sending OSC message to VRChat: $e")
            }
        }
    }
}
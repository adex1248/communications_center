package dev.slimevr.osc

import com.illposed.osc.*
import com.illposed.osc.transport.OSCPortOut
import com.jme3.math.FastMath
import java.net.InetAddress
import java.net.InetSocketAddress
import java.io.IOException
import dev.slimevr.tracking.trackers.Tracker
import dev.slimevr.tracking.trackers.Frame
import io.eiren.util.collections.FastList
import io.eiren.util.logging.LogManager
import io.github.axisangles.ktmath.EulerAngles
import io.github.axisangles.ktmath.EulerOrder
import io.github.axisangles.ktmath.Quaternion
import io.github.axisangles.ktmath.Vector3

class VRCOSCHandler (
    private val bones : List<String>,
) {
    private var oscSender: OSCPortOut? = null
    //private var oscPortIn = 0
    //private var oscPortOut = 0
    private var oscIp: InetAddress? = null
    //private var oscMessage: OSCMessage? = null
    private val oscArgs = FastList<Float?>(3)
    private var frame : Frame = Frame(bones, "")

    fun receiveDataPath(dataPath: String) {
        frame = Frame(bones, dataPath)
    }

    fun updateOscSender(portOut: Int, ip: String) {
        val addr = InetAddress.getByName(ip)
        try {
            oscSender = OSCPortOut(InetSocketAddress(addr, portOut))
            LogManager.info("[VRCOSCHandler] Sending to port $portOut at address $ip")
            oscIp = addr
            oscSender?.connect()
            /*
            * Please implement what the program should do
            * once trackers' sequence is over
            * */
        } catch (e: IOException) {
            return
        }
    }
    fun update() {
        val bundle = OSCBundle()

        for (i in bones.indices) {
            //var temp = getVRCOSCTrackersId(bones[i])
            //println("$temp")
            val data = frame.getValues(bones[i])

            data ?: return
            oscArgs.clear()
            oscArgs.add(data[0])
            oscArgs.add(data[1])
            oscArgs.add(-data[2])
            bundle.addPacket(
                OSCMessage(
                    "/tracking/trackers/${getVRCOSCTrackersId(bones[i])}/position", // This address is wrong
                    oscArgs.clone(),
                ),
            )

            // Assume Rotation data is in Euler

            oscArgs.clear()
            oscArgs.add(data[3])
            oscArgs.add(data[4])
            oscArgs.add(data[5])
            bundle.addPacket(
                OSCMessage(
                    "/tracking/trackers/${getVRCOSCTrackersId(bones[i])}/rotation",
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

            if (frame.hasNextFrame()) frame.update()
        }
    }
    private fun getVRCOSCTrackersId(trackerPosition: String): String {
        // Needs to range from 1-8.
        // Don't change as third party applications may rely
        // on this for mapping trackers to body parts.
        return when (trackerPosition) {
            "hip" -> "1"
            "leftFoot" -> "2"
            "rightFoot" -> "3"
            "leftUpLeg" -> "4"
            "rightUpLeg" -> "5"
            "chest" -> "6"
            "leftUpperArm" -> "7"
            "rightUpperArm" -> "8"
            "head" -> "head"
            else -> "-1"
        }
    }
}
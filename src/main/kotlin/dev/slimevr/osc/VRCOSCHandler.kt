package dev.slimevr.osc

/**
 * VRChat OSCTracker documentation: https://docs.vrchat.com/docs/osc-trackers
 */
class VRCOSCHandler (
    private val test: Int = 0
    //private val server: VRServer,
    //private val config: VRCOSCConfig,
    //private val computedTrackers: List<Tracker>,
) : OSCHandler {
    private val vrsystemTrackersAddresses = arrayOf(
        "/tracking/vrsystem/head/pose",
        "/tracking/vrsystem/leftwrist/pose",
        "/tracking/vrsystem/rightwrist/pose",
    )

    init {

    }
}
package org.example.dev.slimevr.config

open class OSCConfig {

	// Are the OSC receiver and sender enabled?
	var enabled = true // For VRCOSC

	// Port to receive OSC messages from
	var portIn = 9001 // For VRCOSC

	// Port to send out OSC messages at
	var portOut = 9000 // For VRCOSC

	// Address to send out OSC messages at
	var address = "127.0.0.1"
}

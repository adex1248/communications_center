package dev.slimevr.tracking.trackers

import io.github.axisangles.ktmath.Quaternion
import io.github.axisangles.ktmath.Vector3
import java.io.File
import java.io.BufferedReader

class Tracker(filePath: String) {

    // Open a BufferedReader on the file.
    private val reader: BufferedReader = File(filePath).bufferedReader()

    /**
     * Reads the next line from the file and converts it to a list of 7 Float values.
     * Returns null if there are no more lines.
     */
    fun getValues(): List<Float>? {
        val line = reader.readLine() ?: return null

        // Split the line using whitespace as delimiter and filter out empty tokens.
        val tokens = line.split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (tokens.size != 7) {
            throw IllegalArgumentException("Expected 7 values per line but found ${tokens.size}")
        }

        // Convert each token to a Float and return as a list.
        return tokens.map { it.toFloat() }
    }

    /**
     * Closes the underlying file reader.
     */
    fun close() {
        reader.close()
    }
}
package dev.slimevr.tracking.trackers

import java.io.File

class Tracker(
    private val boneName: String,
    dataFolderPath: String
) {
    private val csvFiles: List<File>
    private var currentFileIndex = 0

    // Holds the most recently loaded values for this bone (PosX, PosY, PosZ, RotX, RotY, RotZ).
    private var currentValues: List<Float>? = null

    init {
        // Gather and sort all CSV files in the specified folder.
        csvFiles = File(dataFolderPath)
            .listFiles { file -> file.isFile && file.extension == "csv" }
            ?.sortedBy { it.name }    // Sort by filename (alphabetical)
            ?: emptyList()

        // Load the first CSV (index 0) if available.
        loadBoneData(currentFileIndex)
    }

    /**
     * Attempts to load bone data from the CSV file at the given index.
     * If the file or bone line is not found, currentValues becomes null.
     */
    private fun loadBoneData(index: Int) {
        if (index !in csvFiles.indices) {
            // Invalid index: no more files, or none at all
            currentValues = null
            return
        }

        val file = csvFiles[index]
        val lines = file.readLines()
        if (lines.isEmpty()) {
            currentValues = null
            return
        }

        // Skip the header line and look for the bone row
        val dataLines = lines.drop(1)
        for (line in dataLines) {
            // Each line: BoneName,PosX,PosY,PosZ,RotX,RotY,RotZ
            val tokens = line.split(",").map { it.trim() }
            if (tokens.size == 7 && tokens[0] == boneName) {
                // Parse the remaining 6 float values
                val posX = tokens[1].toFloatOrNull()
                val posY = tokens[2].toFloatOrNull()
                val posZ = tokens[3].toFloatOrNull()
                val rotX = tokens[4].toFloatOrNull()
                val rotY = tokens[5].toFloatOrNull()
                val rotZ = tokens[6].toFloatOrNull()

                // If all are non-null, store them
                if (posX != null && posY != null && posZ != null &&
                    rotX != null && rotY != null && rotZ != null
                ) {
                    currentValues = listOf(posX, posY, posZ, rotX, rotY, rotZ)
                    return
                }
            }
        }
        // If we didn’t find the bone’s line, set currentValues to null
        currentValues = null
    }

    /**
     * Returns the currently loaded 6 float values for this bone,
     * or null if none are loaded.
     */
    fun getValues(): List<Float>? = currentValues

    /**
     * Moves to the next CSV file in the folder (in sorted order),
     * reads the line for our bone, and updates currentValues.
     */
    fun update() {
        currentFileIndex++
        loadBoneData(currentFileIndex)
    }
}

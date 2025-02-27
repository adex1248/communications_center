package dev.slimevr.tracking.trackers

import java.io.File

class Frame(
    private val bonesToTrack: List<String>,
    dataFolderPath: String
) {
    // List of CSV files in the specified folder, sorted by name
    private val csvFiles: List<File>

    // Which CSV (frame) we are currently on
    private var currentFileIndex = 0

    // Map from boneName -> 6 float values [PosX, PosY, PosZ, RotX, RotY, RotZ]
    private val currentFrameData = mutableMapOf<String, List<Float>>()

    init {
        // Find all .csv files in the folder and sort them (alphabetically by filename).
        csvFiles = File(dataFolderPath)
            .listFiles { file -> file.isFile && file.extension == "csv" }
            ?.sortedBy { it.name }
            ?: emptyList()

        // Immediately load the first CSV (if any)
        loadFrame(0)
    }

    /**
     * Reads the CSV at [index] and updates [currentFrameData]
     * to contain the 6 float values for each bone in [bonesToTrack].
     */
    private fun loadFrame(index: Int) {
        // Clear old data
        currentFrameData.clear()

        if (index !in csvFiles.indices) {
            // If index is out of range, do nothing
            return
        }

        val file = csvFiles[index]
        val lines = file.readLines()
        if (lines.isEmpty()) {
            return
        }

        // Skip the header line.
        // Each data line is assumed to be: BoneName,PosX,PosY,PosZ,RotX,RotY,RotZ
        val dataLines = lines.drop(1)

        for (line in dataLines) {
            val tokens = line.split(",").map { it.trim() }
            if (tokens.size == 7) {
                val boneName = tokens[0]
                if (boneName in bonesToTrack) {
                    // Parse the 6 float values
                    val posX = tokens[1].toFloatOrNull()
                    val posY = tokens[2].toFloatOrNull()
                    val posZ = tokens[3].toFloatOrNull()
                    val rotX = tokens[4].toFloatOrNull()
                    val rotY = tokens[5].toFloatOrNull()
                    val rotZ = tokens[6].toFloatOrNull()

                    // Only store if all are parsed correctly
                    if (posX != null && posY != null && posZ != null &&
                        rotX != null && rotY != null && rotZ != null
                    ) {
                        currentFrameData[boneName] = listOf(
                            posX, posY, posZ, rotX, rotY, rotZ
                        )
                    }
                }
            }
        }
    }

    /**
     * Returns the six float values for the given bone
     * ([PosX, PosY, PosZ, RotX, RotY, RotZ]) from the currently loaded frame,
     * or null if the bone wasn't found or not loaded.
     */
    fun getValues(boneName: String): List<Float>? {
        return currentFrameData[boneName]
    }

    /**
     * Move to the next CSV file (next frame) if there is one.
     * After calling this, [getValues] will refer to data from the new frame.
     */
    fun update() {
        // Check if there is a next file
        if (currentFileIndex < csvFiles.size - 1) {
            currentFileIndex++
            loadFrame(currentFileIndex)
        } else {
            // No more frames
            currentFrameData.clear()
        }
    }

    /**
     * Checks if there is another frame (CSV file) available after the current one.
     */
    fun hasNextFrame(): Boolean {
        return currentFileIndex < csvFiles.size - 1
    }

    /**
     * Returns the current frame index (0-based).
     */
    fun getFrameIndex(): Int {
        return currentFileIndex
    }

    /**
     * Returns the file name of the current CSV,
     * or null if there are no CSV files.
     */
    fun getFrameFileName(): String? {
        return csvFiles.getOrNull(currentFileIndex)?.name
    }
}

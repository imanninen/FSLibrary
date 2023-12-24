package fslibrary

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile


abstract class FSEntry(
    val name: String
)

class FSFile(
    fileName: String,
    val content: String
) : FSEntry(fileName)

class FSFolder(
    folderName: String,
    val content: List<FSEntry>
) : FSEntry(folderName)

class FSCreator {
    fun create(entryToCreate: FSEntry, destination: String) {
        if (isInvalidEntryName(entryToCreate))
            throw InvalidCharactersInName("You write bad name to file/folder!")
        var newDestination = if (destination.endsWith("/")) destination else "$destination/"
        if (entryToCreate is FSFile) {
            try {
                newDestination = "$newDestination${entryToCreate.name}"
                Path(newDestination).createFile()
                File(newDestination).writeText(entryToCreate.content)
            } catch (_: Exception) {
            }
        }
        if (entryToCreate is FSFolder) {
            if (hasSameNames(entryToCreate))
                throw TwoFilesWithSameNamesException(
                    "In one directory can't be 2 folders or/and files with same names!"
                )

            newDestination = "$newDestination${entryToCreate.name}"
            if (!newDestination.endsWith("/"))
                newDestination = "$newDestination/"
            Path(newDestination).createDirectory()
            entryToCreate.content.forEach { this.create(it, newDestination) }

        }
    }
}
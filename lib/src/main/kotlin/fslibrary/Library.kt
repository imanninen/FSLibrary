package fslibrary

import java.io.File
import java.util.IllegalFormatFlagsException
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile


abstract class FSEntry(
    val name: String
)

class FSFile(
    fileName: String,
    val content: String
): FSEntry(fileName)

class FSFolder(
    folderName: String,
    val content: List<FSEntry>
): FSEntry(folderName)

class FSCreator() {
    fun create(entryToCreate: FSEntry, destination: String) {
        if (entryToCreate.name == "")
            return
        var newDestination = if (destination.endsWith("/")) destination else "$destination/"
        if (entryToCreate is FSFile) {
            try {
                newDestination = "$newDestination${entryToCreate.name}"
                Path(newDestination).createFile()
                File(newDestination).writeText(entryToCreate.content)
            } catch (e: Exception) {
                // println("failed to crate file: $newDestination")
            } catch (e: FileAlreadyExistsException) {
                // println("There already exists file with current path.")
            }
        }
        if (entryToCreate is FSFolder) {
            if (hasSameNames(entryToCreate))
                throw IllegalStateException("In one directory can't be 2 folders or/and files with same names!")
            try {
                newDestination = "$newDestination${entryToCreate.name}"
                if (! newDestination.endsWith("/"))
                    newDestination = "$newDestination/"
                Path(newDestination).createDirectory()
                entryToCreate.content.forEach { this.create(it, newDestination) }
            } catch (_: FileAlreadyExistsException) {

            }
        }
    }
}
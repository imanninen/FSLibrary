package myLib

import java.io.File
import java.io.IOException
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
        var newDestination = if (destination.endsWith("/")) destination else "$destination/"
        if (entryToCreate is FSFile) {
            try {
                newDestination = "$newDestination${entryToCreate.name}"
                Path(newDestination).createFile()
                File(newDestination).writeText(entryToCreate.content)
            } catch (e: Exception) {
                if (e is IOException) {
                    println("File already exists or there was IO error!")
                } else {
                    println("There is some strange exception!")
                }
            } catch (e: FileAlreadyExistsException) {
                println("There already exists file with current path.")
            }
        }
        if (entryToCreate is FSFolder) {
            try {
                newDestination = "$newDestination${entryToCreate.name}"
                Path(newDestination).createDirectory()
                entryToCreate.content.forEach { this.create(it, newDestination) }
            } catch (e: Exception) {
                if (e is IOException) {
                    println("File already exists or there was IO error!")
                } else {
                    println("There is some strange exception!")
                }
            } catch (e: FileAlreadyExistsException) {
                println("There already exists file with current path.")
            }
        }
    }
}
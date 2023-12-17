package fslibrary

import myLib.FSEntry
import myLib.FSFile
import myLib.FSFolder
import org.jetbrains.academy.test.system.models.method.TestMethodInvokeData
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import javax.print.attribute.standard.Destination
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {

    companion object {
        private const val destination = "./src/test/kotlin/fslibrary/testDir"
        private val arg1 = FSFolder(
            "hello",
            listOf(
                FSFile("hi", "Hi, world!"),
                FSFile("Aboba", "Ababa")
            )
        )
        @JvmStatic
        fun fsCreateMethodTestData() = listOf(
            Arguments.of(arg1, destination)
        )
    }

    @Test
    fun fsEntryClassSimpleTest() {
        val clazz = fsEntryClassTest.checkBaseDefinition()
        fsEntryClassTest.checkFieldsDefinition(clazz)
    }

    @Test
    fun fsFileClassSimpleTest() {
        val clazz = fsFileClassTest.checkBaseDefinition()
        fsFileClassTest.checkFieldsDefinition(clazz)
        assertEquals(clazz.annotatedSuperclass.type, FSEntry::class.java,
            "FSFile class should extend FSEntry class")
    }

    @Test
    fun fsFolderClassSimpleTest() {
        val clazz = fsFolderClassTest.checkBaseDefinition()
        fsFolderClassTest.checkFieldsDefinition(clazz)
        assertEquals(clazz.annotatedSuperclass.type, FSEntry::class.java,
            "FSFile class should extend FSEntry class")
    }


    @Test
    fun fsCreatorClassSimpleTest() {
        val clazz = fsCreatorClassTest.checkBaseDefinition()
        fsCreatorClassTest.checkDeclaredMethods(clazz)
    }

    @ParameterizedTest
    @MethodSource("fsCreateMethodTestData")
    fun fsCreateMethodStructureTest(testFSEntry: FSEntry, destination: String) {
        val invokeData = TestMethodInvokeData(fsCreatorClassTest, fsCreateMethodTest)
        try {
            fsCreatorClassTest.invokeMethodWithArgs(
                args = arrayOf(testFSEntry, destination),
                invokeData = invokeData,
                isPrivate = false
            )
        } catch (_: IllegalArgumentException) {}
        recStructureTest(testFSEntry, destination)
    }

    private fun recStructureTest(currentFSEntry: FSEntry, currentDestination: String) {
        val newDestination = if (currentDestination.endsWith("/"))
            "$currentDestination${currentFSEntry.name}" else "$currentDestination/${currentFSEntry.name}"

        if (currentFSEntry is FSFile) {
            assertTrue(File(newDestination).exists()) {"There is no file with path: $newDestination"}
            val actualContent = File(newDestination).readText()
            assertEquals(currentFSEntry.content, actualContent, "In the file: $currentDestination " +
                    "should be this: ${currentFSEntry.content}")
        }
        if (currentFSEntry is FSFolder) {
            assertTrue(Path(newDestination).exists()) {"There is no folder with path: $newDestination"}
            val listOfFSEntries = currentFSEntry.content
            listOfFSEntries.forEach { recStructureTest(it, newDestination) }
        }
    }
}

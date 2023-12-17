package fslibrary

import org.jetbrains.academy.test.system.models.method.TestMethodInvokeData
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class LibraryTest {
    companion object {
        private const val destination = "./src/test/kotlin/fslibrary/testDir"
        private val arg1 = FSFolder(
            "hello",
            listOf(
                FSFile("hi.txt", "Hi, world!"),
                FSFile("Aboba.txt", "Ababa"),
                FSFolder(
                    "flg", listOf(
                        FSFile("last", "last file!")
                    )
                )
            )
        )
        private val arg2 = FSFolder(
            "hello",
            listOf(
                FSFile("hello", "hello"),
                FSFolder(
                    "hello", listOf(
                        FSFolder(
                            "hello", listOf(
                                FSFile("hello", "hello"),
                                FSFolder("hello", listOf())
                            )
                        )
                    )
                )
            )
        )
        private val arg3 = FSFolder(
            "hi", listOf(
                FSFolder(
                    "hi", listOf(
                        FSFolder(
                            "hi", listOf(
                                FSFolder("hi", listOf()),
                                FSFolder("hi", listOf())
                            )
                        )
                    )
                )
            )
        )

        @JvmStatic
        fun fsCreateMethodTestData() = listOf(
            Arguments.of(arg1, destination),
            // Arguments.of(arg2, destination),
        )

        @JvmStatic
        fun fsCreateMethodSameNamesTestData() = listOf(
            // Arguments.of(arg2, destination),
            Arguments.of(arg3, destination)
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
        assertEquals(
            clazz.annotatedSuperclass.type, FSEntry::class.java,
            "FSFile class should extend FSEntry class"
        )
    }

    @Test
    fun fsFolderClassSimpleTest() {
        val clazz = fsFolderClassTest.checkBaseDefinition()
        fsFolderClassTest.checkFieldsDefinition(clazz)
        assertEquals(
            clazz.annotatedSuperclass.type, FSEntry::class.java,
            "FSFile class should extend FSEntry class"
        )
    }


    @Test
    fun fsCreatorClassSimpleTest() {
        val clazz = fsCreatorClassTest.checkBaseDefinition()
        fsCreatorClassTest.checkDeclaredMethods(clazz)
    }

    @OptIn(ExperimentalPathApi::class)
    @ParameterizedTest
    @MethodSource("fsCreateMethodTestData")
    fun fsCreateMethodStructureTest(testFSEntry: FSEntry, destination: String) {
        val path = if (destination.endsWith("/")) Path("$destination${testFSEntry.name}") else
            Path("$destination/${testFSEntry.name}")
        val invokeData = TestMethodInvokeData(fsCreatorClassTest, fsCreateMethodTest)
        try {
            fsCreatorClassTest.invokeMethodWithArgs(
                args = arrayOf(testFSEntry, destination),
                invokeData = invokeData,
                isPrivate = false
            )
        } catch (_: IllegalArgumentException) {
        }
        recStructureTest(testFSEntry, destination)
        try {
            path.deleteRecursively()
        } catch (_: Exception) {
        }
    }

    @OptIn(ExperimentalPathApi::class)
    @ParameterizedTest
    @MethodSource("fsCreateMethodSameNamesTestData")
    fun fsCreateMethodSameNamesTest(testFSEntry: FSEntry, destination: String) {
        val path = if (destination.endsWith("/")) Path("$destination${testFSEntry.name}") else
            Path("$destination/${testFSEntry.name}")
        val invokeData = TestMethodInvokeData(fsCreatorClassTest, fsCreateMethodTest)
        try {
            fsCreatorClassTest.invokeMethodWithArgs(
                args = arrayOf(testFSEntry, destination),
                invokeData = invokeData,
                isPrivate = false
            )
        } catch (e: InvocationTargetException) {
            try {
                path.deleteRecursively()
            } catch (_: Exception) {
            }
            assertTrue(e.targetException is IllegalStateException, "You should throw IllegalStateException!")
        }
        catch (e: IllegalArgumentException) {

            try {
                path.deleteRecursively()
            } catch (_: Exception) { }
        }
        try {
            path.deleteRecursively()
        } catch (_: Exception) { }

    }

    private fun recStructureTest(currentFSEntry: FSEntry, currentDestination: String) {
        val newDestination = if (currentDestination.endsWith("/"))
            "$currentDestination${currentFSEntry.name}" else "$currentDestination/${currentFSEntry.name}"

        if (currentFSEntry is FSFile) {
            assertTrue(File(newDestination).exists()) { "There is no file with path: $newDestination" }
            val actualContent = File(newDestination).readText()
            assertEquals(
                currentFSEntry.content, actualContent, "In the file: $currentDestination " +
                        "should be this: ${currentFSEntry.content}"
            )
        }
        if (currentFSEntry is FSFolder) {
            assertTrue(Path(newDestination).exists()) { "There is no folder with path: $newDestination" }
            val listOfFSEntries = currentFSEntry.content
            listOfFSEntries.forEach { recStructureTest(it, newDestination) }
        }
    }
}

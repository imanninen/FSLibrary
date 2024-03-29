package fslibrary

import org.jetbrains.academy.test.system.models.method.TestMethodInvokeData
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.nio.charset.IllegalCharsetNameException
import java.util.*
import kotlin.io.path.*
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
    init {
        try {
            Path("./src/test/kotlin/fslibrary/testDir/").createDirectory()
        } catch (_: Exception) {
        }
    }

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
        private val arg6 = FSFile("OneFile.txt", "aboba")

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

        private val arg4 = FSFolder(
            "",
            listOf()
        )

        private val arg5 = FSFolder(
            "empty",
            listOf(FSFile("", "empty"))
        )

        private val arg7 = FSFolder(
            "invalidName", listOf(
                FSFile("invalidFile/name.txt", "bad naming!"),
                FSFolder("invalidFolder/name", listOf())
            )
        )
        private val arg8 = FSFolder(
            "invalid Name", listOf(
                FSFile("GoodName.txt", "good name!")
            )
        )

        private val arg9 = FSFolder(
            "First", listOf(
                FSFolder("Second", listOf()),
                FSFile("Third1", "content"),
                FSFile("Third2", "content"),
                FSFile("Third3", "content"),
                FSFile("Third4", "content"),
                FSFile("Third5", "content"),
                FSFile("Third7", "content"),
                FSFile("Third8", "content"),
                FSFile("Third9", "content"),
                FSFile("Third10", "content10")
            )
        )

        private val arg10 = FSFolder(
            "First", listOf(
                FSFolder(
                    "Second", listOf(
                        FSFolder(
                            "Third", listOf(
                                FSFolder(
                                    "Fourth", listOf(
                                        FSFolder(
                                            "Fifth", listOf(
                                                FSFolder("Fifth", listOf())
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        @JvmStatic
        fun fsCreateMethodTestData() = listOf(
            Arguments.of(arg1, destination),
            Arguments.of(arg6, destination),
            Arguments.of(arg9, destination)
        )

        @JvmStatic
        fun fsCreateMethodSameNamesTestData() = listOf(
            Arguments.of(arg2, destination),
            Arguments.of(arg3, destination),
            Arguments.of(arg10, destination)
        )

        @JvmStatic
        fun fsCreateMethodInvalidNameTestData() = listOf(
            Arguments.of(arg4, destination),
            Arguments.of(arg5, destination),
            Arguments.of(arg7, destination),
            Arguments.of(arg8, destination)
        )

        @JvmStatic
        fun fsCreateMethodFileExistsTestData() = listOf(
            Arguments.of(arg1, destination, arg1),
            Arguments.of(arg6, destination, arg6),
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
            assertTrue(e.targetException is TwoFilesWithSameNamesException,
                "You should throw TwoFilesWithSameNamesException!")
        } catch (e: IllegalArgumentException) {
            try {
                path.deleteRecursively()
            } catch (_: Exception) {
            }
        }
        try {
            path.deleteRecursively()
        } catch (_: Exception) {
        }
    }

    @OptIn(ExperimentalPathApi::class)
    @ParameterizedTest
    @MethodSource("fsCreateMethodInvalidNameTestData")
    fun fsCreateMethodInvalidNameTest(testFSEntry: FSEntry, destination: String) {
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
            assertTrue(e.targetException is InvalidCharactersInName,
                "You should throw InvalidCharactersInName!")
        } catch (_: IllegalArgumentException) {}

        if (testFSEntry.name != "")
            try {
                path.deleteRecursively()
            } catch (_: Exception) {
            }
    }

    @OptIn(ExperimentalPathApi::class)
    @ParameterizedTest
    @MethodSource("fsCreateMethodFileExistsTestData")
    fun fsCreateMethodFileExistsTest(initEntry: FSEntry, destination: String, sameEntry: FSEntry) {
        val path = if (destination.endsWith("/")) Path("$destination${initEntry.name}") else
            Path("$destination/${initEntry.name}")
        val invokeData = TestMethodInvokeData(fsCreatorClassTest, fsCreateMethodTest)
        try {
            fsCreatorClassTest.invokeMethodWithArgs(
                args = arrayOf(initEntry, destination),
                invokeData = invokeData,
                isPrivate = false
            )
        } catch (_: IllegalArgumentException) {
        }
        try {
            fsCreatorClassTest.invokeMethodWithArgs(
                args = arrayOf(sameEntry, destination),
                invokeData = invokeData,
                isPrivate = false
            )
        } catch (e: InvocationTargetException) {
            try {
                path.deleteRecursively()
            } catch (_: Exception) {
            }
            assertTrue(e.targetException is java.nio.file.FileAlreadyExistsException,
                "You should throw FileAlreadyExistsException! You throw ${e.targetException}.")
        } catch (_: IllegalArgumentException) {}

        try {
            path.deleteRecursively()
        } catch (_: Exception) {
        }
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

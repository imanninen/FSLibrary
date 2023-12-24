package fslibrary

import java.nio.charset.IllegalCharsetNameException
import java.util.function.DoubleBinaryOperator

internal fun hasSameNames(testFolder: FSFolder): Boolean {
    val map = testFolder.content.map { it.name to testFolder.content.count { its -> its.name == it.name } }
    map.forEach { (_, count) -> if (count > 1) return true }
    return false
}

internal fun isInvalidEntryName(entry: FSEntry): Boolean =
    entry.name == "" || entry.name.contains("/") ||
            entry.name.contains(" ")


class TwoFilesWithSameNamesException(message: String) : IllegalStateException(message)

class InvalidCharactersInName(message: String) : IllegalCharsetNameException(message)

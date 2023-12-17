package fslibrary

import myLib.FSEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
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

}

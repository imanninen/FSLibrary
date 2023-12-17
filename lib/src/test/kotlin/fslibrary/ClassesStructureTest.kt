package fslibrary

import org.jetbrains.academy.test.system.models.TestKotlinType
import org.jetbrains.academy.test.system.models.Visibility
import org.jetbrains.academy.test.system.models.classes.TestClass
import org.jetbrains.academy.test.system.models.method.TestMethod
import org.jetbrains.academy.test.system.models.variable.TestVariable
import org.jetbrains.academy.test.system.models.variable.VariableMutability

internal val fsEntryClassTest = TestClass(
    "FSEntry",
    "myLib",
    declaredFields = listOf(
        TestVariable(
            name = "name",
            javaType = "string",
            mutability = VariableMutability.VAL,
            visibility = Visibility.PUBLIC
        )
    ),
)

internal val fsFileClassTest = TestClass(
    "FSFile",
    "myLib",
    declaredFields = listOf(
        TestVariable(
            name = "content",
            javaType = "String",
            mutability = VariableMutability.VAL,
            visibility = Visibility.PUBLIC
        ),
    ),
)

internal val fsFolderClassTest = TestClass(
    "FSFolder",
    "myLib",
    declaredFields = listOf(
        TestVariable(
            name = "content",
            javaType = "List",
            mutability = VariableMutability.VAL,
            visibility = Visibility.PUBLIC
        ),
    ),
)

internal val fsCreateMethodTest = TestMethod(
    "create",
    TestKotlinType("void"),
    arguments = listOf(
        TestVariable(
            "entryToCreate",
            javaType = "FSEntry"
        ),
        TestVariable(
            "destination",
            javaType = "String"
        )
    )
)

internal val fsCreatorClassTest = TestClass(
    "FSCreator",
    "myLib",
    customMethods = listOf(
        fsCreateMethodTest
    )
)
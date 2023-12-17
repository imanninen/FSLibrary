package fslibrary

fun hasSameNames(testFolder: FSFolder): Boolean {
    val map = testFolder.content.map { it.name to testFolder.content.count { its -> its.name == it.name } }
    map.forEach { (_, count) ->  if (count > 1) return true }
    return false
}
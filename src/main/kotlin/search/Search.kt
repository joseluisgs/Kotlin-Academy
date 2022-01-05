package search

import search.SearchType.*
import java.io.File
import kotlin.system.exitProcess

/**
 * Search Type enum
 */
enum class SearchType(val type: String) {
    ALL("all".uppercase()),
    ANY("any".uppercase()),
    NONE("none".uppercase()),
    NULL("");

    companion object {
        fun findType(type: String):SearchType {
            values().forEach { enum ->
                if (type == enum.type) return enum
            }
            return NULL
        }
    }
}

fun main(args: Array<String>) {
    checkArgs(args)
    val file = checkFile(args[1])
    val data = readData(file)
    val index = createIndex(data)

    // Actions
    doMenuActions(data, index)

}

/**
 * Menu and Actions
 * @param data List of data
 * @param index Index of data
 */
private fun doMenuActions(
    data: List<String>,
    index: MutableMap<String, MutableSet<Int>>
) {
    do {
        val menu = readMenuOption()
        when (menu) {
            0 -> exit()
            1 -> findPerson(data, index)
            2 -> printAll(data)
        }
    } while (menu != 0)
}

/**
 * Create and inverted index
 * @param data list of data
 * @return map of data: list of indexes
 */
private fun createIndex(data: List<String>): MutableMap<String, MutableSet<Int>> {
    val index = mutableMapOf<String, MutableSet<Int>>()

    data.indices.forEach { line ->
        val words = data[line].split(" ")
        words.forEach { word ->
            val key = word.lowercase()
            if (index.containsKey(key)) {
                index[key]!!.add(line)
            } else {
                index[key] = mutableSetOf(line)
            }
        }
    }

    return index
}

/**
 * read Data from a File
 * @param file
 * @return List<String>
 */
private fun readData(file: File) = file.readLines()

/**
 * Check data file exists
 * @param fileArg file name
 * @return File
 */
private fun checkFile(fileArg: String): File {
    val fileName = "src/$fileArg"
    val file = File(fileName)
    if (!file.exists()) {
        println("File $fileName does not exist")
        exitProcess(1)
    }
    return file
}

/**
 * Check if the command line arguments are valid
 */
private fun checkArgs(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: --data <file.text>")
        exitProcess(1)
    }
}

/**
 * Print all data
 * @param data List of data
 */
fun printAll(data: List<String>) {
    println("=== List of people ===")
    data.forEach { println(it) }
}

/**
 * Find person
 * @param data List of data
 * @param index Index of data
 */
fun findPerson(data: List<String>, index: MutableMap<String, MutableSet<Int>>) {
    val searchType = readStrategy()
    val query = readQuery().lowercase().split(" ")
    search(data, index, query, searchType)
}

/**
 * Read search strategy
 * @return SearchType
 */
fun readStrategy(): SearchType {
    var searchType: SearchType
    do {
        println("Select a matching strategy: ALL, ANY, NONE")
        val type = readln().uppercase()
        searchType = SearchType.findType(type)
    } while (searchType == NULL)
    return searchType
}

/**
 * Exits the program.
 */
fun exit() {
    println("Bye!")
    exitProcess(0)
}

/**
 * Reads Menu
 * @return Menu option
 */
fun readMenuOption(): Int {
    var option: Int
    do {
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
        option = readln().toIntOrNull() ?: -1
        if (option !in 0..2) {
            println("Incorrect option! Try again.")
        }
    } while (option !in 0..2)
    return option
}

/**
 *  Search for a query in a data
 *  @param data The data to search in
 *  @param index The index of the data
 *  @param query The query to search for
 */
fun search(data: List<String>, index: MutableMap<String, MutableSet<Int>>, query: List<String>, searchType: SearchType) {
    when (searchType) {
        ALL -> searchAll(data, index, query)
        ANY -> searchAny(data, index, query)
        NONE -> searchNone(data, index, query)
    }
}

/**
 * Search for ANY query in a data
 * @param data The data to search in
 * @param index The index of the data
 * @param query The query to search for
 */
fun searchAny(data: List<String>, index: MutableMap<String, MutableSet<Int>>, query: List<String>) {
    // Resultados
    var result = mutableSetOf<Int>()

    // Por cada palabra de la query
    for (i in query.indices) {
        var found =  mutableSetOf<Int>()
        // Si la palabra está en el index
        if (index.containsKey(query[i])) {
            // Agregar los resultados de la palabra a la lista
            found = index[query[i]]!!
        }
        // Si es la primera vez lo copiamos en resultados, si no unimos.
        // The program should print the lines containing at least one word from the query.
        // // Hago la union de las listas de values de los indices
        if (i == 0) {
            result.addAll(found)
        } else {
            result = result.union(found).toMutableSet()
        }
    }

    // Salida
    printResult(result, data)
}

/**
 * Search for ALL query in a data
 * @param data The data to search in
 * @param index The index of the data
 * @param query The query to search for
 */
fun searchAll(data: List<String>, index: MutableMap<String, MutableSet<Int>>, query: List<String>) {
    // Resultados
    var result = mutableSetOf<Int>()

    // Por cada palabra de la query
    for (i in query.indices) {
        var found =  mutableSetOf<Int>()
        // Si la palabra está en el index
        if (index.containsKey(query[i])) {
            // Agregar los resultados de la palabra a la lista
            found = index[query[i]]!!
        }
        // Si es la primera vez lo copiamos en resultados, si no intersección
        // the program should print the lines containing all the words from the query.
        // Hago la intersección de las listas de values de los indices
        if(i == 0) {
            result.addAll(found)
        } else {
            result = result.intersect(found).toMutableSet()
        }
    }

    // Salida
    printResult(result, data)
}

/**
 * Search for NONE query in a data
 * @param data The data to search in
 * @param index The index of the data
 * @param query The query to search for
 */
fun searchNone(data: List<String>, index: MutableMap<String, MutableSet<Int>>, query: List<String>) {
    // Resultados, cargo todos porque les tengo que ir quitando los encontrados
    // como es una lista de listas, le hago un faltter para que me devuleva una lista unica con el contenido de todas las listas
    var result = index.values.flatten().toMutableSet()

    // Por cada palabra de la query
    for (i in query.indices) {
        var found =  mutableSetOf<Int>()
        // Si la palabra está en el index
        if (index.containsKey(query[i])) {
            // Agregar los resultados de la palabra a la lista
            found = index[query[i]]!!
        }
        // restamos. the program should print the lines that do not contain any words from the query at all.
        // del total de indices le voy restando la lista que voy consiguiendo
        result = result.subtract(found).toMutableSet()
    }

    // Salida
    printResult(result, data)
}

/**
 * Print the result
 * @param result The result index to print
 * @param data The data to print
 */
private fun printResult(
    result: MutableSet<Int>,
    data: List<String>
) {
    if (result.isNotEmpty()) {
        println("${result.size} persons found:")
        result.forEach { println(data[it]) }
    } else {
        println("No matching people found.")
    }
}

/**
 * Read de query
 * @return The query
 */
fun readQuery(): String {
    println("Enter a name or email to search all matching people. ")
    return readln()
}

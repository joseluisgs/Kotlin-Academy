package calculator

import kotlin.system.exitProcess

fun main() {
    var line = ""
    do {
        line = readln().trim()
        if (line.isNotEmpty()) {
            when  {
                // Commands
                line == "/exit" -> exitProgram()
                line == "/help" -> helpProgram()
                "\\/".toRegex().containsMatchIn(line) -> {
                    println("Unknown command")
                }
                else -> processor(line)
            }
        }
    } while (true)
}

/**
 * Help program
 */
fun helpProgram() {
    println("This program calculates expressions of additions and subtractions")
}

/**
 * Processes the input line.
 * @param line the input line
 */
fun processor(line: String) {
    val tokens = line.split(" ").filter { it.isNotEmpty() }
    when (tokens.size) {
        2 -> {
            println("Invalid input")
            return
        }
        else -> {
        val result = mutableListOf<Int>()
        var i = 0
        while (i < tokens.size) {
            // println(tokens[i])
            if (tokens[i].isNotEmpty()) {
                // Analizamos todos los tokens
                if (tokens[i].toIntOrNull() == null) {
                    // No lo podemos convertir
                    try {
                        val signus = analyzeSignus(tokens[i])
                        result.add(signus * tokens[i + 1].toInt())
                        i += 2
                    } catch (e: Exception) {
                        println("Invalid expression")
                        return
                    }
                } else {
                    // Lo podemos convertir
                    result.add(tokens[i].toInt())
                    i++
                }
            }
        }
        // println(result)
        println(result.sum())
        }
    }
}

/**
 * Analyzes the signus.
 */
fun analyzeSignus(simbols: String): Int {
    if (simbols == "+") {
        return 1
    } else if (simbols == "-") {
        return -1
    } else {
        val veces = simbols.count { it == '-' }
        return when {
            veces == 0 -> return 1
            veces % 2 == 0 -> return 1
            else -> return -1
        }
    }
}

/**
 * Exits the program.
 */
fun exitProgram() {
    println("Bye!")
    exitProcess(0)
}


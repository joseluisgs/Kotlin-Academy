package connectfour

fun main() {
    println("Connect Four")
    println("First player's name: ")
    val player1 = readln()
    println("Second player's name: ")
    val player2 = readln()
    val (rows, columns) = analyzeDimensions()
    println("$player1 VS $player2")
    println("$rows X $columns board")
}

fun analyzeDimensions(): Pair<Int, Int> {
    var dim = Pair(0, 0)
    do {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        val dimensions = readln()
        if (dimensions.trim().isEmpty()) {
            return Pair(6, 7)
        } else {
            val split = dimensions.uppercase().split("X")
            if (split.size != 2) {
                println("Invalid input")
            } else {
                try {
                    val rows = split[0].trim().toInt()
                    val columns = split[1].trim().toInt()
                    if (rows !in (5..9))
                        println("Board rows should be from 5 to 9")
                    if (columns !in (5..9))
                        println("Board columns should be from 5 to 9")
                    if (rows in (5..9) && columns in (5..9)) {
                        dim = Pair(rows, columns)
                    }
                }   catch (e: NumberFormatException) {
                    println("Invalid input")
                }
            }
        }
    } while (dim == Pair(0, 0))
    return dim
}


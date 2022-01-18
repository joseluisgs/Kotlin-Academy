package minesweeper

// Global values
const val ROWS = 9
const val COLS = 9
const val MINE = 'X'
const val CELL = '.'
const val MARK = '*'
var MINES_LOCATION = mutableListOf<Pair<Int, Int>>()

fun main() {
    // Read number of mines
    val numMines = readNumberOfMines()
    // Create board
    val board = Minesweeper(ROWS, COLS, numMines, MINE, CELL)
    // Print board
    // board.printMineLocations()
    board.printBoard()
    board.gameLoop()
}

/**
 * Read the number of mines
 */
fun readNumberOfMines(): Int {
    print("How many mines do you want on the field? ")
    return readln().toInt()
}

/**
 * Minesweeper class
 */
class Minesweeper(val rows: Int, val cols: Int, private val totalMines: Int,
                  val mine: Char, val cell: Char) {

    private var marks = 0
    private var founds = 0

    // Board
    private val board = Array(rows) { CharArray(cols) { cell } }

    /**
     * Initialize the board
     */
    init {
        createBoard()
    }

    /**
     * Create board
     */
    fun createBoard() {
        MINES_LOCATION = generateMineLocations(rows, cols, totalMines)

        // Put the mines
        for (i in MINES_LOCATION) {
            //col-row
            board[i.second][i.first] = mine
        }

        // punt the mines, and numbers or sourrondings mines
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (board[i][j] != MINE) {
                    if (checkSurrounding(i, j) == 0)
                        board[i][j] = CELL
                    else
                        board[i][j] = checkSurrounding(i, j).toString().first()
                }
            }
        }
    }

    /**
     * Check surrounding mines
     * @param row Row
     * @param col Column
     * @return Number of surrounding mines
     */
    private fun checkSurrounding(row: Int, col: Int): Int {
        var mines = 0
        for (x in row - 1..row + 1) {
            for (y in col - 1..col + 1) {
                if (x in 0 until rows && y >= 0 && y < cols) {
                    if (board[x][y] == MINE) {
                        mines++
                    }
                }
            }
        }
        return mines
    }

    /**
     * Print board
     */
    fun printBoard() {
        println(" │123456789│")
        println("—│—————————│")
        for (i in 0 until rows) {
            print("${i + 1}│")
            for (j in 0 until cols) {
                if (board[i][j] == MINE) {
                    print(".")
                    // print(MINE)
                } else {
                    print(board[i][j])
                }
            }
            println("│")
        }
        println("—│—————————│")
    }

    /**
     * Generate mine locations
     * @param rows
     * @param cols
     * @param totalMines
     * @return mine locations
     */
    private fun generateMineLocations(rows: Int, cols: Int, totalMines: Int): MutableList<Pair<Int, Int>> {
        // Get Mines Locations
        val mineLocations = mutableListOf<Pair<Int, Int>>()

        while (mineLocations.size < totalMines) {
            val row = (0 until rows).random()
            val col = (0 until cols).random()

            if (!mineLocations.contains(col to row)) {
                mineLocations.add(col to row)
            }
        }
        return mineLocations
    }

    /**
     * Print mine locations
     */
    fun printMineLocations() {
        MINES_LOCATION.forEach {
            print("(${it.first + 1}-${it.second + 1}) ")
        }
        println()
    }

    /**
     * Game loop
     */
    fun gameLoop() {
        do {
            var undo = false
            val cord = inputCords()
            // println(cord.toString())

            // Si he llegado aquí es porque puedo marcarla/desmarcala...
            if (board[cord.second][cord.first] == MARK) {
                board[cord.second][cord.first] = CELL
                marks--
                undo = true
            } else {
                board[cord.second][cord.first] = MARK
                marks++
            }

            // Si era una mina y he marcado
            if (isMine(cord) && !undo) {
                founds++
            } else if (isMine(cord) && undo) {
                founds--
            }

            // println("Marks: $marks")
            // println("Founds: $founds")
            // println("totalMines: $totalMines")

            // Imprimo el tablero
            printBoard()
        } while (marks != founds || founds != totalMines)
        println("Congratulations! You found all the mines!")

    }

    private fun isMine(cord: Pair<Int, Int>): Boolean {
        return MINES_LOCATION.contains(cord)
    }

    private fun inputCords(): Pair<Int, Int> {
        var ok = false
        var res = Pair(-1, -1)
        do {
            print("Set/delete mines marks (x and y coordinates): ")
            try {
                val input = readln().split(" ").map { it.toInt() }
                // Col[0] and row[1]
                val value = board[input[1] - 1][input[0] - 1].toString().toIntOrNull()
                // Si lo puedo convertir es que es un numero
                if (value != null) {
                    println("There is a number here!")
                    ok = false
                } else {
                    // Si estoy aqui es libre o mina, lo devulevo
                    ok = true
                    // Devuelvo el valor
                    res = input[0] - 1 to input[1] - 1
                }
            } catch (e: Exception) {
                ok = false
            }
        } while (!ok)
        return res
    }

}

package minesweeper

// Global values
const val ROWS = 9
const val COLS = 9
const val MINE = 'X'
const val CELL = '.'

fun main() {
    // Read number of mines
    val numMines = readNumberOfMines()
    // Create board
    val board = Minesweeper(ROWS, COLS, numMines, MINE, CELL)
    // Print board
    board.printBoard()
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
class Minesweeper(val rows: Int, val cols: Int, val totalMines: Int,
                  val mine: Char, val cell: Char) {

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
        val mineLocations = generateMineLocations(rows, cols, totalMines)

        // Put the mines
        for (i in mineLocations) {
            board[i.first][i.second] = mine
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
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                print(board[i][j])
            }
            println()
        }
    }

    /**
     * Generate mine locations
     * @param rows
     * @param cols
     * @param totalMines
     * @return mine locations
     */
    private fun generateMineLocations(rows: Int, cols: Int, totalMines: Int): List<Pair<Int, Int>> {
        // Get Mines Locations
        val mineLocations = mutableListOf<Pair<Int, Int>>()

        while (mineLocations.size < totalMines) {
            val row = (0 until rows).random()
            val col = (0 until cols).random()

            if (!mineLocations.contains(row to col)) {
                mineLocations.add(row to col)
            }
        }
        return mineLocations
    }
}

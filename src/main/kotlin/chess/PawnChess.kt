package chess

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("Pawns-Only Chess")
    val player1 = readPlayer("First")
    val player2 = readPlayer("Second")
    // printl chess board
    printChessBoard()
    // play chess
    playChess(player1, player2)

}

/**
 * Make move until exit, turn player
 */
fun playChess(player1: String, player2: String) {
    var turn = player1
    while (true) {
        val move = readMove(turn)
        // println("$turn move: $move")
        turn = if (turn == player1) player2 else player1
    }
}

/**
 * Read a move from player
 */
fun readMove(turn: String): String {
    val regex = """[a-hA-H][1-8][a-hA-H][1-8]""".toRegex()
    do {
        println("$turn's turn:")
        val move = readln()
        if (move.lowercase() == "exit") {
            exitGame()
        }
        if (regex.matches(move) && move.length == 4) {
            // println("Move: $move")
            return move
        }
        println("Invalid Input")
    } while (true)
}

/**
 * Exist the game
 */
fun exitGame() {
    println("Bye!")
    exitProcess(0)
}


/**
 * Reads player information
 */
fun readPlayer(type: String): String {
    println("$type's player name:")
    return readLine()!!
}


/**
 * Print Chess Board
 */
fun printChessBoard() {
    for(i in 8 downTo 1) {
        println("  +---+---+---+---+---+---+---+---+")
        print("$i ")
       for (j in 1..8) {
           print("|")
           when (i) {
               7 -> print(" B ")
               2 -> print(" W ")
               else -> print("   ")
           }
       }
        println("|")
    }
    println("  +---+---+---+---+---+---+---+---+")
    println("    a   b   c   d   e   f   g   h")
}

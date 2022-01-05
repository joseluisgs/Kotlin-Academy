package processor

import kotlin.system.exitProcess

fun main() {
    doMenuActions()
}

/**
 * Menu and Actions
 * @param data List of data
 * @param index Index of data
 */
private fun doMenuActions() {
    do {
        val menu = readMenuOption()
        when (menu) {
            0 -> exit()
            1 -> addMatrices()
            2 -> multiplyByConstant()
            3 -> multiplyMatrices()
            4 -> transposeMatrix()
            5 -> determinantMatrix()
            6 -> inverseMatrix()
        }
    } while (menu != 0)
}

/**
 * Inverse of a Matrix
 */
fun inverseMatrix() {
    val m = readMatrix("")
    inverseMatrix(m)
}

/**
 * Calculate the inverse of a Matrix
 */
fun inverseMatrix(mat: Array<Array<Double>>) {
    /*
    If det(A) != 0
    A-1 = adj(A)/det(A)
    Else
    "Inverse doesn't exist"
    */
    val det = determinantMatrix(mat, mat.size, mat.size)
    if (det != 0.0) {
        val adj = Array(mat.size) { Array(mat.size) { 0.0 } }
        adjoint(mat, adj, mat.size)
        // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
        // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
        val inverse = Array(mat.size) { Array(mat.size) { 0.0 } }
        for (i in mat.indices)
            for (j in mat.indices)
                inverse[i][j] = adj[i][j] / det

        printMatrix(inverse)

    } else {
        println("This matrix doesn't have an inverse.")
    }

}

/**
 * Determinat of a Matrix
 */
fun determinantMatrix() {
    val m = readMatrix("")
    val determinant = determinantMatrix(m, m.size, m.size)
    println("The result is:")
    println(determinant)
}

/**
 * Function to get cofactor (sub matrix) of mat(p)(q) in temp[][]. n is current dimension of mat[][]
 * @param mat Matrix
 * @param temp Matrix
 * @param p Row
 * @param q Column
 * @param n Size of matrix
 */
fun getCofactor(
    mat: Array<Array<Double>>, temp: Array<Array<Double>>,
    p: Int, q: Int, n: Int
) {
    var i = 0
    var j = 0

    // Looping for each element of
    // the matrix
    for (row in 0 until n) {
        for (col in 0 until n) {
            // Copying into temporary matrix
            // only those element which are
            // not in given row and column
            if (row != p && col != q) {
                temp[i][j++] = mat[row][col]
                // Row is filled, so increase
                // row index and reset col
                // index
                if (j == n - 1) {
                    j = 0
                    i++
                }
            }
        }
    }
}

/**
 * Calculate Determinant of a Matrix Recursive
 * @param mat Matrix
 * @param n Size of actual Matrix
 * @param Size of Matrix Original Matrix
 * @return Determinant of Matrix
 */
fun determinantMatrix(mat: Array<Array<Double>>, n: Int, Size: Int): Double {
    var det = 0.0 // Initialize result

    // Base case : if matrix contains single
    // element
    if (n == 1) return mat[0][0]

    // To store cofactors
    val temp = Array(Size) { Array(Size) { 0.0 } }

    // To store sign multiplier
    var sign = 1

    // Iterate for each element of first row
    for (f in 0 until n) {
        // Getting Cofactor of mat[0][f]
        getCofactor(mat, temp, 0, f, n)
        det += (sign * mat[0][f] * determinantMatrix(temp, n - 1, Size))

        // terms are to be added with
        // alternate sign
        sign = -sign
    }
    return det
}

/**
 * Get adjoint of A(N)(N) in adj(N)(N).
 */
fun adjoint(mat: Array<Array<Double>>, adj: Array<Array<Double>>, Size: Int) {
    if (Size == 1) {
        adj[0][0] = 1.0
        return
    }

    // temp is used to store cofactors of A[][]
    var sign = 1
    val temp = Array(Size) { Array(Size) { 0.0 } }
    for (i in 0 until Size) {
        for (j in 0 until Size) {
            // Get cofactor of A[i][j]
            getCofactor(mat, temp, i, j, Size)

            // sign of adj[j][i] positive if sum of row
            // and column indexes is even.
            sign = if ((i + j) % 2 == 0) 1 else -1

            // Interchanging rows and columns to get the
            // transpose of the cofactor matrix
            adj[j][i] = sign * determinantMatrix(temp, Size - 1, Size)
        }
    }
}

/**
 * Menu Options Transpose Matrix
 */

fun transposeMatrix() {
    var menu = 0
    do {
        menu = readMenuTranposeOption()
        when (menu) {
            1 -> {
                transposeMain()
                menu = 0
            }
            2 -> {
                transposeSide()
                menu = 0
            }
            3 -> {
                transposeVertical()
                menu = 0
            }
            4 -> {
                transposeHorizontal()
                menu = 0
            }
        }
    } while (menu != 0)
    doMenuActions()
}

/**
 * Trasnpose Horizontal Matrix
 */
fun transposeHorizontal() {
    val m = readMatrix("")
    transposeHorizontal(m)
}

/**
 * Trasnpose Horizontal Matrix
 * @param m Matrix
 */
fun transposeHorizontal(mat: Array<Array<Double>>) {
    val mT = Array(mat[0].size) { Array(mat.size) { 0.0 } }
    val n = mat.size
    for (i in 0 until n) {
        for (j in 0 until n) {
            mT[(n - 1) - i][j] = mat[i][j]
        }
    }
    println("Transpose Vertical")
    printMatrix(mT)
}

/**
 * Transpose Vertical Matrix
 */
fun transposeVertical() {
    val m = readMatrix("")
    transposeVertical(m)
}

/**
 * Tranpose Vertical Matrix
 * @param m Matrix
 */
fun transposeVertical(mat: Array<Array<Double>>) {
    val mT = Array(mat[0].size) { Array(mat.size) { 0.0 } }
    val n = mat.size
    for (i in 0 until n) {
        for (j in 0 until n) {
            mT[i][(n - 1) - j] = mat[i][j]
        }
    }
    println("Transpose Vertical")
    printMatrix(mT)
}

/**
 * Tranpose Side Diagonal
 */
fun transposeSide() {
    val m = readMatrix("")
    transposeSide(m)
}

/**
 * Tranpose Side Diagonal
 * @param m Matrix
 */
fun transposeSide(mat: Array<Array<Double>>) {
    val mT = Array(mat[0].size) { Array(mat.size) { 0.0 } }
    val n = mat.size
    for (i in mat.indices) {
        for (j in 0 until mat[0].size) {
            mT[(n - 1) - j][(n - 1) - i] = mat[i][j]
        }
    }

    printMatrix(mT)
}


/**
 * Transpose Matrix Main Diagonal
 */
fun transposeMain() {
    val m = readMatrix("")
    transposeMain(m)

}

/**
 * Transpose Matrix Side Diagonal
 *
 */
fun transposeMain(mat: Array<Array<Double>>) {
    val mT = Array(mat[0].size) { Array(mat.size) { 0.0 } }

    for (i in mat.indices) {
        for (j in 0 until mat[0].size) {
            mT[j][i] = mat[i][j]
        }
    }

    printMatrix(mT)
}

/**
 * Reads Menu Transpose Matrix
 * @return Menu option
 */
fun readMenuTranposeOption(): Int {
    var option: Int
    do {
        println("1. Main diagonal")
        println("2. Side diagonal")
        println("3. Vertical line")
        println("4. Horizontal line")
        print("Your choice: ")
        option = readln().toIntOrNull() ?: -1
        if (option !in 1..4) {
            println("Incorrect option! Try again.")
        }
    } while (option !in 1..4)
    return option
}

/**
 * Multiply Matrices
 */
fun multiplyMatrices() {
    val m1 = readMatrix("first")
    val m2 = readMatrix("second")
    multiplayMatrices(m1, m2)
}

/**
 * Multiply Matrices
 * @param m1 First Matrix
 * @param m2 Second Matrix
 */
fun multiplayMatrices(m1: Array<Array<Double>>, m2: Array<Array<Double>>) {
    // m1. columns == m2.rows
    if (m1[0].size != m2.size) {
        println("The operation cannot be performed.")
    } else {
        //
        val m3 = Array(m1.size) { Array(m2[0].size) { 0.0 } }
        for (i in m1.indices) {
            for (j in 0 until m2[0].size) {
                for (k in m2.indices) {
                    m3[i][j] += m1[i][k] * m2[k][j]
                }
            }
        }
        // imprimimos la matriz resultado
        printMatrix(m3)
    }
}

/**
 * Multiply matrix by constant
 */
fun multiplyByConstant() {
    val m = readMatrix("")
    val c = readConstant()
    matrixByNumber(m, c)
}

/**
 * Reads the constant
 */
fun readConstant(): Double {
    var c: Double
    do {
        print("Enter constant: ")
        c = readln().toDoubleOrNull() ?: Double.MAX_VALUE
    } while (c == Double.MAX_VALUE)
    return c
}

/**
 * Add Matrices
 */
fun addMatrices() {
    val m1 = readMatrix("first")
    val m2 = readMatrix("second")
    addMatrices(m1, m2)
}

/**
 * Reads Menu
 * @return Menu option
 */
fun readMenuOption(): Int {
    var option: Int
    do {
        println("1. Add matrices")
        println("2. Multiply matrix by a constant")
        println("3. Multiply matrices")
        println("4. Transpose matrix")
        println("5. Calculate a determinant")
        println("6. Inverse matrix")
        println("0. Exit")
        print("Your choice: ")
        option = readln().toIntOrNull() ?: -1
        if (option !in 0..6) {
            println("Incorrect option! Try again.")
        }
    } while (option !in 0..6)
    return option
}

/**
 * Exits the program.
 */
fun exit() {
    exitProcess(0)
}


/**
 * Multiply matrix by number
 * @param m Matrix
 * @param c Number
 */
fun matrixByNumber(matrix: Array<Array<Double>>, c: Double) {
    val res = Array(matrix.size) { Array(matrix[0].size) { 0.0 } }
    for (i in matrix.indices) {
        for (j in 0 until matrix[i].size) {
            res[i][j] = c * matrix[i][j]
        }
    }
    printMatrix(res)
}

/**
 * Print a Matrix
 * @param m Matrix
 */
fun printMatrix(m: Array<Array<Double>>) {
    // println()
    println("The result is: ")
    for (i in m.indices) {
        for (j in m[i].indices) {
            print("${m[i][j]} ")
        }
        println()
    }
}


/**
 * Add two matrices
 * @param m1 first matrix
 * @param m2 second matrix
 */
fun addMatrices(m1: Array<Array<Double>>, m2: Array<Array<Double>>) {
    if (m1.size != m2.size || m1[0].size != m2[0].size) {
        println("The operation cannot be performed.")
    } else {
        val m3 = Array(m1.size) { Array(m1[0].size) { 0.0 } }
        for (i in m1.indices) {
            for (j in m1[i].indices) {
                m3[i][j] = m1[i][j] + m2[i][j]
            }
        }
        // imprimimos la matriz resultado
        printMatrix(m3)
    }
}

/**
 * Read a matrix from the console
 * @return a matrix
 */
fun readMatrix(type: String): Array<Array<Double>> {
    print("Enter size of $type matrix: ")
    val (f, c) = readln().split(" ").map { it.toInt() }
    val matrix = Array(f) { Array(c) { 0.0 } }
    println("Enter $type matrix: ")
    for (i in 0 until f) {
        val line = readln().split(" ").map { it.toDouble() }
        for (j in 0 until c) {
            matrix[i][j] = line[j]
        }
    }
    return matrix
}

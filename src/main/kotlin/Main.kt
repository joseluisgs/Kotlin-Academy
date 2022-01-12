import java.io.File

fun main(args: Array<String>) {
    val find = File("src/find.txt").readLines()
    val directory = File("src/directory.txt").readLines()

    val entries = find.size
    var found = 0

    println("Start searching...")
    val timeStart = System.currentTimeMillis()
    for (i in 0 until entries) {
        for (element in directory) {
            if (find[i] in element) {
                found++
            }
        }
    }
    val timeEnd = System.currentTimeMillis()
    print("Found $found / $entries entries. ")
    print("Time taken: " + String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", (timeEnd - timeStart)))

}
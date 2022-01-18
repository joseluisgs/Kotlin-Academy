import kotlin.math.sqrt

// Fix this function
fun main() {
    val a = readln().toDouble()
    val b = readln().toDouble()
    val c = readln().toDouble()
    val p = (a + b + c) / 2
    val area = sqrt(p * (p - a) * (p - b) * (p - c))
    println(area)
}

// do not change the code below

val mainThreadId = Thread.currentThread().id

class SlowStringProcessor(val s: String) : Thread() {
    @Volatile
    var numberOfUniqueCharacters: Int = 0
        private set

    override fun run() {
        val currentId = currentThread().id
        if (currentId == mainThreadId) {
            throw RuntimeException("You must start a new thread!")
        }
        try {
            sleep(2000)
        } catch (e: Exception) {
            throw RuntimeException("Do not interrupt the processor", e)
        }
        numberOfUniqueCharacters = s.split("").filter { it != "" }.toTypedArray().distinct().size
    }
}
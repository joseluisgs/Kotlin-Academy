// Fix this function
fun main() {
    val str: String = readLine()!!
    val processor = SlowStringProcessor(str)
    processor.start()
    processor.join()
    println(processor.numberOfUniqueCharacters)
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
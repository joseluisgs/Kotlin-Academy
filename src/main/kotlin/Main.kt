import kotlin.math.sqrt

// Fix this function
fun main() {
    var cad = "abracadabra"
    var onlyOnce = 0;
    for (element in cad) {
        if (cad.count { it == element } == 1) {
            onlyOnce++
        }
    }
    println(onlyOnce)

}


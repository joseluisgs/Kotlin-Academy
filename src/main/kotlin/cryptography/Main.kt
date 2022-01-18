package cryptography

fun main() {
    val crypt = Cryptographer()
    while (crypt.status != Stat.EXIT) {
        print(crypt.status.s)
        crypt.menu(readLine()!!)
    }
}
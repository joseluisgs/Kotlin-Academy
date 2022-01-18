package calculator

import java.math.BigInteger
import java.util.*

// Global vals and funcs.
private val NUMBERS = mutableMapOf<String, BigInteger>() // Lista de variables
private val STACK = Stack<BigInteger>() // Pila de operandos
private val ERROR = Error()  // Objeto de error

fun main() {
    // Leemos la entrada
    var input = readln()
    // Mietras no exit
    while (input != "/exit") {
        if (input != "") {
            // Si es una operacion
            if (input[0] != '/') {
                // Si tiene el igual es que es una variable, si no vamos a calcular
                if (input.contains('=')) memoryAdd(input) else calculate(postfixFrom(input))
            } else command(input)
        }
        // Volvemos a leer
        input = readln()
        // Limpiamos la pila y los errores
        if (STACK.isNotEmpty()) STACK.clear()
        if (ERROR.triggered()) ERROR.reset()
    }
    println("Bye!")
}

/**
 * Añade una variable de memoria
 */
private fun memoryAdd(value: String) {
    val values = value.replace(" ", "").split('=').toTypedArray()
    // Solo letras
    val sequence: CharRange = 'a'..'z'
    if (values.size > 2) ERROR.invalidAssign() else {
        for (char in values[0]) if (!sequence.contains(char.toLowerCase())) {
            ERROR.invalidID()
            return
        }
        // Vemos si es un número lo que hay detras
        when {
            isNumber(values[1]) -> NUMBERS[values[0]] = values[1].toBigInteger()
            memoryGet(values[1]) != null -> NUMBERS[values[0]] = memoryGet(values[1]) ?: 0.toBigInteger()
            else -> ERROR.invalidAssign()
        }
    }
}

/**
 * Calcula una expresión a partir de postfix
 * @param postfix La expresión en postfix
 */
private fun calculate(postfix: Array<String>) {
    if (postfix.isNotEmpty()) {
        for (element in postfix) {
            // Si es un signos, sacamos de la pila los dos ultimos valores y los operamos
            when (element) {
                "+", "-", "*", "/", "^" -> {
                    val num2 = STACK.pop()
                    val num1 = STACK.pop()
                    // Sacamos el elemento
                    when (element) {
                        "+", "-", "*" -> operation(element[0], num1, num2)
                        "/" -> divide(num1, num2)
                        "^" -> exponent(num1, num2)
                    }
                }
                else -> pushNumber(element)
            }
            if (ERROR.triggered()) return
        }
        println(STACK.last())
    }
}

/**
 * Es un camando conocido
 * @param command El comando
 */
private fun command(command: String) = if (command == "/help") help() else ERROR.unknownCMD()

/**
 * Es un numero
 * @param number El numero
 * @return Si es un numero
 */
private fun isNumber(number: String) = number.toBigIntegerOrNull() != null

/**
 * Existe en la memoria
 * @param value El valor
 * @return El valor si existe
 */
private fun memoryGet(value: String): BigInteger? = if (NUMBERS.containsKey(value)) NUMBERS[value] else null

/**
 * Operaciones
 * @param operation La operacion
 * @param num1 El primer numero
 * @param num2 El segundo numero
 */
private fun operation(op: Char, num1: BigInteger, num2: BigInteger) {
    try {
        var result = num1
        when (op) {
            '+' -> result += num2
            '*' -> result *= num2
            '-' -> result -= num2
        }
        STACK.push(result)
    } catch (e: ArithmeticException) {
        ERROR.calcTooLarge()
    }
}

/**
 * Divide
 * @param num1 El primer numero
 * @param num2 El segundo numero
 */
private fun divide(num1: BigInteger, num2: BigInteger) {
    if (num2 == 0.toBigInteger()) ERROR.zeroDiv() else STACK.push((num1 / num2))
}

/**
 * Exponente
 * @param num1 El primer numero
 * @param num2 El segundo numero
 */
private fun exponent(num1: BigInteger, num2: BigInteger) {
    when {
        num2 < 0.toBigInteger() -> ERROR.negExponent()
        num2 > Int.MAX_VALUE.toBigInteger() -> ERROR.calcTooLarge()
        else -> try {
            val result = num1.pow(num2.toInt())
            STACK.push(result)
        } catch (e: ArithmeticException) {
            ERROR.calcTooLarge()
        }
    }
}

/**
 * Ponemos el numero,s i no lo buscamos de la pila
 */
private fun pushNumber(string: String) {
    val num: BigInteger? = if (isNumber(string)) string.toBigInteger() else memoryGet(string)
    if (num == null) ERROR.unknownVar() else STACK.push(num)
}

/**
 * Muestra la ayuda
 */
private fun help() {
    println(
        "The program can add, subtract, multiply, and divide numerous very large whole numbers, save values and" +
                " supports exponentiation. Example:\na = 3\nb = 2\na + 8 * ((4 + a ^ b) * b + 1) - 6 / (b + 1)"
    )
}
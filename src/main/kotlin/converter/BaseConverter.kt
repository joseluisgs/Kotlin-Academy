package converter

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow
import kotlin.system.exitProcess

fun main() {
    // mainMenu()
    // convertAnyBase()
    convertAnyBaseDecimal()
}

/**
 * Convert to any Base Decimal Problem
 */
fun convertAnyBaseDecimal() {
    print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
    var input = readln()
    if (input == "/exit") {
        exit()
    } else {
        val sourceBase = input.split(" ")[0].toInt()
        val targetBase = input.split(" ")[1].toInt()
        converterNumberDecimal(sourceBase, targetBase)
    }
}

/**
 * Convert A Number form any Base to any Base
 * @param sourceBase
 * @param targetBase
 */
fun converterNumberDecimal(sourceBase: Int, targetBase: Int) {
    print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
    val input = readln()
    if (input != "/back") {
        if (input.indexOf(".") != -1) {
            val integerPart = input.split(".")[0]
            val decimalPart = input.split(".")[1]
            // println("integerPart: $integerPart")
            // println("decimalPart: $decimalPart")
            // Obtengo la parte entera del numero en la base de destino
            val integerValuePartBase = BigInteger(integerPart, sourceBase).toString(targetBase)
            // Obtengo la parte decimal en sistema decimal
            val decimalValuePartDecimal = getDecimalPartfromBase(decimalPart, sourceBase)
            // .setScale(5, BigDecimal.ROUND_HALF_UP).toString().substring(2)
            // println("decimalValuePartDecimal: $decimalValuePartDecimal")
            val decimalValuePartTarget = getDecimalToTarget(decimalValuePartDecimal, targetBase)
            println("Conversion result: $integerValuePartBase.$decimalValuePartTarget")
        } else {
            val value = BigInteger(input, sourceBase).toString(targetBase)
            println("Conversion result: $value")
        }
        converterNumberDecimal(sourceBase, targetBase)
    }
    convertAnyBaseDecimal()
}

/**
 * Get the decimal part from a number in any base
 * @param decimalPart the decimal part of the number
 * @param sourceBase the base of the number
 */
fun getDecimalPartfromBase(decimalPart: String, sourceBase: Int): BigDecimal {
    // println("Decimal Part: $decimalPart")
    var decimal = BigDecimal(0)
    for (i in decimalPart.indices) {
        val base = decimalPart.substring(i, i + 1)
        val digit = BigInteger(base, sourceBase).toString(10)
        val exp = -(i + 1)
        // println("Base: $base Exp: $exp")
        // println("Digit: $digit Exp: $exp")
        val tmp = digit.toBigDecimal() * (sourceBase.toDouble().pow(exp.toDouble())).toBigDecimal()
        // println("Tmp: $tmp")
        decimal += tmp
    }
    return decimal
}

/**
 * Convert to any Base from Decimal
 * @param decimalValuePartDecimal the decimal part of the number
 * @param targetBase the base of the number
 */
fun getDecimalToTarget(decimalPart: BigDecimal, targetBase: Int): String {
    val res = StringBuilder()
    var decimal = decimalPart
    // println(decimal)
    // println(targetBase)
    var veces = 0
    do {
        decimal *= targetBase.toBigDecimal()
        // println(decimal)
        val integerPart = decimal.toBigInteger()
        // println(integerPart)
        val integerTagetBase = integerPart.toString(targetBase)
        // println(integerTagetBase)
        res.append(integerTagetBase)
        decimal = decimal.subtract(integerPart.toBigDecimal())
        // println(decimal)
        veces++
    } while (decimal > BigDecimal.ZERO && veces < 5)
    // println(res.toString())
    if (res.length < 5) {
        for (i in 0 until 5 - res.length) {
            res.append("0")
        }
    }
    return res.toString()
}


/**
 * Convert to any Base Problem
 */
fun convertAnyBase() {
    print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
    var input = readln()
    if (input == "/exit") {
        exit()
    } else {
        val sourceBase = input.split(" ")[0].toInt()
        val targetBase = input.split(" ")[1].toInt()
        converterNumber(sourceBase, targetBase)
    }

}

/**
 * Convert A Number form any Base to any Base
 * @param sourceBase
 * @param targetBase
 */
fun converterNumber(sourceBase: Int, targetBase: Int) {
    print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
    val input = readln()
    if (input != "/back") {
        val number = BigInteger(input, sourceBase)
        println("Conversion result: ${number.toString(targetBase)}")
        converterNumber(sourceBase, targetBase)
    }
    convertAnyBase()
}

/**
 * Main menu of the application stage 1 and 2
 */
fun mainMenu() {
    val options = arrayOf("/from", "/to", "/exit")
    do {
        print("Do you want to convert /from decimal or /to decimal? (To quit type /exit) ")
        val option = readln().lowercase()
        when (option) {
            "/from" -> fromDecimal()
            "/to" -> toDecimal()
            "/exit" -> exit()
            else -> println("Invalid option")
        }
    } while (!options.contains(option) || option != "/exit")
}

/**
 * Convert from a number to decimal
 */
fun toDecimal() {
    print("Enter source number: ")
    val number = readln().uppercase()
    print("Enter source base: ")
    val base = readln().toInt()
    // convert a number from a base to decimal
    // val result = convertToDecimal(number, base)
    println("Conversion to decimal result: ${Integer.parseInt(number, base)}")
}

/**
 * Convert from ba base to Decimal Manual
 * @param number the number to convert
 * @param base the base of the number
 * @return the decimal number
 */
/*fun convertToDecimal(number: String, base: Int): Int {
    var result = 0
    var power = 0
    for (i in number.length - 1 downTo 0) {
        val digit = number[i].toString().toInt()
        result += digit * Math.pow(base.toDouble(), power.toDouble()).toInt()
        power++
    }
    return result
}*/

/**
 * Exit de Program
 */
fun exit() {
    exitProcess(0)
}

/**
 * Converts a Decimal to another Radix Base
 */
private fun fromDecimal() {
    print("Enter number in decimal system: ")
    val number = readln().toInt()
    print("Enter target base: ")
    val base = readln().toInt()
    // println("Conversion result: ${convertBase(number, base)}")
    println("Conversion result: ${number.toString(base)}")
}

/**
 * Converts a number from Decimal to another base Manual
 * @param number the number to convert
 * @param base the base of the number
 * @return the number in the target base
 */
/*fun convertBase(number: Int, base: Int): String {
    val digits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
    var result = ""
    var numberCopy = number
    while (numberCopy > 0) {
        result = digits[numberCopy % base] + result
        numberCopy /= base
    }
    return result
}*/

/**
 * Converts a number from Decimal to another base
 * @param number the number to convert
 * @param base the base of the number
 * @return the number in the target base
 */
/*
fun convertBase(number: Int, base: Int): String {
    return when (base) {
        2 -> Integer.toBinaryString(number)
        8 -> Integer.toOctalString(number)
        16 -> Integer.toHexString(number)
        else -> "Base $base not supported"
    }
}
*/

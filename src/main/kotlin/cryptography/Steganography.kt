package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess

const val DELIMITATOR = "000000000000000000000011"

/**
 * Main
 */
fun main() {
    do {
        println("Task (hide, show, exit):")
        val command = readLine()!!
        processCommand(command)
    } while (command != "exit")
}

/**
 * Process command menu
 * @param command
 */
fun processCommand(command: String) {

    when (command.lowercase()) {
        "exit" -> exit()
        "hide" -> hide()
        "show" -> show()
        else -> println("Wrong task: $command")
    }
}

/**
 * Obtaining the message from the image
 */
fun show() {
    println("Input image file:")
    val inputFile = readLine()!! // "out.png"
    try {
        val image = ImageIO.read(File(inputFile))
        //  BufferedImage.TYPE_INT_RGB
        //val image = BufferedImage(read.width, read.height, BufferedImage.TYPE_INT_RGB)
        val bits = bitFromImage(image)
        if (DELIMITATOR in bits) {
            val messageBits = bits.substring(0, bits.indexOf(DELIMITATOR))
            val message = messageFromBits(messageBits)
            println("Message:")
            println(message)
        } else {
            println("No message in the image")
        }

    } catch (e: Exception) {
        println("Can't read input file!")
    }
}

fun messageFromBits(messageBits: String): String {
    val message = StringBuilder()
    // println("Message: ${messageBits}")
    // println("Message: ${messageBits.length}")
    for (i in messageBits.indices step 8) {
        val byte = messageBits.substring(i, i + 8)
        val byteInt = byte.toInt(2)
        val char = byteInt.toChar()
        message.append(char)
    }
    return message.toString()
}

fun bitFromImage(image: BufferedImage): String {
    val bits : StringBuilder = StringBuilder()
    for (y in 0 until image.height) { // --> y --> height
        for (x in 0 until image.width) { // --> x width
            val color = Color(image.getRGB(x, y))
            val blue = color.blue
            //println(blue)
            val bit = blue.and(1)
            //println(bit)
            bits.append(bit)
        }
    }
    return bits.toString()
}

/**
 * Hiding the message in the image
 */
fun hide() {
    println("Input image file:")
    val inputFile = readLine()!! // "testimage.png"
    println("Output image file:")
    val outputFile = readLine()!! // "out.png"
    // println("Input Image: $inputFile")
    // println("Output Image: $outputFile")
    println("Message to hide:")
    val message = readLine()!!+"\u0000\u0000\u0003" // "pepe\u0000\u0000\u0003" //
    val bits = messageToBits(message)

    // return
    try {
        val read = ImageIO.read(File(inputFile))
        //  BufferedImage.TYPE_INT_RGB
        var image = BufferedImage(read.width, read.height, BufferedImage.TYPE_INT_RGB)
        // check if image is valid
        if (image.width * image.height < bits.size) {
            println("The input image is not large enough to hold this message.")
            return
        }
        // bufferedImage = processingImage(bufferedImage)
        image = messageToImage(image, bits)
        ImageIO.write(image, "png", File(outputFile))
        println("Message saved in $outputFile image.")
    } catch (e: Exception) {
        println("Can't read input file!")
    }

}

fun messageToBits(message: String): Array<Int> {
    val mArray = message.encodeToByteArray()
    val binary = Array(mArray.size) { "" }
    for (i in mArray.indices) {
        binary[i] = mArray[i].toString(2)
        if (binary[i].length < 8) {
            for (j in 0 until 8 - binary[i].length) {
                binary[i] = "0" + binary[i]
            }
        }
    }
    // binary.forEach { println(it) }
    // join all in a bitAray
    val bitArray = binary.joinToString("")
    // println(bitArray)
    val bits = Array(bitArray.length) { 0 }
    for (i in bitArray.indices) {
        bits[i] = bitArray[i].toString().toInt()
    }
    // println(bits.joinToString(""))
    return bits
}

fun messageToImage(image: BufferedImage, bits: Array<Int>): BufferedImage {
    // println(bits.size)
    var ins = 0
    for (y in 0 until image.height) { // --> y --> height
        for (x in 0 until image.width) { // --> x width
            if (ins < bits.size) {
                val color = Color(image.getRGB(x, y))
                // println("Color Original: ${color.blue}")
                val rgb = Color(
                    color.red,
                    color.green,
                    setLeastSignificantBitToOne(color.blue, bits[ins]),
                )
                // println("Color Cambiado: ${rgb.blue}")
                image.setRGB(x, y, rgb.rgb)
                //println("ins: ${ins+1}")
                ins++
            } else {
                //println(ins)
                return image
            }
        }
    }
    return image
}

fun setLeastSignificantBitToOne(pixel: Int, bit: Int): Int {
    // println(pixel.and(254).or(bit))
    // println(if (pixel % 2 == 0) pixel + bit else pixel)
    return pixel.and(254).or(bit)
}

/**
 * Exit
 */
fun exit() {
    println("Bye!")
    exitProcess(0)
}


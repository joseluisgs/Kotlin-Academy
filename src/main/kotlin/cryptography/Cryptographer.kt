package cryptography

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

class Cryptographer {
    var status = Stat.MENU
    var inputFileName = ""
    var outputFileName = ""
    var message = ""
    var messageFileName = ""
    var password = ""

    fun menu(input: String) {
        when (status) {
            Stat.MENU -> {
                when (input) {
                    "hide" -> status = Stat.HIDE_INPUT_IMAGE
                    "show" -> status = Stat.SHOW
                    "exit" -> {
                        println("Bye!")
                        status = Stat.EXIT
                    }
                    else -> println("Wrong task: $input")
                }
            }
            Stat.HIDE_INPUT_IMAGE -> {
                inputFileName = input
                status = Stat.HIDE_OUTPUT_IMAGE
            }
            Stat.HIDE_OUTPUT_IMAGE -> {
                outputFileName = input
                status = Stat.HIDE_MESSAGE
            }
            Stat.HIDE_MESSAGE -> {
                message = input
                status = Stat.HIDE_PASSWORD
            }
            Stat.HIDE_PASSWORD -> {
                password = input
                hideMessage()
                status = Stat.MENU
            }
            Stat.SHOW -> {
                messageFileName = input
                status = Stat.SHOW_PASSWORD
            }
            Stat.SHOW_PASSWORD -> {
                password = input
                showMessage()
                status = Stat.MENU
            }
        }
    }

    private fun showMessage() {
        val inputFile = File(messageFileName)  // Create a file instance in order to read the image file
        // Create a BufferedImage instance from the 24-bit image file data
        val image = ImageIO.read(inputFile)
        var messageBits = ""
        val reg = Regex("000000000000000000000011")
        for (y in 0 until image.height) {
            if (reg.containsMatchIn(messageBits)) {
                break // break if last bits contain stop bits
            }
            for (x in 0 until image.width) {
                var color = Color(image.getRGB(x, y)) // Read color from the (x, y) position
                var b = color.blue.toString(2).last()  // Read the Blue color value (last bit)
                messageBits += b
                if (reg.containsMatchIn(messageBits)) {
                    break // break if last bits contain stop bits
                }
            }
        }
        val mes = messageBits.substring(0, messageBits.length - 24) // message without last 3 bytes
        val list = mutableListOf<Byte>()
        // parse bits from string to Bytes in ByteArray
        for (i in mes.indices step 8) {
            try {
                list.add(mes.substring(i, i + 8).toByte(2))
            } catch (e: Exception) {}
        }
        val listArr1 = list.toByteArray()
        val listArr: ByteArray = encryptMessage(listArr1)

        // encode message
        val encMessage = listArr.toString(Charsets.UTF_8)
        println("Message:\n$encMessage")
    }

    private fun encryptMessage(listArr1: ByteArray): ByteArray {
        val arr = listArr1

        var n = 0
        val stop = "\u0000\u0000\u0003".encodeToByteArray()
        if (byteArrayOf(arr[arr.size - 3], arr[arr.size - 2], arr[arr.size - 1]).contentEquals(stop)) {
            n = 3
        }

        val a = (arr.size - n) / password.length
        val b = (arr.size - n) % password.length
        var pass1 = ""
        repeat(a) {
            pass1 += password
        }
        pass1 += password.substring(0, b)
        val pas = pass1.encodeToByteArray()

        for (i in 0 until arr.size - n) {
            arr[i] = (arr[i].toInt() xor pas[i].toInt()).toByte()
        }
        return arr

    }

    private fun hideMessage() {
        try {
            val inputFile = File(inputFileName)  // Create a file instance in order to read the image file
            // Create a BufferedImage instance from the 24-bit image file data
            val image = ImageIO.read(inputFile)
            val messageBits = messageToBitString()
            if (messageBits.length > (image.height * image.width)) {
                println("The input image is not large enough to hold this message.")
                status = Stat.MENU
                return
            }
            for (i in messageBits.indices) {
                var x = i % image.width // Calc x according char pos in message
                var y = i / image.width // Calc y according char pos in message
                var color = Color(image.getRGB(x, y)) // Read color from the (x, y) position
                var r = color.red                // Access the Red color value
                var g = color.green              // Access the Green color value
                var b = color.blue               // Access the Blue color value
                b = ((b shr 1) shl 1) or messageBits[i].digitToInt()
                color = Color(r, g, b)  // Apply Color instance with the least significant bit for each color (Red, Green, and Blue) is set to 1
                image.setRGB(x, y, color.rgb)    // Set the modified color at the (x, y) position
            }
            val outputFilePNG = File(outputFileName)  // Output the file
            ImageIO.write(image, "png", outputFilePNG)  // Create an image using the BufferedImage instance data
            println("Message saved in $outputFileName image.")
        } catch (e:Exception) {
            println("Can't read input file!")
            status = Stat.MENU
        }
    }

    private fun messageToBitString(): String {
        val parsedMessage = message + "\u0000\u0000\u0003" // add the end bytes
        var messageArray = parsedMessage.encodeToByteArray() // conv to ByteArray
        messageArray = encryptMessage(messageArray)
        // conv to string and add 0 at start of each Byte
        return messageArray.joinToString("") { it.toString(2).padStart(8, '0') }
    }

}

// conditions of program
enum class Stat(val s: String) {
    MENU("Task (hide, show, exit):\n> "),
    EXIT(""),
    HIDE_INPUT_IMAGE("Input image file:\n> "),
    HIDE_OUTPUT_IMAGE("Output image file:\n> "),
    HIDE_MESSAGE("Message to hide:\n> "),
    SHOW("Input image file:\n> "),
    HIDE_PASSWORD("Password:\n> "),
    SHOW_PASSWORD("Password:\n> ")
}
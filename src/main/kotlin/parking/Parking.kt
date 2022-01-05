package parking

import kotlin.system.exitProcess

/**
 * Car
 */
data class Car(val mat: String, val color: String)

/**
 * Parking
 */
class Parking(val size: Int) {
    var capacity: Int = 0
    val cars: Array<Car?> = Array(size) { null }
}

// Parkin Lot
var parking: Parking? = null

/**
 * Main
 */
fun main() {
    do {
        val command = readln()
        processCommand(command)
    } while (command != "exit")
}

/**
 * Process command menu
 * @param command
 */
fun processCommand(command: String) {
    val list = command.split(" ")
    val commandType = list[0]
    val otherCommand = list.drop(1)

    when (commandType) {
        "create" -> create(otherCommand[0].toInt())
        "park" -> park(otherCommand[0], otherCommand[1])
        "leave" -> leave(otherCommand[0].toInt())
        "status" -> status()
        "reg_by_color" -> regByColor(otherCommand[0])
        "spot_by_color" -> spotByColor(otherCommand[0])
        "spot_by_reg" -> spotByReg(otherCommand[0])
        "exit" -> exitProcess(0)
        else -> println("Invalid command")
    }

}

/**
 * Search the spor bay mat
 * @param mat car reg
 */
fun spotByReg(mat: String) {
    if (!checkCreated()) return

    for (i in parking!!.cars.indices) {
        if (parking!!.cars[i] != null && parking!!.cars[i]!!.mat.lowercase()== mat.lowercase()) {
            println(i+1)
            return
        }
    }
    println("No cars with registration number $mat were found.")

}

/**
 * Print spots by color
 */
fun spotByColor(color: String) {
    if (!checkCreated()) return
    val regs = parking!!.cars
        .filter { it?.color?.lowercase() == color.lowercase() }
        .map { it?.mat }
    if (regs.isNotEmpty()) {
        val spots = mutableListOf<Int>()
        for (i in parking!!.cars.indices) {
            if (regs.contains(parking!!.cars[i]?.mat)) {
                spots.add(i + 1)
            }
        }
        println(spots.joinToString(", "))
    } else {
        println("No cars with color $color were found.")
    }
}

/**
 * Print registration number of a car by color
 */
fun regByColor(color: String) {
    if (!checkCreated()) return

    val regs = parking!!.cars
        .filter { it?.color?.lowercase() == color.lowercase() }
        .map { it?.mat }
    if (regs.isNotEmpty()) {
        println(regs.joinToString(", "))
    } else {
        println("No cars with color $color were found.")
    }
}

/**
 * Print status of parking lot
 */
fun status() {
    if (!checkCreated()) return

    if (parking!!.capacity == 0) {
        println("Parking lot is empty.")
        return
    }

    for (i in 0 until parking!!.size) {
        if (parking!!.cars[i] != null) {
            println(
                "${i + 1} ${parking!!.cars[i]!!.mat} ${parking!!.cars[i]!!.color}"
            )
        }
    }
}

/**
 * Create parking lot
 * @param size
 */
fun create(size: Int) {
    parking = Parking(size)
    println("Created a parking lot with $size spots.")
}

/**
 * Leave a car from parking lot
 * @param spot number of spot
 */
fun leave(spot: Int) {
    if (!checkCreated()) return

    if (parking!!.cars[spot - 1] != null) {
        parking!!.cars[spot - 1] = null
        parking!!.capacity--
        println("Spot $spot is free.")
    } else {
        println("There is no car in spot $spot.")
    }
}

/**
 * Park a car in parking lot
 * @param mat registration Code
 * @param color color of car
 */
fun park(mat: String, color: String) {
    if (!checkCreated()) return

    val car = Car(mat, color)
    if (parking!!.capacity < parking!!.size) {
        // find first free spot
        for (i in 0..parking!!.size) {
            if (parking!!.cars[i] == null) {
                parking!!.cars[i] = car
                parking!!.capacity++
                println("${car.color} car parked in spot ${i + 1}.")
                break
            }
        }
    } else {
        println("Sorry, the parking lot is full.")
    }
}

/**
 * Check if parking lot is created
 * @return true if parking lot is created
 */
fun checkCreated(): Boolean {
    return if (parking == null) {
        println("Sorry, a parking lot has not been created.")
        false
    } else {
        true
    }
}

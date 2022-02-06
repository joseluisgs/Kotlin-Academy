package mealplanner

val mealPlanner = MealPlanner()

fun main() {
    mainMenu()
}

fun mainMenu() {
    val option = readMenuOption()

    when (option) {
        "add" -> {
            addMeal()
            mainMenu()
        }
        "show" -> {
            showMeals()
            mainMenu()
        }
        "exit" -> println("Bye!")
    }
}

private fun readMenuOption(): String {
    // Category
    lateinit var option: String
    do {
        println("What would you like to do (add, show, exit)?")
        option = readln()
    } while (option != "add" && option != "show" && option != "exit")
    return option
}

fun addMeal() {
    // Category
    val category: String = readMealCategory()
    // Name
    val name: String = readMealName()
    // Ingredients
    var ingredients = readMealIngredients()

    // Add meal
    mealPlanner.addMeal(Meal(category, name, ingredients))
    println("The meal has been added!")

}

private fun readMealIngredients(): List<String> {
    var ingredients = listOf<String>()
    do {
        var ok = true
        println("Input the ingredients:")
        ingredients = readln().split(",").map { it.trim() }
        ingredients.forEach {
            val regex = """[^a-zA-Z\s]""".toRegex()
            val matchResult = regex.find(it)
            if (matchResult != null || it.isEmpty()) {
                println("Wrong format. Use letters only!")
                ok = false
            }
        }
    } while (!ok)
    return ingredients
}

private fun readMealName(): String {
    lateinit var name: String
    do {
        var ok = true
        println("Input the meal's name:")
        name = readln().trim()
        val regex = """[^a-zA-Z\s]""".toRegex()
        val matchResult = regex.find(name)
        if (matchResult != null || name.isEmpty()) {
            println("Wrong format. Use letters only!")
            ok = false
        }
    } while (!ok)
    return name
}

private fun readMealCategory(): String {
    lateinit var category: String
    do {
        println("Which meal do you want to add (breakfast, lunch, dinner)?")
        category = readln()
        if (category != "breakfast" && category != "lunch" && category != "dinner") {
            println("Wrong meal category! Choose from: breakfast, lunch, dinner.")
        }
    } while (category != "breakfast" && category != "lunch" && category != "dinner")
    return category
}

fun showMeals() {
    if (mealPlanner.isEmpty()) {
        println("No meals saved. Add a meal first.")
    } else {
        mealPlanner.showMeals()
    }
}

class Meal(val category: String, val name: String, val ingredients: List<String>) {
    override fun toString(): String {
        return "Category: $category\nName: $name\nIngredients:\n${ingredients.joinToString("\n")}\n"
    }
}

class MealPlanner {
    private val meals = mutableListOf<Meal>()

    fun size() = meals.size

    fun isEmpty() = meals.isEmpty()

    fun addMeal(meal: Meal) {
        meals.add(meal)
    }

    fun showMeals() {
        meals.forEach { println(it) }
    }
}

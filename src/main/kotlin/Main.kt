import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

fun main() {
   var sourceBase = 21
   var targetBase = 10
   println("Base $sourceBase to Base $targetBase")
   val number = "4242.13a"
   println("Number: $number in Base $sourceBase")
   var integerPart = number.split(".")[0]
   var decimalPart = number.split(".")[1]
   // println("Integer part: $integerPart")
   // println("Decimal part: $decimalPart")

   println("Base 10")
   val integerValuePartBase = BigInteger(integerPart, sourceBase).toString(targetBase)
   val decimalValuePartBase = getDecimalPartfromBase(decimalPart, sourceBase)
      .setScale(5, BigDecimal.ROUND_HALF_UP).toString().substring(2)
   var res = "$integerValuePartBase.$decimalValuePartBase"
   println("Res Number: $res in Base $targetBase")

   // Ahora vamos a otra base y vamos a ver como funciona
   println("Base 21")
   sourceBase = 10
   targetBase = 21
   integerPart = res.split(".")[0]
   decimalPart = res.split(".")[1]
   val integerValuePartBase21 = BigInteger(integerPart, sourceBase).toString(targetBase)
   // println(integerValuePartBase21)
   val decimalValuePartBase21 = getDecimalToTarget("0.$decimalPart", sourceBase, targetBase)
   // println(decimalValuePartBase21)
   res = "$integerValuePartBase21.$decimalValuePartBase21"
   println("Res Number: $res in Base $targetBase")
}

fun getDecimalToTarget(decimalPart: String, sourceBase: Int, targetBase: Int): String {
   val res = StringBuilder()
   var decimal = BigDecimal(decimalPart)
   // println(decimal)
   // println(targetBase)
   var veces = 0
   do {
      decimal = decimal.multiply(targetBase.toBigDecimal()).setScale(5, BigDecimal.ROUND_CEILING)
      // println(decimal)
      val integerPart = decimal.toBigInteger()
      // println(integerPart)
      val integerTagetBase = integerPart.toString(targetBase)
      // println(integerTagetBase)
      res.append(integerTagetBase)
   decimal = decimal.subtract(integerPart.toBigDecimal()).setScale(5, BigDecimal.ROUND_CEILING)
      // println(decimal)
      veces++
   } while (decimal > BigDecimal.ZERO && veces < 5)
   // println(res.toString())
   return res.toString()
}


fun getDecimalPartfromBase(decimalPart: String, sourceBase: Int): BigDecimal{
   // println("Decimal Part: $decimalPart")
   var decimal = BigDecimal(0)
   for (i in decimalPart.indices){
      val base = decimalPart.substring(i, i + 1)
      val digit = BigInteger(base, sourceBase).toString(10)
      val exp = - (i + 1)
      // println("Base: $base Exp: $exp")
      // println("Digit: $digit Exp: $exp")
      val tmp = digit.toBigDecimal() * (sourceBase.toDouble().pow(exp.toDouble())).toBigDecimal()
      // println("Tmp: $tmp")
      decimal += tmp
   }
   return decimal
}

fun digitToDecimal(number: String, base: Int): Int {
   val digits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
      "U", "V", "W", "X", "Y", "Z")
   return digits.indexOf(number)
}
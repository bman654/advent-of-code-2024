import java.io.File

data class Equation(val testValue: Long, val operands: List<Long>)

val operators = listOf("+", "*")

fun processSubEquation(target: Long, subEquation: List<Long>): List<Long> {
  when (subEquation.size) {
    1 -> return subEquation
    2 -> {
      return operators.map { operator ->
        when (operator) {
          "+" -> subEquation[0] + subEquation[1]
          "*" -> subEquation[0] * subEquation[1]
          else -> 0
        }
      }
    }
    else -> {
      // process children and filter out the ones that are past our target already
      val currentNumber = subEquation.last()
      val children = processSubEquation(target, subEquation.subList(0, subEquation.size - 1)).filter { it <= target }
      return operators.flatMap { operator ->
        when (operator) {
          "+" -> children.map { currentNumber + it }
          "*" -> children.map { currentNumber * it }
          else -> listOf(0)
        }}
    }
  }
}

fun processEquation(equation: Equation): Boolean {
  return processSubEquation(equation.testValue, equation.operands).any { it == equation.testValue }
}

fun main() {
  // Read the input file
  val input = File("data/input07.txt").readLines()

  val result = input.map { line ->
    line.split("[:\\s]+".toRegex())
      .map { it -> it.toLong() }
      .let { parts -> Equation(parts[0], parts.subList(1, parts.size)) }
  }
    .filter { equation -> processEquation(equation) }
    .filter {
      println("Kept ${it.testValue}")
      true
    }
    .sumOf { it.testValue }

  println(result)
}

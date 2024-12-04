import java.io.File

interface Instruction
data class NullaryOperator(val operator: String): Instruction
data class BinaryOperator(val operator: String, val operands: Pair<Int, Int>): Instruction

fun findValidInstructions(data: String): Sequence<Instruction> {
  val regex = """(?<operator>mul|don't|do)\((?<operands>\d{1,3}(?:,\d{1,3})*)?\)""".toRegex()

  // find all matches and yield BinaryOperator for each match
  return regex.findAll(data).map {
    val operands = it.groups["operands"]?.value?.split(",") ?: listOf()
    val operator = it.groups["operator"]!!.value
    when (operands.size) {
      0 -> when (operator) {
        "do", "don't" -> NullaryOperator(operator)
        else -> null
      }
      2 -> when (operator) {
        "mul" -> BinaryOperator(operator, operands.map { it.toInt()}.zipWithNext().single())
        else -> null
      }
      else -> null
    }
  }
    .filterNotNull()
}

fun executeProgram(instructions: Sequence<Instruction>): Int {
  var accumulator = 0
  var enabled = true
  for (instruction in instructions) {
    when (instruction) {
      is NullaryOperator -> {
        when (instruction.operator) {
          "do" -> enabled = true
          "don't" -> enabled = false
        }
      }
      is BinaryOperator -> {
        if (enabled) {
          when (instruction.operator) {
            "mul" -> accumulator += instruction.operands.first * instruction.operands.second
          }
        }
      }
    }
  }
  return accumulator
}

fun main() {
  // Read the input file
  val input = File("data/input03.txt").readText()
  val program = findValidInstructions(input)
  val result = executeProgram(program)

  println("Result: $result")
}

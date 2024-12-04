import java.io.File

data class BinaryOperator(val operator: String, val operands: Pair<Int, Int>)

fun findValidInstructions(data: String): Sequence<BinaryOperator> {
  val regex = """(?<operator>mul)\((?<operand1>\d{1,3}),(?<operand2>\d{1,3})\)""".toRegex()

  // find all matches and yield BinaryOperator for each match
  return regex.findAll(data).map {
    BinaryOperator(it.groups["operator"]!!.value, Pair(it.groups["operand1"]!!.value.toInt(), it.groups["operand2"]!!.value.toInt()))
  }
}

fun executeProgram(instructions: Sequence<BinaryOperator>): Int {
  var accumulator = 0
  for (instruction in instructions) {
    when (instruction.operator) {
      "mul" -> accumulator += instruction.operands.first * instruction.operands.second
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

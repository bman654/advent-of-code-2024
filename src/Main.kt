import java.io.File

fun isSafeReport(levels: List<Int>): Boolean {
  if (levels.size < 2) return true  // A single number is trivially safe

  val increasing = levels[1] > levels[0]
  for (i in 1..<levels.size) {
    val diff = levels[i] - levels[i - 1]
    if (increasing && diff !in 1..3) return false
    if (!increasing && diff !in -3..-1) return false
    if (increasing != (levels[i] > levels[i - 1])) return false
  }
  return true
}

fun main() {
  // Read the input file
  val inputLines = File("data/input02.txt").readLines()

  // Count safe reports
  val safeCount = inputLines.count { line ->
    val levels = line.split("\\s+".toRegex()).map { it.toInt() }
    isSafeReport(levels)
  }

  // Output the number of safe reports
  println("Number of safe reports: $safeCount")
}

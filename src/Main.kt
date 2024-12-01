import java.io.File

fun main() {

  // Read the input file
  val inputLines = File("data/input01.txt").readLines()

  // Initialize two lists to hold the location IDs from the two columns
  val leftList = mutableListOf<Int>()
  val rightList = mutableListOf<Int>()

  // Parse each line to extract numbers and add them to the respective lists
  for (line in inputLines) {
    val numbers = line.split("\\s+".toRegex()).map { it.toInt() }
    leftList.add(numbers[0])
    rightList.add(numbers[1])
  }

  // Sort both lists
  leftList.sort()
  rightList.sort()

  // Calculate the total distance
  var totalDistance = 0
  for (i in leftList.indices) {
    totalDistance += Math.abs(leftList[i] - rightList[i])
  }

  // Output the total distance
  println("Total distance: $totalDistance")
}

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

  // Create a frequency map for the right list
  val rightCount = rightList.groupingBy { it }.eachCount()

  // Calculate the total similarity score
  var similarityScore = 0
  for (number in leftList) {
    val countInRight = rightCount[number] ?: 0
    similarityScore += number * countInRight
  }

  // Output the similarity score
  println("Similarity score: $similarityScore")
}

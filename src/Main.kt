import java.io.File

fun main() {
  // Read the input file
  val input = File("data/input04.txt").readLines()
  val word = "XMAS"
  val wordLength = word.length
  val directions = listOf(
    Pair(0, 1),  // right
    Pair(0, -1), // left
    Pair(1, 0),  // down
    Pair(-1, 0), // up
    Pair(1, 1),  // diagonal down-right
    Pair(1, -1), // diagonal down-left
    Pair(-1, 1), // diagonal up-right
    Pair(-1, -1) // diagonal up-left
  )

  fun isWordAtPosition(grid: List<String>, row: Int, col: Int, dRow: Int, dCol: Int): Boolean {
    for (i in 0 until wordLength) {
      val newRow = row + i * dRow
      val newCol = col + i * dCol
      if (newRow !in grid.indices || newCol !in grid[0].indices || grid[newRow][newCol] != word[i]) {
        return false
      }
    }
    return true
  }

  var count = 0
  for (row in input.indices) {
    for (col in input[row].indices) {
      for ((dRow, dCol) in directions) {
        if (isWordAtPosition(input, row, col, dRow, dCol)) {
          count++
        }
      }
    }
  }

  println("The word 'XMAS' appears $count times in the grid.")
}

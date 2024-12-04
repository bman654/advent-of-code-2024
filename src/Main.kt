import java.io.File

fun main() {
  // Read the input file
  val input = File("data/input04.txt").readLines()

  fun isXMasAtPosition(grid: List<String>, row: Int, col: Int): Boolean {
    if (row - 1 in grid.indices && row + 1 in grid.indices && col - 1 in grid.first().indices && col + 1 in grid.first().indices) {
      val w1 = "${grid[row - 1][col - 1]}${grid[row][col]}${grid[row + 1][col + 1]}"
      val w2 = "${grid[row - 1][col + 1]}${grid[row][col]}${grid[row + 1][col - 1]}"
      return (w1 == "SAM" || w1 == "MAS") && (w2 == "SAM" || w2 == "MAS")
    }
    return false
  }

  var count = 0
  for (row in input.indices) {
    for (col in input[row].indices) {
      if (isXMasAtPosition(input, row, col)) {
        count++
      }
    }
  }

  println("The pattern 'X-MAS' appears $count times in the grid.")
}

import java.io.File
import javax.swing.UIManager.put

data class Coord(val x: Int, val y: Int) {
  fun plus(other: Coord): Coord = Coord(x + other.x, y + other.y)
  fun minus(other: Coord): Coord = Coord(x - other.x, y - other.y)
}

data class Grid(val width: Int, val height: Int, val data: Array<Int>) {
  constructor(lines: List<String>) : this(
    width = lines[0].length,
    height = lines.size,
    data = lines.joinToString(separator = "").map { it.code }.toTypedArray()
  )

  fun get(x: Int, y: Int): Char = data[y * width + x].toChar()
  fun get(coord: Coord): Char = get(coord.x, coord.y)

  fun putIfEmpty(coord: Coord, c: Char) {
    if (get(coord) == '.') {
      data[coord.y * width + coord.x] = c.code
    }
  }

  fun isOutside(coord: Coord): Boolean = coord.x < 0 || coord.y < 0 || coord.x >= width || coord.y >= height

  override fun toString(): String {
    val builder = StringBuilder()
    for (y in 0..<height) {
      for (x in 0..<width) {
        builder.append(get(x, y))
      }
      if (y < height - 1) {
        builder.append("\n")   // Newline after each row, except last
      }
    }
    return builder.toString()
  }

  fun findAntennaSets(): Map<Char, List<Coord>> {
    val antennaSets = mutableMapOf<Char, MutableList<Coord>>()
    for (y in 0..<height) {
      for (x in 0..<width) {
        val char = get(x, y)
        if (char.isLetterOrDigit()) {
          antennaSets.computeIfAbsent(char) { mutableListOf() }.add(Coord(x, y))
        }
      }
    }
    return antennaSets
  }
}

fun main() {
  // Read the input file
  val input = File("data/input08-sample.txt").readLines()
  val grid = Grid(input)
  val antinodes = grid.findAntennaSets().values
    .flatMap { antennaSet ->
      antennaSet.flatMapIndexed { index, coord1 -> antennaSet.subList(index + 1, antennaSet.size)
        .flatMap { coord2 -> listOf(coord2.plus(coord2.minus(coord1)), coord1.plus(coord1.minus(coord2)))
        }
      }
        .filter { !grid.isOutside(it) }
  }
    .toSet()

  antinodes.forEach { grid.putIfEmpty(it, '#') }
  println(grid)
  println(antinodes.size)
}

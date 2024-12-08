import java.io.File
import javax.swing.UIManager.put

data class Coord(val x: Int, val y: Int) {
  fun plus(other: Coord): Coord = Coord(x + other.x, y + other.y)
  fun minus(other: Coord): Coord = Coord(x - other.x, y - other.y)

  fun slopeTo(other: Coord): Coord {
    val dy = other.y - y
    val dx = other.x - x
    val gcd = gcd(dy, dx)
    return Coord(dx / gcd, dy / gcd)
  }

  private fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    return gcd(b, a % b)
  }
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
  val input = File("data/input08.txt").readLines()
  val grid = Grid(input)
  val antinodes = grid.findAntennaSets().values
    .flatMap { antennaSet ->
      antennaSet.flatMapIndexed { index, coord1 -> antennaSet.subList(index + 1, antennaSet.size)
        .flatMap { coord2 ->
          val slope = coord1.slopeTo(coord2)
          sequence {
            var current = coord2
            do {
              yield(current)
              current = current.plus(slope)
            } while (!grid.isOutside(current))
            current = coord2.minus(slope)
            do {
              yield(current)
              current = current.minus(slope)
            } while (!grid.isOutside(current))
          }
        }
      }
        .filter { !grid.isOutside(it) }
  }
    .toSet()

  antinodes.forEach { grid.putIfEmpty(it, '#') }
  println(grid)
  println(antinodes.size)
}

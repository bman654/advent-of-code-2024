import java.io.File

data class Coord(val x: Int, val y: Int) {
  operator fun plus(other: Coord): Coord {
    return Coord(x + other.x, y + other.y)
  }

  fun rotate90(): Coord {
    return Coord(-y, x)
  }
}
data class GuardPos(val loc: Coord, val dir: Coord) {
  constructor(loc: Coord, indicator: String) :
    this(
      loc,
      Coord(
        when (indicator) {
          "^", "v" -> 0
          "<" -> -1
          else -> 1
        }, when (indicator) {
          "<", ">" -> 0
          "^" -> -1
          else -> 1
        }
      )
    )
}

data class LabMap(val lines: List<String>) {
  val W: Int get() = lines[0].length
  val H: Int get() = lines.size

  fun get(c: Coord): Char {
    return lines[c.y][c.x]
  }

  fun isOutside(c: Coord): Boolean {
    return c.x < 0 || c.x >= W || c.y < 0 || c.y >= H
  }

  fun findGuard(): GuardPos {
    lines.forEachIndexed { y, line ->
      run {
        val r = line.findAnyOf(listOf("<", ">", "v", "^"))
        if (r != null) {
          return GuardPos(Coord(r.first, y), r.second)
        }
      }
    }
    throw Exception("No guards found")
  }
}

fun tryObstaclePosition(map: LabMap, obstacle: Coord): Boolean {
  var guard = map.findGuard()
  val visited = mutableSetOf(guard)

  while (!map.isOutside(guard.loc + guard.dir)) {
    while (map.get(guard.loc + guard.dir) == '#' || obstacle == guard.loc + guard.dir) {
      guard = guard.copy(dir = guard.dir.rotate90())
    }
    guard = guard.copy(loc = guard.loc + guard.dir)
    if (visited.contains(guard)) {
      return true
    }
    visited.add(guard)
  }

  return false
}

fun main() {
  // Read the input file
  val input = File("data/input06.txt").readLines()
  val map = LabMap(input)
  var guard = map.findGuard()
  val invalidLoc = guard.loc
  val possibleLocations = mutableSetOf<Coord>()

  while (!map.isOutside(guard.loc + guard.dir)) {
    while (map.get(guard.loc + guard.dir) == '#') {
      guard = guard.copy(dir = guard.dir.rotate90())
    }
    guard = guard.copy(loc = guard.loc + guard.dir)
    if (guard.loc != invalidLoc) {
      possibleLocations.add(guard.loc)
    }
  }

  val result = possibleLocations.filter { tryObstaclePosition(map, it) }.size

  println(result)
}

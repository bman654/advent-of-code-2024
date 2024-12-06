import java.io.File

data class Rule(val a: Int, val b: Int) {
  constructor(items: List<Int>) : this(items[0], items[1])
}
data class Update private constructor(val pages: List<Int>, var pageLookup: Map<Int, Int>) {
  constructor(pages: List<Int>) : this(
    pages,
    pages.mapIndexed { index, page -> Pair(page, index)}.toMap()
  )
}

fun passesRule(update: Update, rule: Rule): Boolean {
  return (update.pageLookup[rule.a] ?: Int.MIN_VALUE) < (update.pageLookup[rule.b] ?: Int.MAX_VALUE)
}

fun main() {
  // Read the input file
  val input = File("data/input05.txt").readLines()
  val rules = input.filter { it.contains('|')}.map{ Rule(it.split('|').map{it.toInt()})}
  val updates = input.filter{ it.contains(",")}.map{ Update(it.split(',').map{it.toInt()}) }

  val result = updates.filter { rules.all { rule -> passesRule(it, rule) } }.map { it.pages[it.pages.size / 2]}.sum()
  println(result)
}

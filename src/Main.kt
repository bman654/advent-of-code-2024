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

fun fix(update: Update, rules: List<Rule>): Update {
  val validRules = rules.filter { update.pageLookup.containsKey(it.a) && update.pageLookup.containsKey(it.b) }
  // construct Map<int, Set<int>> where the key is a number and value is all the numbers that come after it directly or indirectly
  val orderLookup = mutableMapOf<Int, MutableSet<Int>>()
  for (rule in validRules) {
    if (orderLookup.containsKey(rule.a)) {
      orderLookup[rule.a]!!.add(rule.b)
    }
    else {
      orderLookup[rule.a] = mutableSetOf(rule.b)
    }
  }

  // now sort Update pages
  val orderedPages = update.pages.sortedWith { a, b ->
    if (orderLookup.containsKey(a) && orderLookup[a]!!.contains(b)) {
      return@sortedWith -1
    }
    if (orderLookup.containsKey(b) && orderLookup[b]!!.contains(a)) {
      return@sortedWith 1
    }
    return@sortedWith 0
  }
  return Update(orderedPages)
}


fun main() {
  // Read the input file
  val input = File("data/input05.txt").readLines()
  val rules = input.filter { it.contains('|')}.map{ Rule(it.split('|').map{it.toInt()})}
  val updates = input.filter{ it.contains(",")}.map{ Update(it.split(',').map{it.toInt()}) }

  val badUpdates = updates.filter { !rules.all { rule -> passesRule(it, rule) } }
  val fixed = badUpdates.map { fix(it, rules) }
  val result = fixed.sumOf { it.pages[it.pages.size / 2] }
  println(result)
}

import java.io.File
import java.util.*

data class Allocation(val blockId: Int, val size: Int)

class DiskMap(input: String) {
  private val files: Array<LinkedList<Allocation>>
  private val freeBlocks: LinkedList<Allocation>

  init {
    val numFiles = (input.length + 1) / 2
    files = Array(numFiles) { LinkedList() }
    freeBlocks = LinkedList()
    var blockId = 0
    input.forEachIndexed { index, c ->
      val size = c.digitToInt()
      if (index % 2 == 0) {
        files[index / 2].addLast(Allocation(blockId, size))
      }
      else {
        freeBlocks.addLast(Allocation(blockId, size))
      }
      blockId += size
    }
  }

  fun compact() {
    for (file in files.reversed()) {
      val f = file.removeFirst()
      val unusedFreeBlocks = LinkedList<Allocation>()
      while (freeBlocks.isNotEmpty() && f.blockId >= freeBlocks.first.blockId) {
        val freeBlock = freeBlocks.removeFirst()
        if (freeBlock.size > f.size) {
          file.addFirst(Allocation(freeBlock.blockId, f.size))
          unusedFreeBlocks.addFirst(Allocation(freeBlock.blockId + f.size, freeBlock.size - f.size))
          break
        }
        else if (freeBlock.size == f.size) {
          file.addFirst(freeBlock)
          break
        }
        else {
          unusedFreeBlocks.addFirst(freeBlock)
        }
      }

      if (file.isEmpty()) {
        file.addLast(f)
      }
      unusedFreeBlocks.forEach { freeBlocks.addFirst(it) }
    }
  }

  fun checksum(): Long {
    return files.withIndex().sumOf { (fileId, value) -> value.sumOf { allocation -> (0..<allocation.size).sumOf { (allocation.blockId + it.toLong()) * fileId } } }
  }
}

fun main() {
  // Read the input file
  val input = File("data/input09.txt").readLines()
  val diskMap = DiskMap(input[0])
  diskMap.compact()
  println("Checksum: ${diskMap.checksum()}")
}

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
    var fileId = files.size - 1
    while (!freeBlocks.isEmpty() && fileId > 0) {
      val free = freeBlocks.removeFirst()
      val file = files[fileId].removeFirst()
      if (file.blockId < free.blockId) {
        // no more free space
        files[fileId].addFirst(file)
        fileId = 0
      }
      else if (file.size < free.size) {
        files[fileId].addLast(Allocation(free.blockId, file.size))
        freeBlocks.addFirst(Allocation(free.blockId + file.size, free.size - file.size))
        --fileId
      }
      else if (file.size == free.size) {
        files[fileId].addLast(Allocation(free.blockId, file.size))
        --fileId
      }
      else {
        // free space not big enough
        files[fileId].addLast(Allocation(free.blockId, free.size))
        files[fileId].addFirst(Allocation(file.blockId, file.size - free.size))
      }
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

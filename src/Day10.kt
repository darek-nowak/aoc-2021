import java.util.ArrayDeque

/*
Learned/Refreshed:
1) skip iteration
list.forEach row@ { row ->
       row.forEach {
           if (it > 5) return@row
           println(it)
       }
    }
2)

 */
fun main() {
    val shapesMap = mapOf(
        ")" to "(",
        "]" to "[",
        "}" to "{",
        ">" to "<"
    )
    val shapesScore = mapOf(
        ")" to 3,
        "]" to 57,
        "}" to 1197,
        ">" to 25137
    )
    val shapesScore2 = mapOf(
        "(" to 1,
        "[" to 2,
        "{" to 3,
        "<" to 4
    )
    fun part1(input: List<String>): Int {
        val table = input.map { it.chunked(1) }
        val results = mutableListOf<String>()

        table.forEach { row ->
            val stack = ArrayDeque<String>()
            row.forEach { shape ->
                if (shapesMap.containsValue(shape)) {
                    stack.push(shape)
                } else if (shapesMap.containsKey(shape)) {
                    val openShape = stack.pop()
                    if (!shapesMap[shape].equals(openShape)) results.add(shape)
                }
            }
        }

        println(results)

        return results
            .sumOf { shapesScore[it] ?: 0 }
    }

    fun part2(input: List<String>): Long {
        val table = input.map { it.chunked(1) }
        val results = mutableListOf<List<String>>()
        table.forEach row@ { row ->
            val stack = ArrayDeque<String>()
            row.forEach { shape ->
                if (shapesMap.containsValue(shape)) {
                    stack.push(shape)
                } else if (shapesMap.containsKey(shape)) {
                    val openShape = stack.pop()
                    if (!shapesMap[shape].equals(openShape))
                        return@row
                }
            }
            results.add(stack.toList())
        }

        val sortedResult = results.map { row ->
            row.map { shapesScore2[it]?.toLong() ?: 0L }
                .reduce { acc, i -> 5 * acc + i }
        }.sorted()

        return sortedResult[sortedResult.size/2]
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day10_test")
    //println(part1(testInput))
    //check(part1(testInput) == 26397)

   val input = readInput("Day10_test1")
   check(part2(input) == 2776842859)
}

private class Table2<T>(private val table: List<List<T>>, private val unreachableValue: T) {
    val xSize = table[0].size - 1
    val ySize = table.size - 1

    fun pos(x: Int, y: Int): T = table[y][x]
    fun left(x: Int, y: Int): T = if (x - 1 < 0) unreachableValue else table[y][x-1]
    fun right(x: Int, y: Int): T = if (x + 1 > xSize) unreachableValue else table[y][x+1]
    fun above(x: Int, y: Int): T = if (y - 1 < 0) unreachableValue else table[y-1][x]
    fun below(x: Int, y: Int): T = if (y + 1 > ySize) unreachableValue else table[y+1][x]

    fun pos(point: Point): T = table[point.y][point.x]
    fun left(point: Point): T = if (point.x - 1 < 0) unreachableValue else table[point.y][point.x-1]
    fun right(point: Point): T = if (point.x + 1 > xSize) unreachableValue else table[point.y][point.x+1]
    fun above(point: Point): T = if (point.y - 1 < 0) unreachableValue else table[point.y-1][point.x]
    fun below(point: Point): T = if (point.y + 1 > ySize) unreachableValue else table[point.y+1][point.x]

    override fun toString() = buildString {
        table.forEach {
            append(it)
            appendLine()
        }
    }
}
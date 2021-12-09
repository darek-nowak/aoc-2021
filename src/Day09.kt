

/*
learned/refreshed :)
public fun <R> CharSequence.chunked(size: Int, transform: (CharSequence) -> R): List<R> {
public inline fun buildString(builderAction: StringBuilder.() -> Unit): String
map.reduce { acc, i -> acc * i }
 */

fun part1(input: List<String>): Int {
    val table = createTable(input)
    val result = findDepressions(table)
    println("Size: ${table.xSize + 1}x${table.ySize + 1}")

    println("Content:")
    println(table.toString())

    println("Calculate depressions")
    result.forEach {
        print("$it value=${table.pos(it.x, it.y)}, ")
    }

    return result.sumOf { table.pos(it.x, it.y) + 1 }
}

fun main() {
    fun part2(input: List<String>): Int {
        val table = createTable(input)
        val lowestPoints = findDepressions(table)
        println("lowestPoints: $lowestPoints")

        val allPaths = mutableListOf<Set<Point>>()
        lowestPoints
            .forEach { point ->
                allPaths.add(mutableSetOf<Point>().apply {
                       add(point)
                       checkSlopeAround(table, point) { add(it) }
                   }
                )
            }

        return allPaths.asSequence().map { it.size }
            .sortedDescending()
            .take(3)
            .onEach { println(it) }
            .reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day09_test1")
    check(part2(input) == 457164)
}

private fun checkSlopeAround(table: Table, point: Point, accumulate: (p:Point)->Unit) {
    if (table.left(point) - table.pos(point) == 1) {
        accumulate(point.left)
        checkSlopeAround(table, point.left, accumulate)
    }
    if (table.right(point) - table.pos(point) == 1) {
        accumulate(point.right)
        checkSlopeAround(table, point.right, accumulate)
    }
    if (table.above(point) - table.pos(point) == 1) {
        accumulate(point.above)
        checkSlopeAround(table, point.above, accumulate)
    }
    if (table.below(point) - table.pos(point) == 1) {
        accumulate(point.below)
        checkSlopeAround(table, point.below, accumulate)
    }
}

private fun createTable(input: List<String>) = Table(
    input.map {
        it.chunked(1) {
            val i = it.toString().toInt()
            if (i < 9) i else Table.UNREACHABLE_VALUE
        }
    }
)

private fun findDepressions(table: Table): List<Point> {
    val results = mutableListOf<Point>()
    for (y in 0..table.ySize) {
        for (x in 0..table.xSize) {
            if (table.pos(x, y) < table.left(x, y)
                && table.pos(x, y) < table.right(x, y)
                && table.pos(x, y) < table.above(x, y)
                && table.pos(x, y) < table.below(x, y)
            ) {
                results.add(Point(x, y))
            }
        }
    }
    return results
}

data class Point(val x: Int, val y: Int) {
    val left: Point get() = Point(x - 1, y)
    val right: Point get() = Point(x + 1, y)
    val above: Point get() = Point(x, y - 1)
    val below: Point get() = Point(x,y + 1)
}

private class Table(private val table: List<List<Int>>) {
    val xSize = table[0].size - 1
    val ySize = table.size - 1

    fun pos(x: Int, y: Int): Int = table[y][x]
    fun left(x: Int, y: Int): Int = if (x - 1 < 0) UNREACHABLE_VALUE else table[y][x-1]
    fun right(x: Int, y: Int): Int = if (x + 1 > xSize) UNREACHABLE_VALUE else table[y][x+1]
    fun above(x: Int, y: Int): Int = if (y - 1 < 0) UNREACHABLE_VALUE else table[y-1][x]
    fun below(x: Int, y: Int): Int = if (y + 1 > ySize) UNREACHABLE_VALUE else table[y+1][x]

    fun pos(point: Point): Int = table[point.y][point.x]
    fun left(point: Point): Int = if (point.x - 1 < 0) UNREACHABLE_VALUE else table[point.y][point.x-1]
    fun right(point: Point): Int = if (point.x + 1 > xSize) UNREACHABLE_VALUE else table[point.y][point.x+1]
    fun above(point: Point): Int = if (point.y - 1 < 0) UNREACHABLE_VALUE else table[point.y-1][point.x]
    fun below(point: Point): Int = if (point.y + 1 > ySize) UNREACHABLE_VALUE else table[point.y+1][point.x]

    override fun toString() = buildString {
        table.forEach {
            append(it)
            appendLine()
        }
    }

    companion object {
        const val UNREACHABLE_VALUE = 10
    }
}



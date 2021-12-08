/*
--- Day 8: Seven Segment Search ---
 */
fun main() {
    /*  1 - 2 digits
        4 - 4 digits
        7 - 3 digits
        8 - 7 digits
        0,6,9 - 6 digits
        2,3,5 - 5 digits */
    fun calculateUnique(input: List<String>): Int {
        return input
            .map { it.split("|").last() }
            .map { it.split(" ").filter { it.length in listOf(2, 3, 4, 7) } }
            .flatten()
            .count()
    }

    println(
        calculateUnique(readInput("Day08_test1"))
    )
}


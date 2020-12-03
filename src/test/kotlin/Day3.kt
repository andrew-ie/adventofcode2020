import dev.acraig.adventofcode.y2020.day3Part1
import dev.acraig.adventofcode.y2020.day3Part2
import dev.acraig.adventofcode.y2020.loadMap
import java.nio.file.Path

fun main() {
    val path = Path.of("src", "test", "resources", "day3_test.txt")
    val treeCoordinates = loadMap(path)
    println(day3Part1(treeCoordinates))
    println(day3Part2(treeCoordinates))
}
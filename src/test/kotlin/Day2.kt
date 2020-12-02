import dev.acraig.adventofcode.y2020.day2part1
import dev.acraig.adventofcode.y2020.day2part2
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

fun main() {
    val input = Files.lines(Path.of("src/test/resources/day2_test.txt")).toList()
    println(day2part1(input.asSequence()))
    println(day2part2(input.asSequence()))
}
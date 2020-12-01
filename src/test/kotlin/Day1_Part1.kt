import dev.acraig.adventofcode.y2020.day1Part1
import dev.acraig.adventofcode.y2020.day1Part2
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

fun main() {
    val input = Files.lines(Path.of("src/test/resources/day1_test.csv")).mapToLong{ it.toLong() } .boxed().collect(Collectors.toSet())
    println(day1Part1(input))
    println(day1Part2(input))
}
package dev.acraig.adventofcode.y2020

fun day25Part1(publicKeys:List<Long>) {
    val loopCount = loopGenerator(7).takeWhile { it != publicKeys[0] }.count() + 1
    val answer = loopGenerator(publicKeys[1]).take(loopCount).last()
    println(answer)
}

private fun loopGenerator(inputNumber:Long):Sequence<Long> {
    return generateSequence(1L) { current ->
        (current * inputNumber) % 20201227
    }
}

fun main() {
    day25Part1(listOf(19774466, 7290641))
}
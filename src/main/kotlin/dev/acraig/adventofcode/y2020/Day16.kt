package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class Rule(val name: String, val ranges: List<IntRange>) {
    fun valid(input: Int): Boolean {
        return ranges.any { range -> range.contains(input) }
    }
}

data class Input(val rules: Collection<Rule>, val myTicket: List<Int>, val otherTickets: Collection<List<Int>>)

fun day16Part1(input: Input) {
    val errorRate = input.otherTickets.flatten().filterNot { field ->
        input.rules.any { rule -> rule.valid(field) }
    }.sum()
    println(errorRate)
}

fun day16Part2(input: Input) {
    val validTickets = input.otherTickets.filter { ticket ->
        ticket.all { field ->
            input.rules.any { rule -> rule.valid(field) }
        }
    }
    val indexToRule = input.myTicket.indices.associateWith { index ->
        val values = validTickets.map { ticket -> ticket[index] }
        input.rules.filter { rule -> values.all { rule.valid(it) } }
    }
    val fields = generateSequence(indexToRule) {
        if (it.any { (_, value) -> value.size > 1 }) {
            lockFields(it)
        } else {
            null
        }
    }.last()
    val departureFields =
        fields.filterValues { rules -> rules[0].name.startsWith("departure") }.map { (key, _) -> key }.toSet()
    val departureValues = departureFields.map { input.myTicket[it] }
    println(departureValues.fold(1L) { acc, value ->
        acc * value
    })

}

private fun lockFields(input: Map<Int, List<Rule>>): Map<Int, List<Rule>> {
    val (locked, toConvert) = input.entries.partition { (_, rule) ->
        rule.size == 1
    }
    val definitelyNot = locked.map { (_, rule) -> rule }.flatten().toSet()
    val converted = toConvert.map { (index, rule) ->
        index to rule - definitelyNot
    }
    return (locked.map { (key, value) -> key to value } + converted).toMap()
}

private fun parseFile(lines: List<String>): Input {
    val rules = lines.takeWhile { line -> line != "" }.map {
        val name = it.substringBefore(": ")
        val rules = it.substringAfter(": ").split(" or ")
        val ranges = rules.map { rule ->
            val (first, second) = rule.split("-")
            first.toInt()..second.toInt()
        }
        Rule(name, ranges)
    }
    val myTicket = lines[lines.indexOf("your ticket:") + 1].split(",").map { it.toInt() }
    val otherTickets = lines.drop(lines.indexOf("nearby tickets:") + 1).map {
        it.split(",").map { fieldValue -> fieldValue.toInt() }
    }
    return Input(rules, myTicket, otherTickets)
}

fun main() {
    val input = parseFile(Files.readAllLines(Path.of("data/Day16.txt")))
    day16Part1(input)
    day16Part2(input)
}
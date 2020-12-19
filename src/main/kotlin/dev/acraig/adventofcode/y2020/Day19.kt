package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

private data class Day19Rule(val subRules: List<List<String>>)

fun day19Part1(input: List<String>): Int {
    val (ruledata, data) = getData(input)
    return data.filter { line -> traverse(ruledata, line).contains(line.length) }.count()
}

fun day19Part2(input: List<String>): Int {
    val (ruledata, data) = getData(input)
    val override = mapOf(
        "8" to Day19Rule(listOf(listOf("42"), listOf("42", "8"))),
        "11" to Day19Rule(listOf(listOf("42", "31"), listOf("42", "11", "31")))
    )
    val newruledata = ruledata + override
    return data.filter { line -> traverse(newruledata, line).contains(line.length) }.count()
}


private fun getRules(strings: List<String>): Map<String, Day19Rule> {
    return strings.map { row ->
        val name = row.substringBefore(':')
        if (row.contains('"')) {
            name to Day19Rule(listOf(listOf(row.substringAfter('"').take(1))))
        } else {
            name to Day19Rule(
                row.substringAfter(':').split('|').map { ruleset -> ruleset.split(' ').filterNot { it.isBlank() } })
        }
    }.toMap()
}

private fun traverse(
    rules: Map<String, Day19Rule>,
    input: String,
    startRule: Day19Rule = rules["0"]!!
): Collection<Int> {
    val result = startRule.subRules.map { subrule ->
        subrule.fold(listOf(0)) { accumulation, rule ->
            if (rule == "a" || rule == "b") {
                accumulation.filter { input.length > it && input[it] == rule[0] }.map { it + 1 }
            } else {
                accumulation.flatMap { offset ->
                    val result = traverse(rules, input.drop(offset), rules[rule]!!)
                    result.map { it + offset }
                }
            }
        }
    }
    return result.flatten().toSet()
}

private fun getData(lines: List<String>): Pair<Map<String, Day19Rule>, List<String>> {
    val split = lines.indexOf("")
    val rules = lines.subList(0, split)
    val data = lines.drop(split + 1)
    val ruledata = getRules(rules)
    return ruledata to data
}

fun main() {
//    val lines = Files.readAllLines(Path.of("src/test/resources/day19_test.txt"))
    val lines = Files.readAllLines(Path.of("data/Day19.txt"))
    println("Part1 - ${day19Part1(lines)}")
    println("Part2 - ${day19Part2(lines)}")
}
package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class BagDefinition(val name: String, val bagTypes: Map<String, Int>) {
    fun canContainShinyGold(allDefinitions: Map<String, BagDefinition>): Boolean {
        return bagTypes.containsKey("shiny gold") || bagTypes.keys.any {
            allDefinitions[it]!!.canContainShinyGold(
                allDefinitions
            )
        }
    }

    fun countNestedBags(allDefinitions: Map<String, BagDefinition>): Int {
        return bagTypes.map { (key, value) ->
            value + (allDefinitions[key]!!.countNestedBags(allDefinitions) * value)
        }.sum()
    }
}

fun day7Part1(path: Path): Int {
    val bagDefinitions = readFile(path)
    return bagDefinitions.values.map() { bag ->
        bag.canContainShinyGold(bagDefinitions)
    }.count { it }
}

fun day7Part2(path: Path): Int {
    val bagDefinitions = readFile(path)
    return bagDefinitions["shiny gold"]!!.countNestedBags(bagDefinitions)
}

private fun readFile(path: Path): Map<String, BagDefinition> {
    val rule = "(.*) bags contain (.*)".toRegex()
    val type = """(\d+) (.*?) bags?""".toRegex()
    return Files.readAllLines(path).mapNotNull {
        rule.matchEntire(it)
    }.map {
        it.groupValues[1] to type.findAll(it.groupValues[2])
            .map { match -> match.groupValues[2] to match.groupValues[1].toInt() }.toMap()
    }.map { BagDefinition(it.first, it.second) }.associateBy { it.name }
}

fun main() {
    val bagsThatCanContainShinyGold = day7Part1(Path.of("data/Day7.txt"))
    val bagsThatMustExistInsideShinyGold = day7Part2(Path.of("data/Day7.txt"))
    println("$bagsThatCanContainShinyGold bags can contain the shiny gold bag eventually")
    println("$bagsThatMustExistInsideShinyGold bags must be inside the shiny gold bag")
}
package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class IngredientList(val ingredients: Collection<String>, val allergens: Collection<String>)

fun day21(input: List<String>) {
    val type = """(.*) \(contains (.*)\)""".toRegex()
    val ingredientLists = input.mapNotNull { ingredientLine -> type.matchEntire(ingredientLine) }.map { match ->
        IngredientList(
            match.groupValues[1].split(" ").toSet(),
            match.groupValues[2].split(",").map { it.trim() }.toSet()
        )
    }
    val possibles =
        ingredientLists.flatMap { ingredientList -> ingredientList.allergens.map { allergen -> allergen to ingredientList.ingredients } }
            .groupBy({ pair -> pair.first }) { pair -> pair.second }
    val sequence = generateSequence(possibles) { current ->
        val found = current.mapValues { (_, value) ->
            value.reduce { a, b -> a intersect b }
        }.filterValues { value -> value.size == 1 }
        val locked = found.values.flatten().toSet()
        val result = current.mapValues { (key, value) ->
            if (found.containsKey(key)) {
                listOf(found[key]!!)
            } else {
                value.map { entry -> entry - locked }
            }
        }
        result
    }
    val result = sequence.first { result ->
        result.values.all {
            it.size == 1
        }
    }
    val knownAllergens = result.values.flatten().flatten().toSet()
    val nonAllergenCount = ingredientLists.map { list ->
        (list.ingredients - knownAllergens).size
    }.sum()
    println("Non Allergen Food Count: $nonAllergenCount")
    val canonicalAllergens = result.toSortedMap().map {
        it.value.flatten()[0]
    }.joinToString(",")
    println("Allergen List: $canonicalAllergens")
}

fun main() {
//    val data = Files.readAllLines(Path.of("src/test/resources/day21_test.txt"))
    val data = Files.readAllLines(Path.of("data/Day21.txt"))
    day21(data)
}
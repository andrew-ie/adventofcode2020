package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class Tile(val id: Int, val data: List<String>, val leftTile:Int? = null, val rightTile:Int? = null, val upTile:Int? = null, val downTile:Int? = null) {
    val edges by lazy {
        listOf(
            data[0], data.last(),
            data.map { line -> line.first() }.joinToString(""),
            data.map { line -> line.last() }.joinToString("")
        )
    }
    val allEdges by lazy {
        edges + edges.map { edge -> edge.reversed() }
    }

    private fun rotate(): Tile {
        val newData = data.indices.map { row ->
            data.indices.map {
                col -> data[col][row]
            }.joinToString("")
        }
        return copy(data = newData)
    }

    private fun flip(): Tile {
        val newData = data.reversed()
        return copy(data = newData)
    }

    private fun flipHorizontal(): Tile {
        val newData = data.map {
            row -> row.reversed()
        }
        return copy(data = newData)
    }

    fun left():String {
        return data.map { it.first() }.joinToString("")
    }
    fun right():String {
        return data.map { it.last() }.joinToString("")
    }

    fun top():String {
        return data.first()
    }
    fun bottom():String {
        return data.last()
    }

    fun render(): List<String> {
        return data.drop(1).dropLast(1).map { line ->
            line.drop(1).dropLast(1)
        }
    }

    fun variations(): Collection<Tile> {
        val rotations = generateSequence(this) { current ->
            current.rotate()
        }.take(4).toList()
        val horizontalFirst = rotations.flatMap { variation ->
            generateSequence(variation){ current -> current.flipHorizontal() }.take(2).toList()
        }
        val flips = (rotations + horizontalFirst).flatMap { variation ->
            generateSequence(variation) { current ->
                current.flip()
            }.take(2).toList()
        }

        val horizontalFlips = flips.flatMap { variation ->
            generateSequence(variation)  { current ->
                current.flipHorizontal()
            }.take(2).toList()
        }
        return horizontalFlips.toSet()
    }

    override fun toString(): String {
        return "$id - ${leftTile},${rightTile},${upTile},${downTile}"
    }
}

private fun readTiles(input: String): Collection<Tile> {
    val tileInput = input.trim().splitToSequence("\n\n")
    return tileInput.map { entry ->
        val lines = entry.trim().split("\n")
        val id = lines[0].substringAfter(" ").substringBefore(":").toInt()
        val data = lines.drop(1)
        Tile(id, data)
    }.toList()
}

private fun renderImage(tiles: Collection<Tile>): Tile {
    val pairs = tiles.associate { tile ->
        tile.id to
        tiles.filter { candidateTile ->
            candidateTile != tile && tile.edges.any {
                candidateTile.allEdges.contains(
                    it
                )
            }
        }.map { it.id }
    }

    val tileMap = tiles.associateBy { it.id }
    val edgeTile = Tile(-1, emptyList(), -1, -1, -1, -1)
    val updatedTiles = generateSequence(tiles.take(1).map { it.id } to tileMap) {
        (toProcess, currentState) ->
        if (toProcess.isEmpty()) {
            null
        } else {
            val tileId = toProcess.first()
            val tile = currentState[tileId]!!
            val possible = pairs[tileId]?.mapNotNull { currentState[it] }?.flatMap { candidate ->
                if (listOfNotNull(candidate.leftTile, candidate.rightTile, candidate.upTile, candidate.downTile).isNotEmpty()) {
                    listOf(candidate)
                } else {
                    candidate.variations()
                }
            } ?: emptyList()
            val left = tile.left()
            val right = tile.right()
            val top = tile.top()
            val bottom = tile.bottom()
            val toLeft = possible.find { candidate -> candidate.right() == left }?.copy(rightTile = tile.id) ?: edgeTile
            val toRight = possible.find { candidate -> candidate.left() == right }?.copy(leftTile = tile.id) ?: edgeTile
            val above = possible.find { candidate -> candidate.bottom() == top }?.copy(downTile = tile.id) ?: edgeTile
            val below = possible.find { candidate -> candidate.top() == bottom }?.copy(upTile = tile.id) ?: edgeTile
            val updatedTile = tile.copy(leftTile = toLeft.id, rightTile = toRight.id, upTile = above.id, downTile = below.id)
            val updatedState = listOfNotNull(updatedTile, toLeft, toRight, above, below).map { it.id to it }
            val newState = currentState + updatedState
            val newToProcess = toProcess.drop(1) + listOfNotNull(toLeft, toRight, above, below).toSet().filter { it.leftTile == null  || it.rightTile == null || it.upTile == null || it.downTile == null }.map { it.id }
            newToProcess to newState
        }
    }.last().second
    val search = (updatedTiles - -1).toSortedMap()
    val topTile = generateSequence(search.values.first()) { current -> current.upTile?.let { search[it] } }.last()
    val leftTile = generateSequence(topTile) { current -> current.leftTile?.let { search[it] } }.last()
    val rows = generateSequence(leftTile to generateSequence(leftTile) { current -> search[current.rightTile] }.toList()) { (firstOnPreviousRow, _) ->
        val firstOnRow = search[firstOnPreviousRow.downTile]
        firstOnRow?.to(generateSequence(firstOnRow) { current -> search[current.rightTile] }.toList())
    }.map { it.second }.toList()
    val data = rows.map { row ->
        row.map { it.render() }.reduce { acc, list ->
            acc.mapIndexed { index, currentLine ->
                "$currentLine${list[index]}"
            }
        }
    }.flatten()
    return Tile(0, data)
}

fun day20Part2(tiles: Collection<Tile>) {
    val image = renderImage(tiles)
    val imageVariations = image.variations()
    val hashCount = image.data.map { row -> row.count { it == '#' } }.sum()
    val nonSeaMonsters = imageVariations.asSequence().map {
        replaceSeaMonsters(it.data)
    }.map { data -> data.map { row -> row.count { it == '#' } }.sum()}.find { it != hashCount }
    println("Non Sea-Monster # symbols: $nonSeaMonsters")
}

fun replaceSeaMonsters(input:List<String>):List<String> {
    val seaMonster = listOf("                  (#) ",
        "(#)    (##)    (##)    (###)",
        " (#)  (#)  (#)  (#)  (#)  (#)   ").map { it.replace(' ', '.') }.map { it.toRegex() }
    return generateSequence(input) { updated ->
        val result = updated.toMutableList()
        result.indices.drop(3).forEach { index ->
            val firstRow = result[index - 2]
            val middleRow = result[index - 1]
            val bottomRow = result[index]
            val matches = generateSequence(seaMonster[2].find(bottomRow)) { match ->
                seaMonster[2].find(bottomRow, match.range.first + 1)
            }
            val applicableMatch = matches.find { match ->
                val found = seaMonster[1].matches(middleRow.substring(match.range)) && seaMonster[0].matches(firstRow.substring(match.range))
                found
            }
            if (applicableMatch != null) {
                result[index - 2] = replaceValue(seaMonster[0].find(firstRow, applicableMatch.range.first)!!, firstRow)
                result[index - 1] = replaceValue(seaMonster[1].find(middleRow, applicableMatch.range.first)!!, middleRow)
                result[index] = replaceValue(applicableMatch, bottomRow)
            }
        }
        if (result != updated) {
            result
        } else {
            null
        }
    }.last()
}
private fun replaceValue(match: MatchResult, input: String): String {
    return match.groups.drop(1).fold(input) { current, result -> result?.let { current.replaceRange(it.range, "O".repeat(it.range.last + 1 - it.range.first)) } ?: current }
}

fun day20Part1(tiles: Collection<Tile>) {
    val pairs = tiles.associateWith { tile ->
        tiles.filter { candidateTile -> candidateTile != tile && tile.edges.any { candidateTile.allEdges.contains(it) } }
    }
    val result = pairs.filterValues { it.size == 2 }.keys.map { it.id.toLong() }.reduce { a, b ->
        a * b
    }
    println("Corners = $result")
}

fun main() {
//    val data = Files.readString(Path.of("src/test/resources/day20_test.txt"))
    val data = Files.readString(Path.of("data/Day20.txt"))
    day20Part1(readTiles(data))
    day20Part2(readTiles(data))
}
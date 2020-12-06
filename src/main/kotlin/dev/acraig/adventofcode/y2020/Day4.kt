package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

private val mandatoryFields = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

private val validators = mapOf(
    "byr" to ::birthYearValidator,
    "iyr" to ::issueYearValidator,
    "eyr" to ::expirationYearValidator,
    "hgt" to ::heightValidator,
    "hcl" to ::hairColourValidator,
    "ecl" to ::eyeColourValidator,
    "pid" to ::passportValidator,
    "cid" to ::countryIdValidator
)

fun day4Part1(input: Path): Int {
    return readFile(input).filter { record -> record.keys.containsAll(mandatoryFields) }.count()
}

fun day4Part2(input: Path): Int {
    return readFile(input).filter { record -> record.keys.containsAll(mandatoryFields) }.filter { record ->
        record.all { (key, value) ->
            validators[key]?.invoke(value) ?: throw UnsupportedOperationException("Unrecognized field $key ($value)")
        }
    }.count()
}

private fun readFile(input: Path): Sequence<Map<String, String>> {
    val pattern = """(\w+?):([^\s]+)""".toRegex()
    val source = Files.readString(input)
    val recordsplit = source.splitToSequence("\n\n")
    return recordsplit.map { record ->
        pattern.findAll(record).map { field -> field.groupValues[1] to field.groupValues[2] }.toMap()
    }
}

private fun birthYearValidator(input: String): Boolean {
    return rangeValidator(input, 1920, 2002)
}

private fun issueYearValidator(input: String): Boolean {
    return rangeValidator(input, 2010, 2020)
}

private fun expirationYearValidator(input: String): Boolean {
    return rangeValidator(input, 2020, 2030)
}

private fun heightValidator(input: String): Boolean {
    val format = """(\d+)(in|cm)""".toRegex()
    val result = format.matchEntire(input)
    return when (result?.groupValues?.get(2)) {
        "in" -> {
            rangeValidator(result.groupValues[1], 59, 76)
        }
        "cm" -> {
            rangeValidator(result.groupValues[1], 150, 193)
        }
        else -> {
            false
        }
    }
}

private fun hairColourValidator(input: String): Boolean {
    val format = """#[a-f0-9]{6}""".toRegex()
    return format.matches(input)
}

private fun eyeColourValidator(input: String): Boolean {
    val validSet = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    return validSet.contains(input)
}

private fun passportValidator(input: String): Boolean {
    val format = """\d{9}""".toRegex()
    return format.matches(input)
}

private fun countryIdValidator(@Suppress("UNUSED_PARAMETER") input: String): Boolean {
    return true
}

private fun rangeValidator(input: String, start: Int, end: Int): Boolean {
    return try {
        val value = input.toInt()
        value in start..end
    } catch (e: NumberFormatException) {
        false
    }
}

fun main() {
    println(day4Part1(Path.of("data", "Day4.txt")))
    println(day4Part2(Path.of("data", "Day4.txt")))
}


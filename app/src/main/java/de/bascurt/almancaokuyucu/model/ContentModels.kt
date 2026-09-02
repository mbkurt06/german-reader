package de.bascurt.almancaokuyucu.model

import kotlinx.serialization.Serializable

@Serializable data class ContentIndex(
    val schemaVersion: Int = 1,
    val revision: Int,
    val lessons: List<LessonSummary>
)

@Serializable data class LessonSummary(
    val id: String,
    val title: String,
    val level: String,
    val file: String,
    val revision: Int
)

@Serializable data class Lesson(
    val schemaVersion: Int = 1,
    val id: String,
    val title: String,
    val level: String,
    val blocks: List<TextBlock>,
    val learningUnits: List<LearningUnit>,
    val activities: List<ActivitySpec> = emptyList()
)

@Serializable data class TextBlock(val id: String, val text: String)

@Serializable data class TextRange(
    val blockId: String,
    val start: Int,
    val endExclusive: Int,
    val role: String? = null
)

@Serializable data class LearningUnit(
    val id: String,
    val base: String,
    val type: String,
    val meaningTr: String,
    val grammar: String? = null,
    val noteTr: String? = null,
    val ranges: List<TextRange>,
    val examples: List<Example> = emptyList(),
    val tags: List<String> = emptyList()
)

@Serializable data class Example(val de: String, val tr: String)

@Serializable data class ActivitySpec(
    val id: String,
    val type: String,
    val unitIds: List<String> = emptyList(),
    val config: Map<String, String> = emptyMap()
)

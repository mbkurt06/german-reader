package de.bascurt.almancaokuyucu.feature.practice

import de.bascurt.almancaokuyucu.model.ActivitySpec
import de.bascurt.almancaokuyucu.model.Lesson

/** Yeni test/tekrar biçimleri bu sözleşmeyi uygulayarak eklenir. */
interface ActivityEngine {
    val type: String
    fun supports(spec: ActivitySpec): Boolean = spec.type == type
    fun build(spec: ActivitySpec, lesson: Lesson): PracticeSession
}

data class PracticeSession(val title: String, val questions: List<Question>)
data class Question(val prompt: String, val answer: String, val choices: List<String> = emptyList())

class ActivityRegistry(engines: List<ActivityEngine>) {
    private val byType = engines.associateBy { it.type }
    fun build(spec: ActivitySpec, lesson: Lesson) = byType[spec.type]?.build(spec, lesson)
}

class GapFillEngine : ActivityEngine {
    override val type = "gap_fill"
    override fun build(spec: ActivitySpec, lesson: Lesson): PracticeSession {
        val units = lesson.learningUnits.filter { it.id in spec.unitIds || spec.unitIds.isEmpty() }
        return PracticeSession("Boşluk doldurma", units.map {
            Question("Türkçesi “${it.meaningTr}” olan Almanca yapı nedir?", it.base)
        })
    }
}

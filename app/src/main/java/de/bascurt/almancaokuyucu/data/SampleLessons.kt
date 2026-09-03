package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

object SampleLessons {
    val all: List<ReaderLesson> by lazy {
        FocusedLessons.all.filter { it.id == "a2-neuer-anfang" }
    }
}

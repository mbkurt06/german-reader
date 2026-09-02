package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

object SampleLessons {
    val all: List<ReaderLesson> by lazy {
        ExtendedLessonsPart0.all +
            ExtendedLessonsPart1.all +
            ExtendedLessonsPart2.all +
            ExtendedLessonsPart3.all +
            ExtendedLessonsPart4.all
    }
}

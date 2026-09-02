package de.bascurt.almancaokuyucu.feature.reader

import de.bascurt.almancaokuyucu.model.LearningUnit

object ReaderLogic {
    fun unitAt(units: List<LearningUnit>, blockId: String, offset: Int): LearningUnit? =
        units.firstOrNull { unit ->
            unit.ranges.any { it.blockId == blockId && offset >= it.start && offset < it.endExclusive }
        }
}

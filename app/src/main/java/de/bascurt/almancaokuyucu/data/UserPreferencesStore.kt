package de.bascurt.almancaokuyucu.data

import android.content.Context

data class UserPreferences(
    val name: String = "Kullanıcı",
    val germanLevel: String = "A2",
    val dailyGoal: Int = 10,
    val themeMode: String = "system",
    val storyTextSize: Int = 22,
    val uiScale: Float = 1f,
    val highlightEnabled: Boolean = true,
    val detailedExplanations: Boolean = true,
    val quizQuestionCount: Int = 10
)

data class LearningStats(
    val answered: Int = 0,
    val correct: Int = 0,
    val studySessions: Int = 0
)

class UserPreferencesStore(context: Context) {
    private val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun load(): UserPreferences = UserPreferences(
        name = prefs.getString("name", "Kullanıcı") ?: "Kullanıcı",
        germanLevel = prefs.getString("german_level", "A2") ?: "A2",
        dailyGoal = prefs.getInt("daily_goal", 10),
        themeMode = prefs.getString("theme_mode", "system") ?: "system",
        storyTextSize = prefs.getInt("story_text_size", 22),
        uiScale = prefs.getFloat("ui_scale", 1f),
        highlightEnabled = prefs.getBoolean("highlight_enabled", true),
        detailedExplanations = prefs.getBoolean("detailed_explanations", true),
        quizQuestionCount = prefs.getInt("quiz_question_count", 10)
    )

    fun save(value: UserPreferences) {
        prefs.edit()
            .putString("name", value.name)
            .putString("german_level", value.germanLevel)
            .putInt("daily_goal", value.dailyGoal)
            .putString("theme_mode", value.themeMode)
            .putInt("story_text_size", value.storyTextSize)
            .putFloat("ui_scale", value.uiScale)
            .putBoolean("highlight_enabled", value.highlightEnabled)
            .putBoolean("detailed_explanations", value.detailedExplanations)
            .putInt("quiz_question_count", value.quizQuestionCount)
            .apply()
    }

    fun readLessonIds(): Set<String> = prefs.getStringSet("read_lessons", emptySet())?.toSet() ?: emptySet()

    fun markLessonRead(id: String) {
        val updated = readLessonIds() + id
        prefs.edit().putStringSet("read_lessons", updated).apply()
    }

    fun loadStats(): LearningStats = LearningStats(
        answered = prefs.getInt("answered", 0),
        correct = prefs.getInt("correct", 0),
        studySessions = prefs.getInt("study_sessions", 0)
    )

    fun recordAnswer(correct: Boolean) {
        val stats = loadStats()
        prefs.edit()
            .putInt("answered", stats.answered + 1)
            .putInt("correct", stats.correct + if (correct) 1 else 0)
            .apply()
    }

    fun recordStudySession() {
        val stats = loadStats()
        prefs.edit().putInt("study_sessions", stats.studySessions + 1).apply()
    }

    fun resetProgress() {
        prefs.edit()
            .remove("read_lessons")
            .putInt("answered", 0)
            .putInt("correct", 0)
            .putInt("study_sessions", 0)
            .apply()
    }
}

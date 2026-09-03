package de.bascurt.almancaokuyucu.data

import android.content.Context
import de.bascurt.almancaokuyucu.model.Lexeme

data class UserPreferences(
    val name: String = "Zeynep",
    val username: String = "zeynep",
    val bio: String = "",
    val profilePhotoUri: String = "",
    val learningReason: String = "Günlük Almanca",
    val germanLevel: String = "A2",
    val dailyGoal: Int = 10,
    val themeMode: String = "system",
    val storyTextSize: Int = 22,
    val readerBrightness: Float = 1f,
    val readerThemeMode: String = "app",
    val readerNightMode: Boolean = false,
    val uiScale: Float = 1f,
    val highlightEnabled: Boolean = true,
    val detailedExplanations: Boolean = true,
    val quizQuestionCount: Int = 10,
    val appLanguage: String = "tr",
    val translationLanguage: String = "tr"
)

data class LearningStats(
    val answered: Int = 0,
    val correct: Int = 0,
    val studySessions: Int = 0
)

data class WordStudyProgress(
    val correct: Int = 0,
    val wrong: Int = 0,
    val streak: Int = 0,
    val lastSeen: Long = 0L
)

class UserPreferencesStore(context: Context) {
    private val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun load(): UserPreferences {
        val legacyNightMode = prefs.getBoolean("reader_night_mode", false)
        val savedReaderTheme = prefs.getString("reader_theme_mode", null)
        return UserPreferences(
            name = prefs.getString("name", "Zeynep") ?: "Zeynep",
            username = prefs.getString("username", "zeynep") ?: "zeynep",
            bio = prefs.getString("bio", "") ?: "",
            profilePhotoUri = prefs.getString("profile_photo_uri", "") ?: "",
            learningReason = prefs.getString("learning_reason", "Günlük Almanca") ?: "Günlük Almanca",
            germanLevel = prefs.getString("german_level", "A2") ?: "A2",
            dailyGoal = prefs.getInt("daily_goal", 10),
            themeMode = prefs.getString("theme_mode", "system") ?: "system",
            storyTextSize = prefs.getInt("story_text_size", 22),
            readerBrightness = prefs.getFloat("reader_brightness", 1f),
            readerThemeMode = savedReaderTheme ?: if (legacyNightMode) "dark" else "app",
            readerNightMode = legacyNightMode,
            uiScale = prefs.getFloat("ui_scale", 1f),
            highlightEnabled = prefs.getBoolean("highlight_enabled", true),
            detailedExplanations = prefs.getBoolean("detailed_explanations", true),
            quizQuestionCount = prefs.getInt("quiz_question_count", 10),
            appLanguage = prefs.getString("app_language", "tr") ?: "tr",
            translationLanguage = prefs.getString("translation_language", "tr") ?: "tr"
        )
    }

    fun save(value: UserPreferences) {
        prefs.edit()
            .putString("name", value.name)
            .putString("username", value.username)
            .putString("bio", value.bio)
            .putString("profile_photo_uri", value.profilePhotoUri)
            .putString("learning_reason", value.learningReason)
            .putString("german_level", value.germanLevel)
            .putInt("daily_goal", value.dailyGoal)
            .putString("theme_mode", value.themeMode)
            .putInt("story_text_size", value.storyTextSize)
            .putFloat("reader_brightness", value.readerBrightness)
            .putString("reader_theme_mode", value.readerThemeMode)
            .putBoolean("reader_night_mode", value.readerThemeMode == "dark")
            .putFloat("ui_scale", value.uiScale)
            .putBoolean("highlight_enabled", value.highlightEnabled)
            .putBoolean("detailed_explanations", value.detailedExplanations)
            .putInt("quiz_question_count", value.quizQuestionCount)
            .putString("app_language", value.appLanguage)
            .putString("translation_language", value.translationLanguage)
            .apply()
    }

    fun readLessonIds(): Set<String> = prefs.getStringSet("read_lessons", emptySet())?.toSet() ?: emptySet()

    fun markLessonRead(id: String) {
        val updated = readLessonIds() + id
        prefs.edit().putStringSet("read_lessons", updated).apply()
    }

    fun toggleLessonRead(id: String) {
        val current = readLessonIds()
        val updated = if (id in current) current - id else current + id
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

    fun wordProgress(id: String): WordStudyProgress = WordStudyProgress(
        correct = prefs.getInt("word_${id}_correct", 0),
        wrong = prefs.getInt("word_${id}_wrong", 0),
        streak = prefs.getInt("word_${id}_streak", 0),
        lastSeen = prefs.getLong("word_${id}_last_seen", 0L)
    )

    fun recordWordAnswer(id: String, correct: Boolean) {
        val current = wordProgress(id)
        prefs.edit()
            .putInt("word_${id}_correct", current.correct + if (correct) 1 else 0)
            .putInt("word_${id}_wrong", current.wrong + if (correct) 0 else 1)
            .putInt("word_${id}_streak", if (correct) current.streak + 1 else 0)
            .putLong("word_${id}_last_seen", System.currentTimeMillis())
            .apply()
    }

    fun selectStudyItems(candidates: List<Lexeme>, limit: Int = 10): List<Lexeme> {
        val unique = candidates.distinctBy { it.id }
        val now = System.currentTimeMillis()
        return unique
            .map { item ->
                val progress = wordProgress(item.id)
                val attempts = progress.correct + progress.wrong
                val unseenBonus = if (attempts == 0) 1000.0 else 0.0
                val errorBonus = progress.wrong * 40.0
                val accuracyPenalty = if (attempts == 0) 0.0 else (progress.correct.toDouble() / attempts) * 20.0
                val streakPenalty = progress.streak * 12.0
                val ageHours = if (progress.lastSeen == 0L) 72.0 else ((now - progress.lastSeen).coerceAtLeast(0L) / 3_600_000.0).coerceAtMost(72.0)
                item to (unseenBonus + errorBonus + ageHours - accuracyPenalty - streakPenalty)
            }
            .sortedByDescending { it.second }
            .take(limit.coerceAtLeast(1))
            .map { it.first }
    }

    fun resetProgress() {
        prefs.edit()
            .remove("read_lessons")
            .putInt("answered", 0)
            .putInt("correct", 0)
            .putInt("study_sessions", 0)
            .apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}

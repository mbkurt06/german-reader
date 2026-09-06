package de.bascurt.almancaokuyucu

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

internal sealed interface SpeechEvent {
    val utteranceId: String
    data class Started(override val utteranceId: String) : SpeechEvent
    data class Finished(override val utteranceId: String) : SpeechEvent
    data class Failed(override val utteranceId: String) : SpeechEvent
}

internal class GermanSpeechController(context: Context) : TextToSpeech.OnInitListener {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var engine: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var ready = false
    var onEvent: ((SpeechEvent) -> Unit)? = null

    override fun onInit(status: Int) {
        val tts = engine ?: return
        val languageStatus = if (status == TextToSpeech.SUCCESS) tts.setLanguage(Locale.GERMANY) else TextToSpeech.ERROR
        ready = status == TextToSpeech.SUCCESS &&
            languageStatus != TextToSpeech.LANG_MISSING_DATA &&
            languageStatus != TextToSpeech.LANG_NOT_SUPPORTED
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) = dispatch(SpeechEvent.Started(utteranceId))
            override fun onDone(utteranceId: String) = dispatch(SpeechEvent.Finished(utteranceId))
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String) = dispatch(SpeechEvent.Failed(utteranceId))
        })
    }

    fun speak(text: String, utteranceId: String, flush: Boolean = true): Boolean {
        if (!ready || text.isBlank()) return false
        engine?.setSpeechRate(.9f)
        return engine?.speak(
            text,
            if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD,
            Bundle(),
            utteranceId
        ) == TextToSpeech.SUCCESS
    }

    fun stop() { engine?.stop() }

    fun shutdown() {
        engine?.stop()
        engine?.shutdown()
        engine = null
        ready = false
        onEvent = null
    }

    private fun dispatch(event: SpeechEvent) {
        mainHandler.post { onEvent?.invoke(event) }
    }
}

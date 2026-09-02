package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.BuildConfig
import de.bascurt.almancaokuyucu.model.ContentIndex
import de.bascurt.almancaokuyucu.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

interface ContentRepository {
    suspend fun index(): ContentIndex
    suspend fun lesson(path: String): Lesson
}

class GitHubContentRepository(
    private val client: OkHttpClient = OkHttpClient(),
    private val json: Json = Json { ignoreUnknownKeys = true }
) : ContentRepository {
    override suspend fun index() = get("index.json", ContentIndex.serializer())
    override suspend fun lesson(path: String) = get(path, Lesson.serializer())

    private suspend fun <T> get(path: String, serializer: kotlinx.serialization.KSerializer<T>): T =
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(BuildConfig.CONTENT_BASE_URL + path).build()
            client.newCall(request).execute().use { response ->
                check(response.isSuccessful) { "İçerik indirilemedi: ${response.code}" }
                json.decodeFromString(serializer, checkNotNull(response.body).string())
            }
        }
}

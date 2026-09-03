package de.bascurt.almancaokuyucu

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider

internal data class AppAccount(
    val uid: String,
    val displayName: String?,
    val email: String?
)

internal class FirebaseAuthGateway(private val activity: Activity) {
    private fun authOrNull(): FirebaseAuth? =
        if (FirebaseApp.getApps(activity).isNotEmpty()) FirebaseAuth.getInstance() else null

    val isConfigured: Boolean
        get() = authOrNull() != null

    fun currentAccount(): AppAccount? = authOrNull()?.currentUser?.let {
        AppAccount(it.uid, it.displayName, it.email)
    }

    fun registerWithEmail(email: String, password: String, onResult: (Result<AppAccount>) -> Unit) {
        val auth = authOrNull() ?: return onResult(Result.failure(configurationError()))
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) onResult(Result.failure(IllegalStateException("Kullanıcı oluşturulamadı.")))
                else onResult(Result.success(AppAccount(user.uid, user.displayName, user.email)))
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun signInWithEmail(email: String, password: String, onResult: (Result<AppAccount>) -> Unit) {
        val auth = authOrNull() ?: return onResult(Result.failure(configurationError()))
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) onResult(Result.failure(IllegalStateException("Oturum açılamadı.")))
                else onResult(Result.success(AppAccount(user.uid, user.displayName, user.email)))
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    suspend fun signInWithGoogle(onResult: (Result<AppAccount>) -> Unit) {
        val auth = authOrNull() ?: return onResult(Result.failure(configurationError()))
        val clientIdRes = activity.resources.getIdentifier("default_web_client_id", "string", activity.packageName)
        if (clientIdRes == 0) {
            onResult(Result.failure(IllegalStateException("Google OAuth istemci kimliği bulunamadı. Firebase google-services.json yapılandırmasını ekleyin.")))
            return
        }

        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(activity.getString(clientIdRes))
                .setAutoSelectEnabled(false)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            val credential = CredentialManager.create(activity)
                .getCredential(context = activity, request = request)
                .credential

            if (credential !is CustomCredential || credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                onResult(Result.failure(IllegalStateException("Google hesabı bilgisi alınamadı.")))
                return
            }

            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user == null) onResult(Result.failure(IllegalStateException("Google ile oturum açılamadı.")))
                    else onResult(Result.success(AppAccount(user.uid, user.displayName, user.email)))
                }
                .addOnFailureListener { onResult(Result.failure(it)) }
        } catch (error: Exception) {
            onResult(Result.failure(error))
        }
    }

    fun signInWithApple(onResult: (Result<AppAccount>) -> Unit) {
        val auth = authOrNull() ?: return onResult(Result.failure(configurationError()))
        val provider = OAuthProvider.newBuilder("apple.com").apply {
            scopes = listOf("email", "name")
        }

        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user == null) onResult(Result.failure(IllegalStateException("Apple ile oturum açılamadı.")))
                    else onResult(Result.success(AppAccount(user.uid, user.displayName, user.email)))
                }
                .addOnFailureListener { onResult(Result.failure(it)) }
            return
        }

        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) onResult(Result.failure(IllegalStateException("Apple ile oturum açılamadı.")))
                else onResult(Result.success(AppAccount(user.uid, user.displayName, user.email)))
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun signOut() {
        authOrNull()?.signOut()
    }

    private fun configurationError() = IllegalStateException(
        "Firebase henüz yapılandırılmadı. app/google-services.json dosyasını ekleyip Firebase Authentication sağlayıcılarını etkinleştirin."
    )
}

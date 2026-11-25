package fr.pm1g1.arboris.ui.View.EventForm

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import fr.pm1g1.arboris.core.storage.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun downloadDocumentWithManager(context: Context, fullUrl: String, fileName: String) {
    try {
        val tokenManager = TokenManager(context)
        val tokens = tokenManager.getTokens()
        
        val request = DownloadManager.Request(Uri.parse(fullUrl)).apply {
            setTitle(fileName)
            setDescription("Téléchargement du document...")
    
            tokens?.accessToken?.let { token ->
                addRequestHeader("Authorization", "Bearer $token")
            }
            
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(context, "Téléchargement démarré", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Erreur lors du téléchargement", Toast.LENGTH_SHORT).show()
    }
}

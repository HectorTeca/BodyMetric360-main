import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ApiRequestTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg urls: String): String {
        val url = URL(urls[0])
        val urlConnection = url.openConnection() as HttpURLConnection
        val responseBody = StringBuilder()

        try {
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line = reader.readLine()
            while (line != null) {
                responseBody.append(line)
                line = reader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection.disconnect()
        }

        return responseBody.toString()
    }
}
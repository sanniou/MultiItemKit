package com.sanniou.multiitemsample.utils;

import android.annotation.TargetApi
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.HashMap

class HttpUtil {
    companion object {
        private const val TAG = "HttpUtil"
        private const val CONNECT_TIME_OUT = 10000
        private const val READ_TIME_OUT = 15000
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63"
        private const val ENCODE: String = "utf-8"

        @TargetApi(Build.VERSION_CODES.KITKAT)
        fun post(requestUrl: String?, header: HashMap<String, String>?, params: String?): String {
            var result: String
            try {
                val url = URL(requestUrl)
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                conn.connectTimeout =
                    CONNECT_TIME_OUT
                conn.readTimeout =
                    READ_TIME_OUT
                conn.doInput = true
                conn.doOutput = true

                //add reuqest header
                conn.requestMethod = "POST"
                conn.setRequestProperty(
                    "User-Agent",
                    USER_AGENT
                )
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
                conn.setRequestProperty(
                    "Accept-Charset",
                    ENCODE
                )
                conn.setRequestProperty("Content-type", "application/json")

                if (header != null) {
                    for ((k, v) in header) {
                        conn.setRequestProperty(k, v)
                    }
                }

                val dos = DataOutputStream(conn.outputStream)

                params?.toByteArray(StandardCharsets.UTF_8)?.run {
                    dos.write(this)
                }
                dos.flush()
                dos.close()

                val responseCode: Int = conn.responseCode

                Log.e(TAG, "post-responseUrl = $requestUrl")
                Log.e(TAG, "post-responseCode = $responseCode")

                val inputStream: InputStream = conn.inputStream
                val bos = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var size = inputStream.read(buffer)
                while (size != -1) {
                    bos.write(buffer, 0, size)
                    size = inputStream.read(buffer)
                }
                result = String(bos.toByteArray(), Charset.forName(ENCODE))
                inputStream.close()
                bos.close()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: ProtocolException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                result = e.message.toString()
            }
            Log.e(TAG, "post-responseData = $result")
            return result
        }

        @Throws(SocketTimeoutException::class)
        fun get(requestUrl: String?): String {
            var result: String
            try {
                val url = URL(requestUrl)
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                conn.connectTimeout =
                    CONNECT_TIME_OUT
                conn.readTimeout =
                    READ_TIME_OUT
                // optional default is GET
                conn.requestMethod = "GET"

                //add request header
                conn.setRequestProperty(
                    "User-Agent",
                    USER_AGENT
                )

                val responseCode = conn.responseCode

                Log.e(TAG, "get-responseUrl = $requestUrl")
                Log.e(TAG, "get-responseCode = $responseCode")

                val inputStream: InputStream = conn.inputStream
                val bos = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var size = inputStream.read(buffer)
                while (size != -1) {
                    bos.write(buffer, 0, size)
                    size = inputStream.read(buffer)
                }
                result = String(bos.toByteArray(), Charset.forName(ENCODE))
                inputStream.close()
                bos.close()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: ProtocolException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                result = e.message.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                result = e.message.toString()
            }
            Log.e(TAG, "get-responseData = $result")
            return result
        }
    }
}

suspend fun String.toRequestGet() = withContext(Dispatchers.IO) {
    HttpUtil.get(this@toRequestGet)
}
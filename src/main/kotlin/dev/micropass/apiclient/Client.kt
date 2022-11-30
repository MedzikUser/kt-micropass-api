package dev.micropass.apiclient

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Client(private val accessToken: String?) {
    private val apiURL = "https://micropass-api.medzik.xyz"

    private val client = HttpClient.newBuilder().build()

    @Throws(Exception::class)
    fun get(url: String): String {
        return send("GET", url, HttpRequest.BodyPublishers.noBody())
    }

    @Throws(Exception::class)
    fun post(url: String, body: String): String {
        return send("POST", url, HttpRequest.BodyPublishers.ofString(body))
    }

    @Throws(Exception::class)
    fun post(url: String, body: HashMap<*, *>): String {
        return post(url, Gson().toJson(body))
    }

    @Throws(Exception::class)
    fun patch(url: String, body: String): String {
        return send("PATCH", url, HttpRequest.BodyPublishers.ofString(body))
    }

    @Throws(Exception::class)
    fun patch(url: String, body: HashMap<*, *>): String {
        return patch(url, Gson().toJson(body))
    }

    @Throws(Exception::class)
    fun delete(url: String): String {
        return send("DELETE", url, HttpRequest.BodyPublishers.noBody())
    }

    @Throws(Exception::class)
    private fun send(method: String, url: String, body: HttpRequest.BodyPublisher): String {
        var req = HttpRequest.newBuilder().uri(URI.create(apiURL+url)).method(method, body)

        if (body != HttpRequest.BodyPublishers.noBody()) {
            req = req.header("Content-Type", "application/json")
        }

        if (accessToken != null) {
            req = req.header("Authorization", "Bearer $accessToken")
        }

        return client.send(req.build(), HttpResponse.BodyHandlers.ofString()).body()
    }
}

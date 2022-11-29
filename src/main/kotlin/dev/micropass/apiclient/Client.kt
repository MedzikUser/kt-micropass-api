package dev.micropass.apiclient

import com.google.gson.Gson
import java.lang.RuntimeException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Client(val accessToken: String?) {
    val apiURL = "https://micropass-api.medzik.xyz"

    private val client = HttpClient.newBuilder().build()

    fun get(url: String): String {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL+url))

        if (accessToken != null) {
            request = request.header("Authorization", "Bearer $accessToken")
        }

        try {
            return client.send(request.build(), HttpResponse.BodyHandlers.ofString()).body()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun post(url: String, body: String): String {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL+url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")

        if (accessToken != null) {
            request = request.header("Authorization", "Bearer $accessToken")
        }

        try {
            return client.send(request.build(), HttpResponse.BodyHandlers.ofString()).body()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun post(url: String, body: HashMap<*, *>): String {
        return post(url, Gson().toJson(body))
    }

    fun patch(url: String, body: String): String {
        var request = HttpRequest.newBuilder()
            .uri(URI.create(apiURL+url))
            .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
            .header("Content-Type", "application/json")

        if (accessToken != null) {
            request = request.header("Authorization", "Bearer $accessToken")
        }

        try {
            return client.send(request.build(), HttpResponse.BodyHandlers.ofString()).body()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun patch(url: String, body: HashMap<*, *>): String {
        return patch(url, Gson().toJson(body))
    }

    fun delete(url: String): String {
        var request = HttpRequest.newBuilder().method("DELETE", HttpRequest.BodyPublishers.noBody()).uri(URI.create(apiURL+url))

        if (accessToken != null) {
            request = request.header("Authorization", "Bearer $accessToken")
        }

        try {
            return client.send(request.build(), HttpResponse.BodyHandlers.ofString()).body()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

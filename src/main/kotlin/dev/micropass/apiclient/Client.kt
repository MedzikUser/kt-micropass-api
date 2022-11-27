package dev.micropass.apiclient

import java.lang.RuntimeException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

public class Client {
    val url = "https://micropass-api.medzik.xyz"
//    val url = "http://localhost:3000/api"

    private val client = HttpClient.newBuilder().build()

    fun get(url: String): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun post(url: String, body: String): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build()

        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            return response.body()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

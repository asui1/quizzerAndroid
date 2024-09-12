package com.asu1.quizzer.network

import com.asu1.quizzer.model.VersionResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertEquals

class ApiServiceTests {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getVersion should return VersionResponse`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("""{"latestVersion": "1.1.0"}""")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response: Response<VersionResponse> = apiService.getVersion()

        assertEquals("1.1.0", response.body()?.latestVersion)
    }
}
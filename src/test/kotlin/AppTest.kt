
/**
 * Created on 2018/01/10.
 */

import org.junit.After
import org.junit.Before

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import junit.framework.TestCase.assertEquals
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.junit.Test
import spark.Spark

class SparkTest {

    private val httpClient = HttpClients.custom().build()
    private val serverHost = "http://localhost"
    private val serverPort = "8080"


    private fun url():String {
        return serverHost + ":" + serverPort
    }

    private fun get(path:String):HttpResponse{
        val httpGet = HttpGet(url() + path)
        return httpClient.execute(httpGet)
    }

    @Before
    fun setup() {
        main(Array(0, {_ -> ""}))
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Thread.sleep(1000)
        Spark.stop()
    }

    @Test
    @Throws(IOException::class)
    fun testRooting() {
        val response = get("/")

        val statusCode = response.statusLine.statusCode
        val rd = BufferedReader(InputStreamReader(response.entity.content))

        val result = StringBuffer()
        var line = rd.readLine()
        while (line != null) {
            result.append(line)
            line = rd.readLine()
        }

        assertEquals(200, statusCode)
        assertEquals("Hello World!", result.toString())
    }
}
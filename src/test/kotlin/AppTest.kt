
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

class RequestHandler {
    private val httpClient = HttpClients.custom().build()
    private val serverHost = "localhost"
    private val serverPort = "8080"

    fun get(path:String) :HttpResponse {
        val httpGet = HttpGet(url() + path)
        val response = httpClient.execute(httpGet)
        return response
    }

    private fun url():String {
        return "http://$serverHost:$serverPort"
    }


}
class SparkTest {

    private val handler = RequestHandler()

    @Before
    fun setup() {
        // Appを起動
        main(Array(0, {_ -> ""}))
        Thread.sleep(1500)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        // Appを停止
        Thread.sleep(1000)
        Spark.stop()
    }

    @Test
    @Throws(IOException::class)
    fun testRooting() {
        val response = handler.get("/")

        val statusCode = response.statusLine.statusCode
        val rd = BufferedReader(InputStreamReader(response.entity.content))

        val result = StringBuffer()
        var line = rd.readLine()

        do {
            result.append(line)
            line = rd.readLine()

        } while (line != null)

        assertEquals(200, statusCode)
        assertEquals("Hello World!", result.toString())
    }
}
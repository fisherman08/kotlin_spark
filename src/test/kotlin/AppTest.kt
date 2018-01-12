
/**
 * Created on 2018/01/10.
 */

import java.io.BufferedReader
import java.io.InputStreamReader

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*
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

    fun execGet(path :String) :HashMap<String, String>{
        val result = HashMap<String, String>()

        val response = get(path)
        val statusCode = response.statusLine.statusCode
        val rd = BufferedReader(InputStreamReader(response.entity.content))

        val body = StringBuffer()
        var line = rd.readLine()

        do {
            body.append(line)
            line = rd.readLine()

        } while (line != null)

        result["statusCode"] = statusCode.toString()
        result["responseBody"] = body.toString()

        return result
    }

}
object SparkTest : Spek({
    val handler = RequestHandler()

    given("rooting") {

        beforeGroup {
            // Appを起動
            main(Array(0, {_ -> ""}))
            Thread.sleep(1500)
        }

        afterGroup {
            // Appを停止
            Thread.sleep(1000)
            Spark.stop()
        }

        on("/") {
            val response = handler.execGet("/")

            it("should return valid response"){
                assertEquals("200", response["statusCode"])
                assertEquals("Hello World!", response["responseBody"])
            }

        }

        on("/template") {
            val response = handler.execGet("/template")

            it("should return valid response"){
                assertEquals("200", response["statusCode"])
                assertTrue(response["responseBody"].toString().contains("suzuki"))
            }

        }
    }
})

/**
 * Created on 2018/01/10.
 */

import spark.Spark.*

fun main(args: Array<String>) {
    port(8080)

    get("/") { request, response -> "Hello World!" }

    get("/stop") { request, response ->
        stop()
    }
}
/**
 * Created on 2018/01/10.
 */

import spark.ModelAndView
import spark.Spark.*
import spark.kotlin.get
import spark.template.velocity.VelocityTemplateEngine
import java.util.HashMap



fun main(args: Array<String>) {
    port(8080)

    get("/") { request, response -> "Hello World!" }

    get("/template") { req, res ->
        val model :HashMap<String, Any> = hashMapOf("name" to "suzuki", "age" to "25")
        VelocityTemplateEngine().render(
                ModelAndView(model, "template.vm")
        )
    }

    get("/stop") { request, response ->
        stop()
    }
}
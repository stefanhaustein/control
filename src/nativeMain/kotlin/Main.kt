import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

val ROOT_PATH = "/Users/haustein/src/control"
val STATIC_RESOURCES_PATH = "$ROOT_PATH/src/commonMain/resources/static"


fun main() {
    embeddedServer(
        factory = CIO,
        module = Application::module,
        configure = {
            connector { port = 8080 }
            reuseAddress = true
        }
    ).start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/{path...}") {
            try {
                val requestPath =  call.parameters.getAll("path")?.joinToString("/") ?: ""
                val resourcePath = Path(STATIC_RESOURCES_PATH + "/" + requestPath)
                val source = SystemFileSystem.source(resourcePath)
                val text = source.buffered().readText()
                call.respondText(text, ContentType.defaultForFilePath(requestPath))
//                call.respondText("Path: $path; Files: " + source)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText(e.toString())
            }
        }
    }
}

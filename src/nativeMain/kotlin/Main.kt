import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
    }
}

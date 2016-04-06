import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{FormData, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, FromRequestUnmarshaller, PredefinedFromEntityUnmarshallers, Unmarshaller}
import akka.stream.ActorMaterializer
import com.animedetour.twellio.TwilioMessage
import com.sun.xml.internal.ws.util.Pool.Unmarshaller

import scala.io.StdIn

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher
  implicit val twilioRequestUnmarshaller : FromEntityUnmarshaller[TwilioMessage] =
    PredefinedFromEntityUnmarshallers.defaultUrlEncodedFormDataUnmarshaller.map { formData: FormData =>
      TwilioMessage(
        body = formData.fields.getOrElse("Body", "none"),
        from = formData.fields.getOrElse("From", "none")
      )
    }

  val route =
    path("hello") {
      get {
        complete {
          "Hello World"
        }
      }
      post {
        decodeRequest {
          entity(as[TwilioMessage]) { twilioMessage =>
            complete {
              twilioMessage.body + twilioMessage.from
            }
          }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done
}

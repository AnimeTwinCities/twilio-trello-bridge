import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{FormData, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, FromRequestUnmarshaller, PredefinedFromEntityUnmarshallers, Unmarshaller}
import akka.stream.ActorMaterializer
import com.animedetour.twellio.{TwilioActor, TwilioMessage}
import com.sun.xml.internal.ws.util.Pool.Unmarshaller
import com.typesafe.config.{Config, ConfigFactory}

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

  val twilioActor = system.actorOf(Props[TwilioActor], "twilioActor")

  val route =
    path("hello") {
      post {
        decodeRequest {
          entity(as[TwilioMessage]) { twilioMessage =>
            complete {
              twilioActor ! twilioMessage
              "body:" + twilioMessage.body + " from:" + twilioMessage.from
            }
          }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
}

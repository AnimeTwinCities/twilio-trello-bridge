package com.animedetour.twellio

import akka.http.javadsl.model.FormData
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, FromRequestUnmarshaller, PredefinedFromEntityUnmarshallers}
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by wertible on 4/3/16.
  */
object TwilioRequestUnmarshaller {
  implicit def twilioRequestUnmarshaller : FromEntityUnmarshaller[TwilioMessage] =
    PredefinedFromEntityUnmarshallers.defaultUrlEncodedFormDataUnmarshaller.map()
}

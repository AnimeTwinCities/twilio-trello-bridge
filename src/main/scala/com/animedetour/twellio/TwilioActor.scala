package com.animedetour.twellio

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.event.Logging

/**
  * Actor for recieving messages from Twilio.
  */
class TwilioActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case twilioMessage : TwilioMessage =>
    case unsupportedMessage => log.error("Received invalid message from twilio:_ " + unsupportedMessage)
  }
}

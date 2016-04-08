package com.animedetour.twellio

import akka.actor.Actor
import akka.event.Logging
import com.typesafe.config.{Config, ConfigFactory}
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
  * Actor for recieving messages from Twilio.
  */
class TwilioActor extends Actor {
  val log = Logging(context.system, this)
  val trelloConfig : Config = ConfigFactory.load()
  val retrofit : Retrofit = new Retrofit.Builder().baseUrl("https:/api.trello.com/1/").addConverterFactory(GsonConverterFactory.create()).build()
  val trelloClient : TrelloClient = retrofit.create(classOf[TrelloClient])
  val trelloKey : String = trelloConfig.getString("trello.key")
  val trelloToken : String = trelloConfig.getString("trello.token")
  val trelloListId : String = trelloConfig.getString("trello.listId")

  override def receive: Receive = {
    case twilioMessage : TwilioMessage => respondTo(twilioMessage)
    case unsupportedMessage => log.error("Received invalid message from twilio:_ " + unsupportedMessage)
  }

  def respondTo(message: TwilioMessage): Unit = {
    createCommentForCard(
      createCard(message),
      createTextFromMessageForComment(message)
    )
  }

  def createCard(message: TwilioMessage): String = {
    trelloClient.addCard(trelloListId, trelloKey, trelloToken, message.body, null).execute().body().getId
  }

  def createCommentForCard(cardId: String, text: String): Unit = {
    trelloClient.addComment(cardId, trelloKey, trelloToken, text).execute().body()
  }

  def createTextFromMessageForComment(message: TwilioMessage): String = {
    s"From: ${message.from}"
  }
}

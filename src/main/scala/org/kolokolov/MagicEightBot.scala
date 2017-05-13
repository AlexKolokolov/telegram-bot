package org.kolokolov

import com.typesafe.scalalogging.Logger
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import org.kolokolov.MessageHandler._

import scala.concurrent.Future

/**
  * Created by Kolokolov on 12.05.2017.
  */
class MagicEightBot(val token: String) extends TelegramBot with Polling with Commands {

  override val logger = Logger("HistoryLogger")

  on("/start") {
    implicit msg => _ => {
      for {
        user <- msg.from
      } {
        reply(s"Hello, ${user.firstName}! I can predict future. Please, ask me something.")
        logger.info(s"User ${user.firstName} (${user.username.getOrElse("Unknown Nik")}) has connected")
      }
    }
  }

  override def onMessage(msg: Message): Unit ={
    request(SendChatAction(msg.source, ChatAction.Typing))
    super.onMessage(msg)
    for {
      question <- msg.text
      user <- msg.from
      if question != "/start"
    } {
      val answer = answerQuestion(question)
      request(SendMessage(msg.source, answer))
      logger.info(s"question: ${user.firstName} (${user.username.getOrElse("Unknown Nik")}) : $question -- bot's answer:  $answer")
    }
  }
}

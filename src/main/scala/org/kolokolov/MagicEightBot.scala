package org.kolokolov

import com.typesafe.scalalogging.Logger
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import org.kolokolov.MessageHandler._

import scala.io.Source


/**
  * Created by Kolokolov on 12.05.2017.
  */
class MagicEightBot extends TelegramBot with Polling with Commands {

  override val logger = Logger("HistoryLogger")

  lazy val token: String = Source.fromFile("bot.token").getLines.mkString

  on("/start") {
    implicit msg => _ => {
      for {
        user <- msg.from
      } {
        reply(s"Hello, ${user.firstName}! Nice to meet you! I'm a magic 8 ball predicting future. Please, ask me something.")
        logger.info(s"User ${user.firstName} ${user.lastName.getOrElse("'NoLastName'")} (${user.username.getOrElse("Unknown Nik")}) has connected")
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
        answerQuestion(question).foreach { answer =>
        request(SendMessage(msg.source, answer))
        logger.info(s"question: ${user.firstName} ${user.lastName.getOrElse("'NoLastName'")} (${user.username.getOrElse("Unknown Nik")}) : $question -- bot's answer:  $answer")
      }
    }
  }
}

package org.kolokolov

import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import org.kolokolov.MessageHandler._

/**
  * Created by andersen on 12.05.2017.
  */
class MagicEightBot(val token: String) extends TelegramBot with Polling with Commands {

  on("/start") {
    implicit msg => _ => {
      for {
        user <- msg.from
      }
        reply(s"Hello, ${user.firstName}! I can predict future. Please, ask me something.")
    }
  }

  override def onMessage(msg: Message): Unit ={
    request(SendChatAction(msg.source, ChatAction.Typing))
    super.onMessage(msg)
    for {
      question <- msg.text
      if question != "/start"
    } request(SendMessage(msg.source, answerQuestion(question)))
  }
}

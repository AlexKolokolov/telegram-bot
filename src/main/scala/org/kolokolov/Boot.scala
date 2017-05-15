package org.kolokolov


import scala.io.StdIn

/**
  * Created by andersen on 12.05.2017.
  */
object Boot extends App {
  val bot = new MagicEightBot
  bot.run()

  println("Magic8Bot is running. Press ENTER to stop...")
  StdIn.readLine()
  bot.shutdown()
}

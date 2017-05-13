package org.kolokolov

import scala.util.Random

/**
  * Created by Kolokolov on 13.05.2017.
  */
object MessageHandler {

  val futureQuestionStarters = List("will", "should", "must", "shall", "would", "may")

  val probableFutureQuestionsAttributes = List(
    List("is", "are", "am") -> List("going to","gonna"),
    List("do", "does") -> List("have to"))

  val pastQuestionsStarters = List("was","were","have","has","had")

  val positiveAnswers = Vector(
    "Yes.", "No.", "Probably yes.", "Probably no.", "Ask later.", "Yes.", "No.",
    "Ask something else.", "Sure!", "No way!", "Yes.", "No.",
    "Probably yes.", "Probably no.", "Without a doubt!", "I doubt it.",
    "I don't think so", "Can't say for sure.", "That's for sure!", "No doubt.")

  val answersForNoQuestions = Vector(
    "Is it a question?",
    "You are supposed to ask a question.",
    "Question, dude! I can only answer questions!",
    "Are you sure that it is a question? Because I'm not.",
    "Don't you understand what \"question\" is?")

  val answersForNotFutureQuestions = Vector(
    "I can only predict future. Please, no questions about past or present time!",
    "FUTURE, dude! Ask about FUTURE!",
    "Are you sure that your question is about future?")

  val answersForNotSimpleQuestions = Vector(
    "I can only answer simple questions. Yes or no.",
    "Yes or no question, please.",
    "YES or NO questions only! OMG, it is not a rocket science!")

  def isQuestion(sentence: String): Boolean = {
    sentence.endsWith("?")
  }

  private def startsWith(starters: List[String])(question: String): Boolean = starters match {
    case Nil => false
    case head :: tail => question.toLowerCase.startsWith(head) || startsWith(tail)(question)
  }

  private def contains(starters: List[String])(question: String): Boolean = starters match {
    case Nil => false
    case head :: tail => question.toLowerCase.contains(head) || contains(tail)(question)
  }

  private def ifFutureQuestion(question: String): Boolean = {
    def probablyFutureQuestion(question: String, attributes: List[(List[String],List[String])]): Boolean = attributes match {
      case Nil => false
      case head :: tail =>  startsWith(head._1)(question) && contains(head._2)(question) || probablyFutureQuestion(question, tail)
    }
    startsWith(futureQuestionStarters)(question) || probablyFutureQuestion(question,probableFutureQuestionsAttributes)
  }

  private def isSimpleQuestion: String => Boolean = {
    startsWith(futureQuestionStarters ++ pastQuestionsStarters ++ probableFutureQuestionsAttributes.flatMap(t => t._1))
  }

  private def chooseRandomAnswer(answers: Vector[String]): String = answers(Random.nextInt(answers.length))

  def answerQuestion(question: String): String = question match {
    case q if !isQuestion(q) => chooseRandomAnswer(answersForNoQuestions)
    case q if !isSimpleQuestion(q) => chooseRandomAnswer(answersForNotSimpleQuestions)
    case q if !ifFutureQuestion(q) => chooseRandomAnswer(answersForNotFutureQuestions)
    case _ => chooseRandomAnswer(positiveAnswers)
  }
}

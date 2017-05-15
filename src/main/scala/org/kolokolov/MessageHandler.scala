package org.kolokolov

import info.mukel.telegrambot4s.models.User

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

/**
  * Created by Kolokolov on 13.05.2017.
  */
object MessageHandler {

  val greetings = List("hi", "hello", "hey", "good morning", "good evening", "good afternoon")

  val futureQuestionStarters = List("will", "should", "must", "shall", "would", "may", "can")

  val probableFutureQuestionsAttributes = List(
    List("is", "are", "am") -> List("going","gonna","supposed"),
    List("do", "does") -> List("have to","need to"))

  val pastQuestionsStarters = List("was","were","have","has","had")

  val incorrectQuestionsStarters = List("i'm", "i am", "i have", "i need", "i should", "i would", "i can", "i may")

  val greetingsAnswers = Vector(
    "Hello! Please, ask me a question.",
    "Hi. Your question, please.",
    "Nice to see you! Ask me something about future.",
    "Hello! I'm standing by for your question.")

  val positiveAnswers = Vector(
    "Yes.", "No.", "Probably yes.", "Probably no.", "Ask later.",
    "Ask something else.", "Sure!", "No way!", "Without a doubt!",
    "I doubt it.", "I don't think so.", "Can't say for sure.",
    "That's for sure!", "No doubt.", "Definitely NO!")

  val answersForNotQuestions = Vector(
    "Is it a question?",
    "You are supposed to ask a question.",
    "Question, please! I can only answer questions!",
    "Are you sure that it was a question? Because I'm not.",
    "Don't you understand what \"question\" is?. It should end with \"?\"")

  val answerForIncorrectQuestion = Vector(
    "Are you sure that your question is correct? Please, use proper English grammar.",
    "Are you sure that your question is correct? I can't understand it.",
    "Sorry, it was probably not a correct question. I can't understand it. Reformulate it, please.",
    "Are you sure that it was a correct question? Because I'm can't understand it. Could you reformulate it?"
  )

  val answersForNotFutureQuestions = Vector(
    "I can only predict future. Please, no questions about past or present time!",
    "FUTURE, please! Ask about FUTURE!",
    "Are you sure that your question was about future?",
    "I'm not sure that your question was about future. Could you reformulate it somehow?")

  val answersForNotSimpleQuestions = Vector(
    "I can only answer simple questions. Yes or no.",
    "Yes or no question, please.",
    "YES or NO questions only! OMG, it is not a rocket science!")

  private def startsWithOneOf(starters: List[String])(question: String): Boolean = starters match {
    case Nil => false
    case head :: tail => question.toLowerCase.startsWith(head) || startsWithOneOf(tail)(question)
  }

  private def containsOneOf(starters: List[String])(question: String): Boolean = starters match {
    case Nil => false
    case head :: tail => question.toLowerCase.contains(head) || containsOneOf(tail)(question)
  }

  def isQuestion(sentence: String): Boolean = sentence.endsWith("?")

  def isIncorrectQuestion: String => Boolean = startsWithOneOf(incorrectQuestionsStarters)

  def ifFutureQuestion(question: String): Boolean = {
    def probablyFutureQuestion(question: String, attributes: List[(List[String],List[String])]): Boolean = attributes match {
      case Nil => false
      case head :: tail =>  startsWithOneOf(head._1)(question) && containsOneOf(head._2)(question) || probablyFutureQuestion(question, tail)
    }
    startsWithOneOf(futureQuestionStarters)(question) || probablyFutureQuestion(question,probableFutureQuestionsAttributes)
  }

  def isSimpleQuestion: String => Boolean = {
    startsWithOneOf(futureQuestionStarters ++
      pastQuestionsStarters ++
      probableFutureQuestionsAttributes.flatMap(t => t._1))
  }

  def isGreeting: String => Boolean = startsWithOneOf(greetings)

  private def chooseRandomAnswer(answers: Vector[String]): String = answers(Random.nextInt(answers.length))

  def userGreeting(user: User): String = {
      s"Hello, ${user.firstName}! Nice to meet you! I'm a magic 8 ball predicting future. Please, ask me something."
  }

  def answerQuestion(question: String)(implicit ec: ExecutionContext): Future[String] = {
    Future {
      question match {
        case q if isGreeting(q) => chooseRandomAnswer(greetingsAnswers)
        case q if !isQuestion(q) => chooseRandomAnswer(answersForNotQuestions)
        case q if isIncorrectQuestion(q) => chooseRandomAnswer(answerForIncorrectQuestion)
        case q if !isSimpleQuestion(q) => chooseRandomAnswer(answersForNotSimpleQuestions)
        case q if !ifFutureQuestion(q) => chooseRandomAnswer(answersForNotFutureQuestions)
        case _ => chooseRandomAnswer(positiveAnswers)
      }
    }
  }
}

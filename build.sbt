name := "magic8telegram-bot"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "info.mukel" %% "telegrambot4s" % "2.2.1-SNAPSHOT"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
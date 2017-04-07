name := "ansible-module"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.5.12",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
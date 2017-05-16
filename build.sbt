lazy val commonSettings = Seq(
  name := "ansiblemodule-basic",
  description := "Ansible Module implementation for Scala",
  version in ThisBuild := "0.1.0"
)

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.11.8",
    commonSettings,
    resolvers := Seq(
      Resolver.typesafeRepo("releases")
    ),
    libraryDependencies := Seq(
      "com.typesafe.play" %% "play-json" % "2.5.12",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    publishMavenStyle := false,
    bintrayRepository := "ansible",
    bintrayOrganization in bintray := None,
    bintrayPackageLabels := Seq("ansible")
  )

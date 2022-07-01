import Dependencies._

Global / semanticdbEnabled := true // for metals

ThisBuild / scalaVersion     := "3.1.3"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "pro.reiss"
ThisBuild / organizationName := "reiss.pro"

lazy val `typicode-graph` = project
  .in(file("."))
  .settings(name := "typicode-graph")
  .aggregate(frontend, backend, domain.jvm, domain.js)

lazy val frontend = project
  .in(file("modules/frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
    },
    scalaJSLinkerConfig ~= {
      _.withSourceMap(false)
    },
    libraryDependencies ++= Seq(
      Libraries.tyrian.value,
      Libraries.`caliban-client`.value,
    ),
  )
  .dependsOn(domain.js)

lazy val backend = project
  .in(file("modules/backend"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.`sttp-client3-zio`.value,
      Libraries.caliban.value,
      Libraries.`caliban-cats`.value,
      Libraries.`caliban-zio-http`.value,
      Libraries.zio.value,
      Libraries.`zio-query`.value,
    ),
    excludeDependencies += "org.scala-lang.modules" % "scala-collection-compat_2.13",
  )
  .dependsOn(domain.jvm)

lazy val domain = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/domain"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.`zio-json`.value
    )
  )

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-source:future",
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xmax-inlines:64",
    "-Ykind-projector",
    "-rewrite",
    "-indent",
  )
)

addSbtPlugin("com.github.ghostdogpr" % "caliban-codegen-sbt"      % "2.0.1")
addSbtPlugin("org.scala-js"          % "sbt-scalajs"              % "1.11.0")
addSbtPlugin("org.portable-scala"    % "sbt-scalajs-crossproject" % "1.2.0")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"

addSbtPlugin("com.github.ghostdogpr" % "caliban-codegen-sbt"      % "2.0.2")
addSbtPlugin("org.scala-js"          % "sbt-scalajs"              % "1.12.0")
addSbtPlugin("org.portable-scala"    % "sbt-scalajs-crossproject" % "1.2.0")
addSbtPlugin("com.timushev.sbt"      % "sbt-updates"              % "0.6.3")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  object V         {
    val laminar           = "0.14.2"
    val caliban           = "2.0.1"
    val sttp3             = "3.7.4"
    val zio               = "2.0.0"
    val `zio-config`      = "3.0.2"
    val `zio-query`       = "0.3.1"
    val `zio-json`        = "0.3.0-RC10"
    val `scala-java-time` = "2.4.0"
  }
  object Libraries {
    val `scala-java-time`         = Def.setting("io.github.cquiroz" %%% "scala-java-time" % V.`scala-java-time`)
    val laminar                   = Def.setting("com.raquo" %%% "laminar" % V.laminar)
    val caliban                   = Def.setting("com.github.ghostdogpr" %%% "caliban" % V.caliban)
    val `caliban-client`          = Def.setting("com.github.ghostdogpr" %%% "caliban-client" % V.caliban)
    val `caliban-client-laminext` = Def.setting("com.github.ghostdogpr" %%% "caliban-client-laminext" % V.caliban)
    val `caliban-zio-http`        = Def.setting("com.github.ghostdogpr" %% "caliban-zio-http" % V.caliban)
    val `sttp-client3-core`       = Def.setting("com.softwaremill.sttp.client3" %%% "core" % V.sttp3)
    val `sttp-client3-zio`        = Def.setting("com.softwaremill.sttp.client3" %%% "zio" % V.sttp3)
    val zio                       = Def.setting("dev.zio" %% "zio" % V.zio)
    val `zio-config`              = Def.setting("dev.zio" %% "zio-config" % V.`zio-config`)
    val `zio-config-typesafe`     = Def.setting("dev.zio" %% "zio-config-typesafe" % V.`zio-config`)
    val `zio-query`               = Def.setting("dev.zio" %% "zio-query" % V.`zio-query`)
    val `zio-json`                = Def.setting("dev.zio" %%% "zio-json" % V.`zio-json`)
  }
}

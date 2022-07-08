import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  object V         {
    val tyrian      = "0.5.1"
    val caliban     = "2.0.0-RC2"
    val sttp3       = "3.6.2"
    val zio         = "2.0.0"
    val `zio-query` = "0.3.0"
    val `zio-json`  = "0.3.0-RC10"
  }
  object Libraries {
    val tyrian             = Def.setting("io.indigoengine" %%% "tyrian" % V.tyrian)
    val caliban            = Def.setting("com.github.ghostdogpr" %%% "caliban" % V.caliban)
    val `caliban-cats`     = Def.setting("com.github.ghostdogpr" %% "caliban-cats" % V.caliban)
    val `caliban-client`   = Def.setting("com.github.ghostdogpr" %% "caliban-client" % V.caliban)
    val `caliban-zio-http` = Def.setting("com.github.ghostdogpr" %% "caliban-zio-http" % V.caliban)
    val zio                = Def.setting("dev.zio" %% "zio" % V.zio)
    val `zio-query`        = Def.setting("dev.zio" %% "zio-query" % V.`zio-query`)
    val `zio-json`         = Def.setting("dev.zio" %% "zio-json" % V.`zio-json`)
    val `sttp-client3-zio` = Def.setting("com.softwaremill.sttp.client3" %% "zio" % V.sttp3)
  }
}

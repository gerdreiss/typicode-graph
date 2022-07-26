package typicode.services

import zio.*
import zio.config.*
import zio.config.typesafe.TypesafeConfigSource
import com.typesafe.config.ConfigFactory

case class TypicodeConfig(protocol: String, host: String, port: Int):
  val baseUrl: String = s"$protocol://$host:$port"

object TypicodeConfig:
  import ConfigDescriptor.*

  private val properties =
    TypesafeConfigSource.fromTypesafeConfig(ZIO.attempt(ConfigFactory.defaultApplication()))

  val config: ConfigDescriptor[TypicodeConfig] =
    string("protocol")
      .zip(string("host"))
      .zip(int("port"))
      .to[TypicodeConfig]
      .from(properties)

  val live = configLayer_(config)

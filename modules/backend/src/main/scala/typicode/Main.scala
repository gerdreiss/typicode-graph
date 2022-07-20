package typicode

import caliban.*
import caliban.wrappers.Wrappers.*
import sttp.client3.httpclient.zio.HttpClientZioBackend
import typicode.resolvers.*
import typicode.services.*
import zhttp.http.*
import zhttp.service.Server
import zio.*
import zio.stream.ZStream

import scala.language.postfixOps

object Main extends ZIOAppDefault:

  val api: GraphQL[TypicodeService] =
    GraphQL
      .graphQL[TypicodeService, Queries, Unit, Unit](
        RootResolver(Queries(user => UserView.resolve(user.id)))
      ) @@
      maxFields(200) @@               // query analyzer that limit query fields
      maxDepth(50) @@                 // query analyzer that limit query depth
      timeout(3 seconds) @@           // wrapper that fails slow queries
      printSlowQueries(500 millis) @@ // wrapper that logs slow queries
      printErrors                     // wrapper that logs errors

  val program: ZIO[TypicodeService, Throwable, Unit] =
    api
      .interpreter
      .flatMap { interpreter =>
        Server
          .start(
            8088,
            Http.collectHttp[Request] {
              case _ -> !! / "api" / "graphql" => ZHttpAdapter.makeHttpService(interpreter)
              case _ -> !! / "ws" / "graphql"  => ZHttpAdapter.makeWebSocketService(interpreter)
              case _ -> !! / "graphiql"        => Http.fromStream(ZStream.fromResource("graphiql.html"))
            },
          )
          .forever
      }

  override def run: ZIO[Any, Any, Any] =
    program.provide(
      HttpClientZioBackend.layer(),
      TypicodeService.live,
    )

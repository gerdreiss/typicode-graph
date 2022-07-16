package typicode

import caliban.GraphQL
import caliban.RootResolver
import caliban.ZHttpAdapter
import caliban.wrappers.Wrappers.*

import sttp.client3.httpclient.zio.HttpClientZioBackend

import zhttp.http.*
import zhttp.service.Server

import zio.*
import zio.stream.ZStream

import typicode.resolvers.*
import typicode.services.*

import scala.language.postfixOps

object Main extends ZIOAppDefault:
  val api = GraphQL
    .graphQL[TypicodeService, Queries, Unit, Unit](
      RootResolver(Queries(user => UserView.resolve(user.id)))
    ) @@
    maxFields(50) @@                // query analyzer that limit query fields
    maxDepth(10) @@                 // query analyzer that limit query depth
    timeout(3 seconds) @@           // wrapper that fails slow queries
    printSlowQueries(500 millis) @@ // wrapper that logs slow queries
    printErrors                     // wrapper that logs errors

  val program =
    for
      interpreter <- api.interpreter
      // Server.start() doesn't compile
      // java.lang.AssertionError: assertion failed
      // [error] scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:11)
      // _           <- Server
      //                  .start(
      //                    8088,
      //                    Http.route[Request] {
      //                      case _ -> !! / "api" / "graphql" => ZHttpAdapter.makeHttpService(interpreter)
      //                      case _ -> !! / "ws" / "graphql"  => ZHttpAdapter.makeWebSocketService(interpreter)
      //                      case _ -> !! / "graphiql"        => Http.fromStream(ZStream.fromResource("graphiql.html"))
      //                    },
      //                  )
      //                  .forever
      // this compiles, but fails to run with 'Caused by: java.lang.ClassNotFoundException: zio.IO$'
      result      <- interpreter.execute(Queries.user)
      _           <- Console.printLine(result.data.toString)
    yield ()

  override def run =
    program.provide(
      ZLayer.succeed(Clock.ClockLive),
      ZLayer.succeed(Console.ConsoleLive),
      HttpClientZioBackend.layer(),
      TypicodeService.live
    )

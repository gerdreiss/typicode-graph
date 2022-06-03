package typicode

import caliban.Http4sAdapter
import caliban.*
import caliban.wrappers.Wrappers.*

import cats.data.Kleisli

import org.http4s.StaticFile
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.CORS

import sttp.client3.httpclient.zio.*

import zhttp.http.*
import zhttp.service.Server

import zio.*
import zio.interop.catz.*
import zio.stream.ZStream

import typicode.resolvers.*
import typicode.services.*

import scala.language.postfixOps

object Main extends ZIOAppDefault:
  type TypicodeEnv  = ZEnv & SttpClient & TypicodeService
  type TypicodeF[A] = RIO[TypicodeEnv, A]

  val api = GraphQL
    .graphQL[SttpClient & TypicodeService, Queries, Unit, Unit](
      RootResolver(Queries(user => UserView.resolve(user.id)))
    ) @@
    maxFields(200) @@               // query analyzer that limit query fields
    maxDepth(30) @@                 // query analyzer that limit query depth
    timeout(3 seconds) @@           // wrapper that fails slow queries
    printSlowQueries(500 millis) @@ // wrapper that logs slow queries
    printErrors                     // wrapper that logs errors

  val program =
    // ZIO
    //   .runtime[TypicodeEnv]
    //   .flatMap(implicit runtime =>
    for
      interpreter <- api.interpreter
      // this doesn't compile
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
      // this doesn't compile either
      // _           <- BlazeServerBuilder[TypicodeF]
      //                  .bindHttp(8088, "localhost")
      //                  .withHttpWebSocketApp(wsBuilder =>
      //                    Router[TypicodeF](
      //                      "/api/graphql" -> CORS.policy(Http4sAdapter.makeHttpService(interpreter)),
      //                      "/ws/graphql"  -> CORS.policy(Http4sAdapter.makeWebSocketService(wsBuilder, interpreter)),
      //                      "/graphiql"    -> Kleisli.liftF(StaticFile.fromResource("/graphiql.html", None)),
      //                    ).orNotFound
      //                  )
      //                  .resource
      //                  .toManagedZIO
      //                  .useForever
      // this compiles, but fails to run with 'Caused by: java.lang.ClassNotFoundException: zio.IO$'
      result      <- interpreter.execute(Queries.user)
      _           <- Console.printLine(result.data.toString)
    yield ()
  // )

  override def run =
    program
      .provide(HttpClientZioBackend.layer(), TypicodeService.live, Console.live, Clock.live)

package typicode.services

import sttp.client3.*
import sttp.client3.httpclient.zio.*
import sttp.model.*

import zio.*
import zio.json.JsonDecoder

import typicode.domain.*

trait TypicodeService:
  def getUser(userId: UserId): RIO[SttpClient, User]
  def getTodos(userId: UserId): RIO[SttpClient, Todos]
  def getPosts(userId: UserId): RIO[SttpClient, Posts]
  def getComments(postId: PostId): RIO[SttpClient, Comments]
  def getAlbums(userId: UserId): RIO[SttpClient, Albums]
  def getPhotos(albumId: AlbumId): RIO[SttpClient, Photos]

object TypicodeService:
  def getUser(userId: UserId)     = ZIO.serviceWithZIO[TypicodeService](_.getUser(userId))
  def getTodos(userId: UserId)    = ZIO.serviceWithZIO[TypicodeService](_.getTodos(userId))
  def getPosts(userId: UserId)    = ZIO.serviceWithZIO[TypicodeService](_.getPosts(userId))
  def getComments(postId: PostId) = ZIO.serviceWithZIO[TypicodeService](_.getComments(postId))
  def getAlbums(userId: UserId)   = ZIO.serviceWithZIO[TypicodeService](_.getAlbums(userId))
  def getPhotos(albumId: AlbumId) = ZIO.serviceWithZIO[TypicodeService](_.getPhotos(albumId))

  def live: ULayer[TypicodeService] = ZLayer.succeed {
    new:

      private val commonHeaders = Map("Content-Type" -> "application/json")

      // TODO get from config
      private val baseUrl = "https://jsonplaceholder.typicode.com"

      private def getUserURI(userId: UserId): Uri          = uri"$baseUrl/users/$userId"
      private def getUserTodosURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/todos"
      private def getUserPostsURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/posts"
      private def getUserAlbumURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/albums"
      private def getPostCommentsURI(postId: PostId): Uri  = uri"$baseUrl/posts/$postId/comments"
      private def getAlbumPhotosURI(albumId: AlbumId): Uri = uri"$baseUrl/albums/$albumId/photos"

      private def createRequest[T <: TypicodeData](
          uri: Uri,
          lastModified: Option[String] = None,
        )(using
          D: JsonDecoder[T]
        ): Request[Either[String, T], Any] =
        val headers = lastModified.map("If-Modified-Since" -> _).foldLeft(commonHeaders)(_ + _)
        basicRequest
          .get(uri)
          .headers(headers)
          .mapResponse(_.flatMap(D.decodeJson))

      private def getObject[T <: TypicodeData](uri: Uri)(using D: JsonDecoder[T]): RIO[SttpClient, T] =
        for
          response <- send(createRequest[T](uri))
          result   <- response.code match
                        case StatusCode.Ok =>
                          response.body match
                            case Right(body) => ZIO.succeed(body)
                            case Left(error) => ZIO.fail(new Exception(error))
                        case code          =>
                          ZIO.fail(new Exception(s"Unexpected response code: $code"))
        yield result

      def getUser(userId: UserId): ZIO[SttpClient, Throwable, User] =
        getObject[User](getUserURI(userId))

      def getTodos(userId: UserId): ZIO[SttpClient, Throwable, Todos] =
        getObject[Todos](getUserTodosURI(userId))

      def getPosts(userId: UserId): ZIO[SttpClient, Throwable, Posts] =
        getObject[Posts](getUserPostsURI(userId))

      def getComments(postId: PostId): ZIO[SttpClient, Throwable, Comments] =
        getObject[Comments](getPostCommentsURI(postId))

      def getAlbums(userId: UserId): ZIO[SttpClient, Throwable, Albums] =
        getObject[Albums](getUserAlbumURI(userId))

      def getPhotos(albumId: AlbumId): ZIO[SttpClient, Throwable, Photos] =
        getObject[Photos](getAlbumPhotosURI(albumId))
  }

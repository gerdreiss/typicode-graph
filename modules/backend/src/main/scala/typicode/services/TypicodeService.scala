package typicode.services

import sttp.client3.*
import sttp.model.{ StatusCode, Uri }
import typicode.domain.*
import zio.*
import zio.json.JsonDecoder

trait TypicodeService:
  def getUsers(username: Option[String]): Task[List[User]]
  def getUser(userId: UserId): Task[User]
  def getUserTodos(userId: UserId): Task[List[Todo]]
  def getUserPosts(userId: UserId): Task[List[Post]]
  def getPostComments(postId: PostId): Task[List[Comment]]
  def getUserAlbums(userId: UserId): Task[List[Album]]
  def getAlbumPhotos(albumId: AlbumId): Task[List[Photo]]

object TypicodeService:
  def getUsers(username: Option[String]) = ZIO.serviceWithZIO[TypicodeService](_.getUsers(username))
  def getUser(userId: UserId)            = ZIO.serviceWithZIO[TypicodeService](_.getUser(userId))
  def getUserTodos(userId: UserId)       = ZIO.serviceWithZIO[TypicodeService](_.getUserTodos(userId))
  def getUserPosts(userId: UserId)       = ZIO.serviceWithZIO[TypicodeService](_.getUserPosts(userId))
  def getPostComments(postId: PostId)    = ZIO.serviceWithZIO[TypicodeService](_.getPostComments(postId))
  def getUserAlbums(userId: UserId)      = ZIO.serviceWithZIO[TypicodeService](_.getUserAlbums(userId))
  def getAlbumPhotos(albumId: AlbumId)   = ZIO.serviceWithZIO[TypicodeService](_.getAlbumPhotos(albumId))

  def live: ZLayer[TypicodeConfig & SttpBackend[Task, Any], Any, TypicodeService] =
    ZLayer.fromFunction(TypicodeServiceLive.apply)

case class TypicodeServiceLive(config: TypicodeConfig, backend: SttpBackend[Task, Any])
    extends TypicodeService:
  private val commonHeaders = Map("Content-Type" -> "application/json")

  private def getUsersURI(username: Option[String]): Uri =
    uri"${config.baseUrl}/users${queryParam(username)}"

  private def queryParam(username: Option[String]): String =
    username.map(u => s"?username=$u").getOrElse("")

  private def getUserURI(userId: UserId): Uri          = uri"${config.baseUrl}/users/$userId"
  private def getUserTodosURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/todos"
  private def getUserPostsURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/posts"
  private def getUserAlbumURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/albums"
  private def getPostCommentsURI(postId: PostId): Uri  = uri"${config.baseUrl}/posts/$postId/comments"
  private def getAlbumPhotosURI(albumId: AlbumId): Uri = uri"${config.baseUrl}/albums/$albumId/photos"

  private def createRequest[T](uri: Uri, lastModified: Option[String] = None)(using
      D: JsonDecoder[T]
  ): Request[Either[String, T], Any] =
    basicRequest
      .get(uri)
      .headers(lastModified.map("If-Modified-Since" -> _).foldLeft(commonHeaders)(_ + _))
      .mapResponse(_.flatMap(D.decodeJson))

  private def getObject[T](uri: Uri)(using D: JsonDecoder[T]): Task[T] =
    for
      _        <- Console.printLine(s"GET $uri")
      response <- createRequest[T](uri).send(backend)
      result   <- response.code match
                    case StatusCode.Ok =>
                      response.body match
                        case Right(body) => ZIO.succeed(body)
                        case Left(error) => ZIO.fail(new Exception(error))
                    case code          =>
                      ZIO.fail(new Exception(s"Unexpected response code: $code"))
    yield result

  def getUsers(username: Option[String]): Task[List[User]] =
    getObject[List[User]](getUsersURI(username))

  def getUser(userId: UserId): Task[User] =
    getObject[User](getUserURI(userId))

  def getUserTodos(userId: UserId): Task[List[Todo]] =
    getObject[List[Todo]](getUserTodosURI(userId))

  def getUserPosts(userId: UserId): Task[List[Post]] =
    getObject[List[Post]](getUserPostsURI(userId))

  def getPostComments(postId: PostId): Task[List[Comment]] =
    getObject[List[Comment]](getPostCommentsURI(postId))

  def getUserAlbums(userId: UserId): Task[List[Album]] =
    getObject[List[Album]](getUserAlbumURI(userId))

  def getAlbumPhotos(albumId: AlbumId): Task[List[Photo]] =
    getObject[List[Photo]](getAlbumPhotosURI(albumId))

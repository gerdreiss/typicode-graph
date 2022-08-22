package typicode
package services

import sttp.client3.*
import sttp.model.{ StatusCode, Uri }
import zio.*
import zio.json.JsonDecoder

import domain.*

trait TypicodeService:
  def getUsers: Task[List[User]]
  def getUser(userId: UserId): Task[User]
  def getUserTodos(userId: UserId): Task[List[Todo]]
  def getUserPosts(userId: UserId): Task[List[Post]]
  def getUserAlbums(userId: UserId): Task[List[Album]]
  def getPost(postId: PostId): Task[Post]
  def getPostComments(postId: PostId): Task[List[Comment]]
  def getAlbum(albumId: AlbumId): Task[Album]
  def getAlbumPhotos(albumId: AlbumId): Task[List[Photo]]

object TypicodeService:
  def getUsers                         = ZIO.serviceWithZIO[TypicodeService](_.getUsers)
  def getUser(userId: UserId)          = ZIO.serviceWithZIO[TypicodeService](_.getUser(userId))
  def getUserTodos(userId: UserId)     = ZIO.serviceWithZIO[TypicodeService](_.getUserTodos(userId))
  def getUserPosts(userId: UserId)     = ZIO.serviceWithZIO[TypicodeService](_.getUserPosts(userId))
  def getUserAlbums(userId: UserId)    = ZIO.serviceWithZIO[TypicodeService](_.getUserAlbums(userId))
  def getPost(postId: PostId)          = ZIO.serviceWithZIO[TypicodeService](_.getPost(postId))
  def getPostComments(postId: PostId)  = ZIO.serviceWithZIO[TypicodeService](_.getPostComments(postId))
  def getAlbum(albumId: AlbumId)       = ZIO.serviceWithZIO[TypicodeService](_.getAlbum(albumId))
  def getAlbumPhotos(albumId: AlbumId) = ZIO.serviceWithZIO[TypicodeService](_.getAlbumPhotos(albumId))

  val live: ZLayer[TypicodeConfig & SttpBackend[Task, Any], Any, TypicodeService] =
    ZLayer.fromFunction(TypicodeServiceLive.apply)

case class TypicodeServiceLive(config: TypicodeConfig, backend: SttpBackend[Task, Any]) extends TypicodeService:

  private val commonHeaders = Map("Content-Type" -> "application/json")

  private def createRequest[T](
      uri: Uri,
      lastModified: Option[String] = None,
  )(using
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

  private def getUsersURI: Uri                         = uri"${config.baseUrl}/users"
  private def getUserURI(userId: UserId): Uri          = uri"$getUsersURI/$userId"
  private def getUserTodosURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/todos"
  private def getUserPostsURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/posts"
  private def getUserAlbumsURI(userId: UserId): Uri    = uri"${getUserURI(userId)}/albums"
  private def getPostURI(postId: PostId): Uri          = uri"${config.baseUrl}/posts/$postId"
  private def getPostCommentsURI(postId: PostId): Uri  = uri"${config.baseUrl}/posts/$postId/comments"
  private def getAlbumURI(albumId: AlbumId): Uri       = uri"${config.baseUrl}/albums/$albumId"
  private def getAlbumPhotosURI(albumId: AlbumId): Uri = uri"${config.baseUrl}/albums/$albumId/photos"

  def getUsers: Task[List[User]] =
    getObject[List[User]](getUsersURI)

  def getUser(userId: UserId): Task[User] =
    getObject[User](getUserURI(userId))

  def getUserTodos(userId: UserId): Task[List[Todo]] =
    getObject[List[Todo]](getUserTodosURI(userId))

  def getUserPosts(userId: UserId): Task[List[Post]] =
    getObject[List[Post]](getUserPostsURI(userId))

  def getUserAlbums(userId: UserId): Task[List[Album]] =
    getObject[List[Album]](getUserAlbumsURI(userId))

  def getPost(postId: PostId): Task[Post] =
    getObject[Post](getPostURI(postId))

  def getPostComments(postId: PostId): Task[List[Comment]] =
    getObject[List[Comment]](getPostCommentsURI(postId))

  def getAlbum(albumId: AlbumId): Task[Album] =
    getObject[Album](getAlbumURI(albumId))

  def getAlbumPhotos(albumId: AlbumId): Task[List[Photo]] =
    getObject[List[Photo]](getAlbumPhotosURI(albumId))

package typicode.services

import sttp.client3.*
import sttp.model.{ StatusCode, Uri }
import typicode.domain.*
import zio.*
import zio.json.JsonDecoder

trait TypicodeService:
  def getUser(userId: UserId): Task[User]
  def getTodos(userId: UserId): Task[List[Todo]]
  def getPosts(userId: UserId): Task[List[Post]]
  def getComments(postId: PostId): Task[List[Comment]]
  def getAlbums(userId: UserId): Task[List[Album]]
  def getPhotos(albumId: AlbumId): Task[List[Photo]]

object TypicodeService:
  def getUser(userId: UserId)     = ZIO.serviceWithZIO[TypicodeService](_.getUser(userId))
  def getTodos(userId: UserId)    = ZIO.serviceWithZIO[TypicodeService](_.getTodos(userId))
  def getPosts(userId: UserId)    = ZIO.serviceWithZIO[TypicodeService](_.getPosts(userId))
  def getComments(postId: PostId) = ZIO.serviceWithZIO[TypicodeService](_.getComments(postId))
  def getAlbums(userId: UserId)   = ZIO.serviceWithZIO[TypicodeService](_.getAlbums(userId))
  def getPhotos(albumId: AlbumId) = ZIO.serviceWithZIO[TypicodeService](_.getPhotos(albumId))

  def live: ZLayer[SttpBackend[Task, Any], Any, TypicodeService] =
    ZLayer.fromFunction(TypicodeServiceLive.apply)

case class TypicodeServiceLive(backend: SttpBackend[Task, Any]) extends TypicodeService:
  private val commonHeaders = Map("Content-Type" -> "application/json")

  // TODO get from config
  private val baseUrl = "https://jsonplaceholder.typicode.com"

  private def getUserURI(userId: UserId): Uri          = uri"$baseUrl/users/$userId"
  private def getUserTodosURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/todos"
  private def getUserPostsURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/posts"
  private def getUserAlbumURI(userId: UserId): Uri     = uri"${getUserURI(userId)}/albums"
  private def getPostCommentsURI(postId: PostId): Uri  = uri"$baseUrl/posts/$postId/comments"
  private def getAlbumPhotosURI(albumId: AlbumId): Uri = uri"$baseUrl/albums/$albumId/photos"

  private def createRequest[T](uri: Uri, lastModified: Option[String] = None)(using
      D: JsonDecoder[T]
  ): Request[Either[String, T], Any] =
    basicRequest
      .get(uri)
      .headers(lastModified.map("If-Modified-Since" -> _).foldLeft(commonHeaders)(_ + _))
      .mapResponse(_.flatMap(D.decodeJson))

  private def getObject[T](uri: Uri)(using D: JsonDecoder[T]): Task[T] =
    for
      response <- createRequest[T](uri).send(backend)
      result   <- response.code match
                    case StatusCode.Ok =>
                      response.body match
                        case Right(body) => ZIO.succeed(body)
                        case Left(error) => ZIO.fail(new Exception(error))
                    case code          =>
                      ZIO.fail(new Exception(s"Unexpected response code: $code"))
    yield result

  def getUser(userId: UserId): Task[User] =
    getObject[User](getUserURI(userId))

  def getTodos(userId: UserId): Task[List[Todo]] =
    getObject[List[Todo]](getUserTodosURI(userId))

  def getPosts(userId: UserId): Task[List[Post]] =
    getObject[List[Post]](getUserPostsURI(userId))

  def getComments(postId: PostId): Task[List[Comment]] =
    getObject[List[Comment]](getPostCommentsURI(postId))

  def getAlbums(userId: UserId): Task[List[Album]] =
    getObject[List[Album]](getUserAlbumURI(userId))

  def getPhotos(albumId: AlbumId): Task[List[Photo]] =
    getObject[List[Photo]](getAlbumPhotosURI(albumId))

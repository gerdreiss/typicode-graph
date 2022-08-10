package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class AlbumView(
    title: String,
    photos: ZQ[List[PhotoView]],
)

object AlbumView:
  case class GetUserAlbums(userId: UserId) extends Request[Throwable, List[Album]]

  private val AlbumsDS: DS[GetUserAlbums] =
    DataSource.fromFunctionZIO("AlbumsDataSource") { request =>
      TypicodeService.getUserAlbums(request.userId)
    }

  def getUserAlbums(userId: UserId): ZQ[List[AlbumView]] =
    ZQuery.fromRequest(GetUserAlbums(userId))(AlbumsDS).map {
      _.map(album =>
        AlbumView(
          album.title,
          PhotoView.getAlbumPhotos(album.id),
        )
      )
    }

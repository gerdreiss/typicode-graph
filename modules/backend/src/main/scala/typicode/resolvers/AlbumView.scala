package typicode
package resolvers

import zio.query.*

import domain.*
import services.*

case class AlbumView(
    title: String,
    photos: ZQ[List[PhotoView]],
)

object AlbumView:
  case class GetUserAlbums(userId: UserId) extends Request[Throwable, List[Album]]
  case class GetAlbum(albumId: AlbumId)    extends Request[Throwable, Album]

  private val AlbumsDS: DS[GetUserAlbums] =
    DataSource.fromFunctionZIO("AlbumsDataSource") { request =>
      TypicodeService.getUserAlbums(request.userId)
    }
  private val AlbumDS: DS[GetAlbum]       =
    DataSource.fromFunctionZIO("AlbumDataSource") { request =>
      TypicodeService.getAlbum(request.albumId)
    }

  def getUserAlbums(userId: UserId): ZQ[List[AlbumView]] =
    ZQuery
      .fromRequest(GetUserAlbums(userId))(AlbumsDS)
      .map {
        _.map(album =>
          AlbumView(
            album.title,
            PhotoView.getAlbumPhotos(album.id),
          )
        )
      }

  def getAlbum(albumId: AlbumId): ZQ[AlbumView] =
    ZQuery
      .fromRequest(GetAlbum(albumId))(AlbumDS)
      .map { album =>
        AlbumView(
          album.title,
          PhotoView.getAlbumPhotos(album.id),
        )
      }

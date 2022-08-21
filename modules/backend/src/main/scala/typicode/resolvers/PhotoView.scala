package typicode
package resolvers

import zio.query.*

import domain.*
import services.*

case class PhotoView(
    title: String,
    url: String,
    thumbnailUrl: String,
)

object PhotoView:
  case class GetAlbumPhotos(albumId: AlbumId) extends Request[Throwable, List[Photo]]

  private val AlbumPhotosDS: DS[GetAlbumPhotos] =
    DataSource.fromFunctionZIO("PhotosDataSource") { request =>
      TypicodeService.getAlbumPhotos(request.albumId)
    }

  def getAlbumPhotos(albumId: AlbumId): ZQ[List[PhotoView]] =
    ZQuery
      .fromRequest(GetAlbumPhotos(albumId))(AlbumPhotosDS)
      .map {
        _.map { photo =>
          PhotoView(photo.title, photo.url, photo.thumbnailUrl)
        }
      }

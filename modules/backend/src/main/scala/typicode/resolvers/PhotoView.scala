package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class PhotoView(
    title: String,
    url: String,
    thumbnailUrl: String,
)

object PhotoView:
  case class GetAlbumPhotos(albumId: AlbumId) extends Request[Throwable, List[Photo]]

  val ds: DS[GetAlbumPhotos] =
    DataSource.fromFunctionZIO("PhotosDataSource") { request =>
      TypicodeService.getAlbumPhotos(request.albumId)
    }

  def getAlbumPhotos(albumId: AlbumId): ZQ[List[PhotoView]] =
    ZQuery.fromRequest(GetAlbumPhotos(albumId))(ds).map {
      _.map { photo =>
        PhotoView(photo.title, photo.url, photo.thumbnailUrl)
      }
    }

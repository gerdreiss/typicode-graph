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
  case class GetPhotos(albumId: AlbumId) extends Request[Throwable, List[Photo]]

  val ds: DS[GetPhotos] =
    DataSource.fromFunctionZIO("PhotosDataSource") { request =>
      TypicodeService.getPhotos(request.albumId)
    }

  def resolve(albumId: AlbumId): ZQ[List[PhotoView]] =
    ZQuery.fromRequest(GetPhotos(albumId))(ds).map {
      _.map { photo =>
        PhotoView(photo.title, photo.url, photo.thumbnailUrl)
      }
    }

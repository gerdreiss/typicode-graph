package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class AlbumView(
    title: String,
    photos: ZQ[List[PhotoView]]
)

object AlbumView:
  case class GetAlbums(userId: UserId) extends Request[Throwable, Albums]

  private val ds: DS[GetAlbums] =
    DataSource.fromFunctionZIO("AlbumsDataSource") { request =>
      TypicodeService.getAlbums(request.userId)
    }

  def resolve(userId: UserId): ZQ[List[AlbumView]] =
    ZQuery.fromRequest(GetAlbums(userId))(ds).map {
      _.data.map(album =>
        AlbumView(
          album.title,
          PhotoView.resolve(album.id)
        )
      )
    }

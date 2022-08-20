package typicode
package domain

import zio.json.*

case class Photo(
    albumId: AlbumId,
    id: PhotoId,
    title: String,
    url: String,
    thumbnailUrl: String,
)

object Photo:
  given JsonDecoder[Photo] = DeriveJsonDecoder.gen[Photo]

case class Album(
    userId: UserId,
    id: AlbumId,
    title: String,
)

object Album:
  given JsonDecoder[Album] = DeriveJsonDecoder.gen[Album]

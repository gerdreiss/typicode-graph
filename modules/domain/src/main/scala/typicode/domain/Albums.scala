package typicode.domain

import zio.json.*

case class Photo(
    albumId: AlbumId,
    id: PhotoId,
    title: String,
    url: String,
    thumbnailUrl: String,
  )

case class Photos(
    data: List[Photo]
  ) extends TypicodeData

object Photos:
  given JsonDecoder[Photos] = DeriveJsonDecoder.gen[Photos]
  given JsonDecoder[Photo]  = DeriveJsonDecoder.gen[Photo]

case class Album(
    userId: UserId,
    id: AlbumId,
    title: String,
  )

case class Albums(
    data: List[Album]
  ) extends TypicodeData

object Albums:
  given JsonDecoder[Albums] = DeriveJsonDecoder.gen[Albums]
  given JsonDecoder[Album]  = DeriveJsonDecoder.gen[Album]

package typicode.domain

import zio.json.*

case class Comment(
    postId: PostId,
    id: CommentId,
    name: String,
    email: String,
    body: String,
  )

case class Comments(
    data: List[Comment]
  ) extends TypicodeData

object Comments:
  given JsonDecoder[Comments] = DeriveJsonDecoder.gen[Comments]
  given JsonDecoder[Comment]  = DeriveJsonDecoder.gen[Comment]

case class Post(
    userId: UserId,
    id: PostId,
    title: String,
    body: String,
  )

case class Posts(
    data: List[Post]
  ) extends TypicodeData

object Posts:
  given JsonDecoder[Posts] = DeriveJsonDecoder.gen[Posts]
  given JsonDecoder[Post]  = DeriveJsonDecoder.gen[Post]

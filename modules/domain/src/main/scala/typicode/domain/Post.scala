package typicode
package domain

import zio.json.*

case class Comment(
    postId: PostId,
    id: CommentId,
    name: String,
    email: String,
    body: String,
)

object Comment:
  given JsonDecoder[Comment] = DeriveJsonDecoder.gen[Comment]

case class Post(
    userId: UserId,
    id: PostId,
    title: String,
    body: String,
)

object Post:
  given JsonDecoder[Post] = DeriveJsonDecoder.gen[Post]

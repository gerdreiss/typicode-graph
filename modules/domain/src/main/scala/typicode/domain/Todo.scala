package typicode
package domain

import zio.json.*

case class Todo(
    userId: UserId,
    id: PostId,
    title: String,
    completed: Boolean,
)

object Todo:
  given JsonDecoder[Todo] = DeriveJsonDecoder.gen[Todo]

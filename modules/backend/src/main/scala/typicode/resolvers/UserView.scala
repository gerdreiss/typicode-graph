package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class UserView(
    name: String,
    username: String,
    email: String,
    phone: String,
    website: String,
    address: Address,
    company: Company,
    todos: ZQ[List[TodoView]],
    posts: ZQ[List[PostView]],
    albums: ZQ[List[AlbumView]],
  )

object UserView:
  def resolve(userId: UserId): ZQ[UserView] =
    ZQuery
      .fromZIO(TypicodeService.getUser(userId))
      .map { user =>
        UserView(
          user.name,
          user.username,
          user.email,
          user.phone,
          user.website,
          user.address,
          user.company,
          TodoView.resolve(user.id),
          PostView.resolve(user.id),
          AlbumView.resolve(user.id),
        )
      }

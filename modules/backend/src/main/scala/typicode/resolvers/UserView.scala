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
  def getUsers(username: Option[String]) =
    ZQuery
      .fromZIO(TypicodeService.getUsers(username))
      .map(_.map(mapUser))

  def getUser(userId: UserId): ZQ[UserView] =
    ZQuery
      .fromZIO(TypicodeService.getUser(userId))
      .map(mapUser)

  private def mapUser(user: User): UserView =
    UserView(
      user.name,
      user.username,
      user.email,
      user.phone,
      user.website,
      user.address,
      user.company,
      TodoView.getUserTodos(user.id),
      PostView.getUserPosts(user.id),
      AlbumView.getUserAlbums(user.id),
    )

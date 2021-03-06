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
  case class GetUsers(username: Option[String]) extends Request[Throwable, List[User]]
  case class GetUser(userId: UserId)            extends Request[Throwable, User]

  val UsersDS: DS[GetUsers] =
    DataSource.fromFunctionZIO("UsersDataSource") { request =>
      TypicodeService.getUsers(request.username)
    }
  val UserDS: DS[GetUser]   =
    DataSource.fromFunctionZIO("UserDataSource") { request =>
      TypicodeService.getUser(request.userId)
    }

  def getUsers(username: Option[String]) =
    ZQuery
      .fromRequest(GetUsers(username))(UsersDS)
      .map(_.map(mapUser))

  def getUser(userId: UserId): ZQ[UserView] =
    ZQuery.fromRequest(GetUser(userId))(UserDS).map(mapUser)

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

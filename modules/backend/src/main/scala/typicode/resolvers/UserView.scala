package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class UserView(
    id: UserId,
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
  case class GetUsers()              extends Request[Throwable, List[User]]
  case class GetUser(userId: UserId) extends Request[Throwable, User]

  private val UsersDS: DS[GetUsers] =
    DataSource.fromFunctionZIO("UsersDataSource") { _ =>
      TypicodeService.getUsers
    }
  private val UserDS: DS[GetUser]   =
    DataSource.fromFunctionZIO("UserDataSource") { request =>
      TypicodeService.getUser(request.userId)
    }

  def getUsers: ZQ[List[UserView]] =
    ZQuery
      .fromRequest(GetUsers())(UsersDS)
      .map(_.map(mapUser))

  def getUser(userId: UserId): ZQ[UserView] =
    ZQuery
      .fromRequest(GetUser(userId))(UserDS)
      .map(mapUser)

  private def mapUser(user: User): UserView =
    UserView(
      user.id,
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

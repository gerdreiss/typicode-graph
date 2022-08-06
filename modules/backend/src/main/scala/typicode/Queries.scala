package typicode

import caliban.schema.Annotations.GQLDescription

import domain.*
import resolvers.*

case class UserIdArgs(userId: UserId)
case class Queries(
    @GQLDescription("Return users")
    users: ZQ[List[UserView]],
    @GQLDescription("Return user")
    user: UserIdArgs => ZQ[UserView],
    @GQLDescription("Return user todos")
    userTodos: UserIdArgs => ZQ[List[TodoView]],
    @GQLDescription("Return user posts")
    userPosts: UserIdArgs => ZQ[List[PostView]],
    @GQLDescription("Return user albums")
    userAlbums: UserIdArgs => ZQ[List[AlbumView]],
)

object Queries:
  val make: Queries = Queries(
    UserView.getUsers,
    user => UserView.getUser(user.userId),
    userTodos => TodoView.getUserTodos(userTodos.userId),
    userPosts => PostView.getUserPosts(userPosts.userId),
    userAlbums => AlbumView.getUserAlbums(userAlbums.userId),
  )

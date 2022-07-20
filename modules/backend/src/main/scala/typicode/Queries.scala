package typicode

import caliban.schema.Annotations.GQLDescription
import zio.query.*

import typicode.domain.*
import typicode.resolvers.*

case class UserQueryArgs(username: Option[String])
case class UserIdArgs(userId: UserId)
case class Queries(
    @GQLDescription("Return users")
    users: UserQueryArgs => ZQ[List[UserView]],
    @GQLDescription("Return user")
    user: UserIdArgs => ZQ[UserView],
    @GQLDescription("Return user todos")
    userTodos: UserIdArgs => ZQ[List[TodoView]],
    @GQLDescription("Return user posts")
    userPosts: UserIdArgs => ZQ[List[PostView]],
)

object Queries:
  val make: Queries = Queries(
    users => UserView.getUsers(users.username),
    user => UserView.getUser(user.userId),
    userTodos => TodoView.getUserTodos(userTodos.userId),
    userPosts => PostView.getUserPosts(userPosts.userId),
  )

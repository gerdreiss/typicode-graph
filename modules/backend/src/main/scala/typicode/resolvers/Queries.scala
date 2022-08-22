package typicode
package resolvers

import caliban.schema.Annotations.GQLDescription

import domain.*

case class UserIdArgs(userId: UserId)
case class PostIdArgs(postId: PostId)
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
    @GQLDescription("Return post")
    post: PostIdArgs => ZQ[PostView],
    @GQLDescription("Return post comments")
    postComments: PostIdArgs => ZQ[List[CommentView]],
)

object Queries:
  lazy val make: Queries = Queries(
    UserView.getUsers,
    user => UserView.getUser(user.userId),
    userTodos => TodoView.getUserTodos(userTodos.userId),
    userPosts => PostView.getUserPosts(userPosts.userId),
    userAlbums => AlbumView.getUserAlbums(userAlbums.userId),
    post => PostView.getPost(post.postId),
    postComments => CommentView.getPostComments(postComments.postId),
  )

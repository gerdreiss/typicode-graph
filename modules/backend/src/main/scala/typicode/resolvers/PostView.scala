package typicode
package resolvers

import zio.query.*

import domain.*
import services.*

case class PostView(
    id: PostId,
    userId: UserId,
    title: String,
    body: String,
    comments: ZQ[List[CommentView]],
)

object PostView:
  case class GetUserPosts(userId: UserId) extends Request[Throwable, List[Post]]
  case class GetPost(postId: PostId)      extends Request[Throwable, Post]

  private val PostsDS: DS[GetUserPosts] =
    DataSource.fromFunctionZIO("PostsDataSource") { request =>
      TypicodeService.getUserPosts(request.userId)
    }
  private val PostDS: DS[GetPost]       =
    DataSource.fromFunctionZIO("PostDataSource") { request =>
      TypicodeService.getPost(request.postId)
    }

  def getUserPosts(userId: UserId): ZQ[List[PostView]] =
    ZQuery
      .fromRequest(GetUserPosts(userId))(PostsDS)
      .map(_.map(mapPost))

  def getPost(postId: PostId): ZQ[PostView] =
    ZQuery
      .fromRequest(GetPost(postId))(PostDS)
      .map(mapPost)

  private def mapPost(post: Post) =
    PostView(
      post.id,
      post.userId,
      post.title,
      post.body,
      CommentView.getPostComments(post.id),
    )

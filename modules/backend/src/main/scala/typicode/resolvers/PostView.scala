package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class PostView(
    id: PostId,
    userId: UserId,
    title: String,
    body: String,
    comments: ZQ[List[CommentView]],
)

object PostView:
  case class GetUserPosts(userId: UserId) extends Request[Throwable, List[Post]]

  private val PostsDS: DS[GetUserPosts] =
    DataSource.fromFunctionZIO("PostsDataSource") { request =>
      TypicodeService.getUserPosts(request.userId)
    }

  def getUserPosts(userId: UserId): ZQ[List[PostView]] =
    ZQuery.fromRequest(GetUserPosts(userId))(PostsDS).map {
      _.map { post =>
        PostView(
          post.id,
          post.userId,
          post.title,
          post.body,
          CommentView.getPostComments(post.id),
        )
      }
    }

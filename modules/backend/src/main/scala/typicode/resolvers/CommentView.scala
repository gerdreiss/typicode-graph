package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class CommentView(
    name: String,
    email: String,
    body: String,
)

object CommentView:
  case class GetPostComments(postId: PostId) extends Request[Throwable, List[Comment]]

  private val ds: DS[GetPostComments] =
    DataSource.fromFunctionZIO("CommentsDataSource") { request =>
      TypicodeService.getPostComments(request.postId)
    }

  def getPostComments(postId: PostId): ZQ[List[CommentView]] =
    ZQuery.fromRequest(GetPostComments(postId))(ds).map {
      _.map { comment =>
        CommentView(comment.name, comment.email, comment.body)
      }
    }

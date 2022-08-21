package typicode
package resolvers

import zio.query.*

import domain.*
import services.*

case class CommentView(
    postId: PostId,
    id: CommentId,
    name: String,
    email: String,
    body: String,
)

object CommentView:
  case class GetPostComments(postId: PostId) extends Request[Throwable, List[Comment]]

  private val PostCommentsDS: DS[GetPostComments] =
    DataSource.fromFunctionZIO("CommentsDataSource") { request =>
      TypicodeService.getPostComments(request.postId)
    }

  def getPostComments(postId: PostId): ZQ[List[CommentView]] =
    ZQuery
      .fromRequest(GetPostComments(postId))(PostCommentsDS)
      .map {
        _.map { comment =>
          CommentView(comment.postId, comment.id, comment.name, comment.email, comment.body)
        }
      }

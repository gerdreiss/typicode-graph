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
  case class GetComments(postId: PostId) extends Request[Throwable, Comments]

  private val ds: DS[GetComments] =
    DataSource.fromFunctionZIO("CommentsDataSource") { request =>
      TypicodeService.getComments(request.postId)
    }

  def resolve(postId: PostId): ZQ[List[CommentView]] =
    ZQuery.fromRequest(GetComments(postId))(ds).map {
      _.data.map { comment =>
        CommentView(comment.name, comment.email, comment.body)
      }
    }

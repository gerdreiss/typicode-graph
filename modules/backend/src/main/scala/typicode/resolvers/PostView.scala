package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class PostView(
    title: String,
    body: String,
    comments: ZQ[List[CommentView]],
  )

object PostView:
  case class GetPosts(userId: UserId) extends Request[Throwable, Posts]

  val ds: DS[GetPosts] =
    DataSource.fromFunctionZIO("PostsDataSource") { request =>
      TypicodeService.getPosts(request.userId)
    }

  def resolve(userId: UserId): ZQ[List[PostView]] =
    ZQuery.fromRequest(GetPosts(userId))(ds).map {
      _.data.map { post =>
        PostView(
          post.title,
          post.body,
          CommentView.resolve(post.id),
        )
      }
    }

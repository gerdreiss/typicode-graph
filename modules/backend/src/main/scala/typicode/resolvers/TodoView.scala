package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class TodoView(
    id: TodoId,
    userId: UserId,
    title: String,
    completed: Boolean,
)

object TodoView:
  case class GetUserTodos(userId: UserId) extends Request[Throwable, List[Todo]]

  val ds: DS[GetUserTodos] =
    DataSource.fromFunctionZIO("TodosDataSource") { request =>
      TypicodeService.getUserTodos(request.userId)
    }

  def getUserTodos(userId: UserId): ZQ[List[TodoView]] =
    ZQuery.fromRequest(GetUserTodos(userId))(ds).map {
      _.map { todo =>
        TodoView(todo.id, todo.userId, todo.title, todo.completed)
      }
    }

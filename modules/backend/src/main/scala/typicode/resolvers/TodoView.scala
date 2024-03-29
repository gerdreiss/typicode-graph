package typicode
package resolvers

import zio.query.*

import domain.*
import services.*

case class TodoView(
    id: TodoId,
    userId: UserId,
    title: String,
    completed: Boolean,
)

object TodoView:
  case class GetUserTodos(userId: UserId) extends Request[Throwable, List[Todo]]

  private val TodosDS: DS[GetUserTodos] =
    DataSource.fromFunctionZIO("TodosDataSource") { request =>
      TypicodeService.getUserTodos(request.userId)
    }

  def getUserTodos(userId: UserId): ZQ[List[TodoView]] =
    ZQuery
      .fromRequest(GetUserTodos(userId))(TodosDS)
      .map {
        _.map { todo =>
          TodoView(todo.id, todo.userId, todo.title, todo.completed)
        }
      }

package typicode.resolvers

import zio.query.*

import typicode.domain.*
import typicode.services.*

case class TodoView(
    title: String,
    completed: Boolean,
  )

object TodoView:
  case class GetTodos(userId: UserId) extends Request[Throwable, Todos]

  val ds: DS[GetTodos] =
    DataSource.fromFunctionZIO("TodosDataSource") { request =>
      TypicodeService.getTodos(request.userId)
    }

  def resolve(userId: UserId): ZQ[List[TodoView]] =
    ZQuery.fromRequest(GetTodos(userId))(ds).map {
      _.data.map { todo =>
        TodoView(todo.title, todo.completed)
      }
    }

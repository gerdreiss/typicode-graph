package typicode

import com.raquo.laminar.api.L.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success

import Vars.*

object Commands:

  enum Command:
    case ShowAllUsers
    case ShowUser(userId: Int)
    case ShowUserTodos(userId: Int)
    case ShowUserPosts(userId: Int)

  val commandObserver: Observer[Command] =
    Observer[Command] {
      case Command.ShowAllUsers          =>
        Client
          .getUsers
          .onComplete {
            case Success(Right(Some(users))) => updateVars(users = users)
            case Success(Right(None))        => updateVars(header = "No users returned")
            case Success(Left(error))        => updateVars(header = error.getMessage)
            case Failure(error)              => updateVars(header = error.getMessage)
          }
      case Command.ShowUser(userId)      =>
        Client
          .getUser(userId)
          .onComplete {
            case Success(Right(Some(user))) => updateVars(header = user.name, user = Some(user))
            case Success(Right(None))       => updateVars(header = "No user returned")
            case Success(Left(error))       => updateVars(header = error.getMessage)
            case Failure(error)             => updateVars(header = error.getMessage)
          }
      case Command.ShowUserTodos(userId) =>
        Client
          .getUserTodos(userId)
          .onComplete {
            case Success(Right(Some(todos))) => userTodosVar.set(todos)
            case Success(Right(None))        => userTodosVar.set(List.empty)
            case Success(Left(error))        => updateVars(header = error.getMessage)
            case Failure(error)              => updateVars(header = error.getMessage)
          }
      case Command.ShowUserPosts(userId) =>
        Client
          .getUserPosts(userId)
          .onComplete {
            case Success(Right(Some(posts))) => userPostsVar.set(posts)
            case Success(Right(None))        => userPostsVar.set(List.empty)
            case Success(Left(error))        => updateVars(header = error.getMessage)
            case Failure(error)              => updateVars(header = error.getMessage)
          }
    }

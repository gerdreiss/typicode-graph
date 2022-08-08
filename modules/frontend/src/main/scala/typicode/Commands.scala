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

  val commandObserver = Observer[Command] {
    case Command.ShowAllUsers          =>
      Client
        .getUsers
        .onComplete {
          case Success(Right(Some(users))) =>
            org.scalajs.dom.console.log(s"Successfully got users: $users")
            headerVar.set("Users")
            usersVar.set(users)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Success(Right(None))        =>
            org.scalajs.dom.console.log("No users found")
            headerVar.set("No users returned")
            usersVar.set(List.empty)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Success(Left(error))        =>
            org.scalajs.dom.console.log(s"Error getting users: $error")
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Failure(error)              =>
            org.scalajs.dom.console.log(s"Error getting users: $error")
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
        }
    case Command.ShowUser(userId)      =>
      Client
        .getUser(userId)
        .onComplete {
          case Success(Right(Some(user))) =>
            headerVar.set(user.name)
            usersVar.set(List.empty)
            userVar.set(Some(user))
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Success(Right(None))       =>
            headerVar.set("No user returned")
            usersVar.set(List.empty)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Success(Left(error))       =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Failure(error)             =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userVar.set(None)
            userTodosVar.set(List.empty)
            userPostsVar.set(List.empty)
        }
    case Command.ShowUserTodos(userId) =>
      Client
        .getUserTodos(userId)
        .onComplete {
          case Success(Right(Some(todos))) =>
            usersVar.set(List.empty)
            userTodosVar.set(todos)
          case Success(Right(None))        =>
            usersVar.set(List.empty)
            userTodosVar.set(List.empty)
          case Success(Left(error))        =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userTodosVar.set(List.empty)
          case Failure(error)              =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userTodosVar.set(List.empty)
        }
    case Command.ShowUserPosts(userId) =>
      Client
        .getUserPosts(userId)
        .onComplete {
          case Success(Right(Some(posts))) =>
            usersVar.set(List.empty)
            userPostsVar.set(posts)
          case Success(Right(None))        =>
            usersVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Success(Left(error))        =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userPostsVar.set(List.empty)
          case Failure(error)              =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
            userPostsVar.set(List.empty)
        }
  }

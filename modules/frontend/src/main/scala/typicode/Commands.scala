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

  val commandObserver = Observer[Command] {
    case Command.ShowAllUsers     =>
      Client
        .getUsers
        .onComplete {
          case Success(Right(Some(users))) =>
            org.scalajs.dom.console.log(s"Successfully got users: $users")
            headerVar.set("Users")
            usersVar.set(users)
          case Success(Right(None))        =>
            org.scalajs.dom.console.log("No users found")
            headerVar.set("No users returned")
            usersVar.set(List.empty)
          case Success(Left(error))        =>
            org.scalajs.dom.console.log(s"Error getting users: $error")
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
          case Failure(error)              =>
            org.scalajs.dom.console.log(s"Error getting users: $error")
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
        }
    case Command.ShowUser(userId) =>
      Client
        .getUser(userId)
        .onComplete {
          case Success(Right(Some(user))) =>
            headerVar.set("Users")
            usersVar.set(List(user))
          case Success(Right(None))       =>
            headerVar.set("No user returned")
            usersVar.set(List.empty)
          case Success(Left(error))       =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
          case Failure(error)             =>
            headerVar.set(error.getMessage)
            usersVar.set(List.empty)
        }
  }

package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement

import domain.*

object Vars:
  val headerVar: Var[String]              = Var("Users")
  val usersVar: Var[List[User]]           = Var(List.empty)
  val userVar: Var[Option[User]]          = Var(None)
  val userTodosVar: Var[List[Todo]]       = Var(List.empty)
  val userPostsVar: Var[List[Post]]       = Var(List.empty)
  val postVar: Var[Option[Post]]          = Var(None)
  var postCommentsVar: Var[List[Comment]] = Var(List.empty)

  def updateVars(
      header: String = "Users",
      users: List[User] = List.empty,
      user: Option[User] = None,
      userTodos: List[Todo] = List.empty,
      userPosts: List[Post] = List.empty,
      post: Option[Post] = None,
      postComments: List[Comment] = List.empty,
  ): Unit =
    headerVar.set(header)
    usersVar.set(users)
    userVar.set(user)
    userTodosVar.set(userTodos)
    userPostsVar.set(userPosts)
    postVar.set(post)
    postCommentsVar.set(postComments)

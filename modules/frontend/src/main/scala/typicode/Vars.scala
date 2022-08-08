package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement

import domain.*

object Vars:
  val headerVar: Var[String]        = Var("Users")
  val usersVar: Var[List[User]]     = Var(List.empty)
  val userVar: Var[Option[User]]    = Var(None)
  val userTodosVar: Var[List[Todo]] = Var(List.empty)
  val userPostsVar: Var[List[Post]] = Var(List.empty)

  val combinedSignals = usersVar.signal.combineWith(userVar.signal)

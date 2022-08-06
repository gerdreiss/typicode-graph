package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement

import domain.*

object Vars:
  val headerVar: Var[String]    = Var("Users")
  val usersVar: Var[List[User]] = Var(List.empty)

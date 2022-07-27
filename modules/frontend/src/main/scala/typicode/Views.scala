package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement
import typicode.domain.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success

enum Command:
  case ShowAllUsers
  case ShowUser(userId: Int)

object Views:
  val defaultHeader = div(cls := "content", p("Users"))

  val headerVar: Var[ReactiveHtmlElement[HTMLElement]] = Var(defaultHeader)
  val usersVar: Var[List[User]]                        = Var(List.empty)

  val commandObserver = Observer[Command] {
    case Command.ShowAllUsers     =>
      Client
        .getUsers
        .onComplete {
          case Success(Right(Some(users))) =>
            headerVar.set(defaultHeader)
            usersVar.set(users)
          case Success(Right(None))        =>
            headerVar.set(div(cls := "content", p("No users returned")))
            usersVar.set(List.empty)
          case Success(Left(error))        =>
            headerVar.set(div(cls := "content", p(error.getMessage)))
            usersVar.set(List.empty)
          case Failure(error)              =>
            headerVar.set(div(cls := "content", p(error.getMessage)))
            usersVar.set(List.empty)
        }
    case Command.ShowUser(userId) =>
      Client
        .getUser(userId)
        .onComplete {
          case Success(Right(Some(user))) =>
            headerVar.set(defaultHeader)
            usersVar.set(List(user))
          case Success(Right(None))       =>
            headerVar.set(div(cls := "content", p("No user returned")))
            usersVar.set(List.empty)
          case Success(Left(error))       =>
            headerVar.set(div(cls := "content", p(error.getMessage)))
            usersVar.set(List.empty)
          case Failure(error)             =>
            headerVar.set(div(cls := "content", p(error.getMessage)))
            usersVar.set(List.empty)
        }
  }

  def renderApp: ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui raised very padded container segment",
      h1(
        cls   := "ui header",
        i(cls   := "circular users icon"),
        div(cls := "content", "Users"),
      ),
      div(cls := "ui divider"),
      children <-- usersVar.signal.map(renderUserList),
    )

  def renderUserList(users: List[User]): List[ReactiveHtmlElement[HTMLElement]] =
    users.map { user =>
      div(
        cls := "ui grid",
        div(cls := "four wide column", renderUserCard(user)),
        div(cls := "three wide column", renderAddressCard(user.address)),
        div(cls := "three wide column", renderGeoCard(user.address.geo)),
        div(cls := "six wide column", renderCompanyCard(user.company)),
      )
    }

  def renderUserCard(user: User): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui card",
      div(
        cls := "content",
        div(
          cls   := "header",
          a(user.name),
          onClick.mapTo(user.id) --> commandObserver.contramap[Int] { userId =>
            Command.ShowUser(userId)
          },
        ),
        div(cls := "description", i(cls := "envelope icon"), user.email),
        div(cls := "description", i(cls := "phone icon"), user.phone),
        div(cls := "description", i(cls := "globe icon"), user.website),
        br(),
      ),
    )

  def renderAddressCard(address: Address): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui card",
      div(
        cls := "content",
        div(cls := "header", i(cls := "address book icon"), "Address"),
        div(cls := "description", address.street),
        div(cls := "description", address.suite),
        div(cls := "description", address.city),
        div(cls := "description", address.zipcode),
      ),
    )

  def renderGeoCard(geo: Geo): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui card",
      div(
        cls := "content",
        div(cls := "header", i(cls := "location arrow icon"), "Position"),
        div(cls := "description", geo.lat),
        div(cls := "description", geo.lng),
        br(),
        br(),
      ),
    )

  def renderCompanyCard(company: Company): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui card",
      div(
        cls := "content",
        div(cls := "header", i(cls := "building icon"), "Company"),
        div(cls := "description", company.name),
        div(cls := "description", company.catchPhrase),
        div(cls := "description", company.bs),
        br(),
      ),
    )

package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement

import domain.*
import Commands.*
import Vars.*

object Views:

  def renderApp: ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui raised very padded container segment",
      h1(
        cls   := "ui header",
        i(cls   := "circular users icon"),
        div(cls := "content", child <-- headerVar.signal.map(p(_))),
      ),
      div(cls := "ui divider"),
      children <-- usersVar.signal.map(renderUserList),
      onMountCallback { _ =>
        commandObserver.onNext(Command.ShowAllUsers)
      },
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

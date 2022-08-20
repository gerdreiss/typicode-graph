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
      renderHeader,
      div(cls := "ui divider"),
      children <-- usersVar
        .signal
        .combineWith(userVar.signal)
        .map {
          case (users, None)   => renderUserList(users)
          case (_, Some(user)) => renderUserDetails(user)
        },
      onMountCallback { _ =>
        commandObserver.onNext(Command.ShowAllUsers)
      },
    )

  def renderHeader: ReactiveHtmlElement[HTMLElement] =
    h1(
      cls := "ui header",
      i(cls := "circular users icon"),
      div(
        cls := "content",
        div(
          cls := "ui grid",
          div(
            cls := "row",
            div(cls := "fourteen wide column", child <-- headerVar.signal.map(p(_))),
            child <-- userVar.signal.map {
              _.fold(div()) { _ =>
                div(
                  cls := "two wide column",
                  button(
                    cls := "ui labeled button",
                    i(cls := "left arrow icon"),
                    "Back",
                    onClick.mapTo(()) --> commandObserver.contramap(_ => Command.ShowAllUsers),
                  ),
                )
              }
            },
          ),
        ),
      ),
    )

  def renderUserList(users: List[User]): List[ReactiveHtmlElement[HTMLElement]] =
    users.map { user =>
      div(
        cls := "ui grid",
        div(cls := "four wide column", renderClickableUserCard(user)),
        div(cls := "three wide column", renderAddressCard(user.address)),
        div(cls := "three wide column", renderGeoCard(user.address.geo)),
        div(cls := "six wide column", renderCompanyCard(user.company)),
      )
    }

  def renderUserDetails(user: User): List[ReactiveHtmlElement[HTMLElement]] =
    div(
      cls := "ui grid",
      div(
        cls := "five wide column",
        renderUserCard(user),
        renderAddressCard(user.address),
        renderGeoCard(user.address.geo),
        renderCompanyCard(user.company),
      ),
      div(
        cls := "five wide column",
        h3(cls := "ui header", div(cls := "content", i(cls := "list icon"), p("To-Do List"))),
        div(
          cls  := "ui relaxed divided list",
          children <-- userTodosVar.signal.map(renderTodoList),
          onMountCallback { _ =>
            commandObserver.onNext(Command.ShowUserTodos(user.id))
          },
        ),
      ),
      div(
        cls := "five wide column",
        h3(cls := "ui header", div(cls := "content", i(cls := "edit icon"), p("Posts"))),
        div(
          cls  := "ui relaxed divided list",
          children <-- userPostsVar.signal.map(renderPostList),
          onMountCallback { _ =>
            commandObserver.onNext(Command.ShowUserPosts(user.id))
          },
        ),
      ),
    ) :: Nil

  def renderClickableUserCard(user: User): ReactiveHtmlElement[HTMLElement] =
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

  def renderUserCard(user: User): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui card",
      div(
        cls := "content",
        div(cls := "header", user.name),
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

  def renderTodoList(todos: List[Todo]): List[ReactiveHtmlElement[HTMLElement]] =
    todos.map { todo =>
      div(
        cls := "item",
        if todo.completed then i(cls := "check icon")
        else i(cls                   := "square outline icon"),
        div(cls := "content", div(cls := "description", todo.title)),
      )
    }

  def renderPostList(posts: List[Post]): List[ReactiveHtmlElement[HTMLElement]] =
    posts.map { post =>
      div(
        cls := "item",
        i(cls := "edit icon"),
        div(
          cls := "content",
          a(cls   := "header", a(post.title)),
          div(cls := "description", p(post.body)),
        ),
      )
    }

package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement

import Commands.*
import Vars.*
import domain.*

object UI:

  def renderApp: ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui raised very padded container segment",
      renderHeader,
      div(cls := "ui divider"),
      children <-- usersVar.signal
        .combineWith(nameFilterVar.signal)
        .combineWith(userVar.signal)
        .combineWith(postVar.signal)
        .map {
          case (users, filter, None, None) => renderUserList(users, filter)
          case (_, _, Some(user), None)    => renderUserDetails(user)
          case (_, _, _, Some(post))       => renderPostDetails(post)
        },
      onMountCallback { _ =>
        commandObserver.onNext(Command.ShowAllUsers)
      },
    )

  def renderHeader: ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui  grid",
      div(
        cls := "ten wide column",
        h1(
          cls := "ui header",
          i(cls := "circular users icon"),
          div(
            cls := "content",
            child <-- headerVar.signal.map(p(_)),
          ),
        ),
      ),
      div(
        cls := "six wide column",
        child <-- usersVar.signal
          .combineWith(userVar.signal)
          .combineWith(postVar.signal)
          .map {
            case (_, None, None)      => renderUserSearch
            case (_, maybeUser, None) => renderBackToUsers(maybeUser)
            case (_, _, maybePost)    => renderBackToUser(maybePost)
          },
      ),
    )

  def renderUserSearch: ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "content",
      styleAttr := "margin-top: 1em",
      div(
        cls := "ui fluid left icon large input",
        i(cls := "users icon"),
        input(
          onMountFocus,
          placeholder := "Enter name here",
          onInput.mapToValue --> nameFilterVar,
        ),
      ),
    )

  def renderBackToUsers(maybeUser: Option[User]): ReactiveHtmlElement[HTMLElement] =
    maybeUser.fold(div()) { _ =>
      div(
        cls := "content",
        styleAttr := "margin-top: 2em; text-align: right",
        button(
          cls := "ui labeled button",
          i(cls := "left arrow icon"),
          "Back",
          onClick.mapTo(()) --> commandObserver.contramap(_ => Command.ShowAllUsers),
        ),
      )
    }

  def renderBackToUser(maybePost: Option[Post]): ReactiveHtmlElement[HTMLElement] =
    maybePost.fold(div()) { post =>
      div(
        cls := "content",
        styleAttr := "margin-top: 2em; text-align: right",
        button(
          cls := "ui labeled button",
          i(cls := "left arrow icon"),
          "Back",
          onClick.mapTo(post.userId) --> commandObserver.contramap(userId => Command.ShowUser(userId)),
        ),
      )
    }

  def renderUserList(users: List[User], filter: String = ""): List[ReactiveHtmlElement[HTMLElement]] =
    users
      .filter(u => filter.isEmpty || u.name.contains(filter) || u.username.contains(filter))
      .map { user =>
        div(
          cls := "ui grid",
          div(cls := "four wide column", renderClickableUserCard(user)),
          div(cls := "three wide column", renderAddressCard(user.address)),
          div(cls := "three wide column", renderGeoCard(user.address.geo)),
          div(cls := "six wide column", renderCompanyCard(user.company, fluid = true)),
        )
      }

  def renderClickableUserCard(user: User): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui card",
      div(
        cls := "content",
        div(
          cls := "header",
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
      renderTodos(user.id),
      renderPosts(user.id),
    ) :: Nil

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

  def renderCompanyCard(company: Company, fluid: Boolean = false): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := (if fluid then "ui fluid card" else "ui card"),
      div(
        cls := "content",
        div(cls := "header", i(cls := "building icon"), "Company"),
        div(cls := "description", company.name),
        div(cls := "description", company.catchPhrase),
        div(cls := "description", company.bs),
        br(),
      ),
    )

  def renderTodos(userId: UserId): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "five wide column",
      h3(cls := "ui header", div(cls := "content", i(cls := "list icon"), "To-Do List")),
      div(
        cls := "ui relaxed divided list",
        children <-- userTodosVar.signal.map(renderTodoList),
        onMountCallback { _ =>
          commandObserver.onNext(Command.ShowUserTodos(userId))
        },
      ),
    )

  def renderTodoList(todos: List[Todo]): List[ReactiveHtmlElement[HTMLElement]] =
    todos.map { todo =>
      div(
        cls := "item",
        if todo.completed then i(cls := "check icon")
        else i(cls := "square outline icon"),
        div(cls := "content", div(cls := "description", todo.title)),
      )
    }

  def renderPosts(userId: UserId): ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "six wide column",
      h3(cls := "ui header", div(cls := "content", i(cls := "edit icon"), "Posts")),
      div(
        cls := "ui relaxed divided list",
        children <-- userPostsVar.signal.map(renderPostList),
        onMountCallback { _ =>
          commandObserver.onNext(Command.ShowUserPosts(userId))
        },
      ),
    )

  def renderPostList(posts: List[Post]): List[ReactiveHtmlElement[HTMLElement]] =
    posts.map { post =>
      div(
        cls := "item",
        i(cls := "edit icon"),
        div(
          cls := "content",
          a(
            cls := "header",
            a(
              post.title,
              onClick.mapTo(post.id) --> commandObserver.contramap(postId => Command.ShowPost(postId)),
            ),
          ),
          div(cls := "description", post.body),
        ),
      )
    }

  def renderPostDetails(post: Post): List[ReactiveHtmlElement[HTMLElement]] =
    div(
      cls := "ui grid",
      div(
        cls := "sixteen wide column",
        div(
          cls := "ui card",
          styleAttr := "width: 100%",
          div(
            cls := "content",
            div(cls := "ui header", i(cls := "edit icon"), post.title),
            div(cls := "description", b(post.body)),
          ),
        ),
        div(
          cls := "ui comments",
          h3(cls := "ui dividing header", "Comments"),
          children <-- postCommentsVar.signal.map(renderCommentList),
          onMountCallback { _ =>
            commandObserver.onNext(Command.ShowPostComments(post.id))
          },
        ),
      ),
    ) :: Nil

  def renderCommentList(comments: List[Comment]): List[ReactiveHtmlElement[HTMLElement]] =
    comments.map { comment =>
      div(
        cls := "comment",
        i(cls := "large comment top aligned icon avatar"),
        div(
          cls := "content",
          a(cls := "author", comment.name),
          div(cls := "metadata", i(cls := "envelope icon"), comment.email),
          div(cls := "text", comment.body),
        ),
      )
    }

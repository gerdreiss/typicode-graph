package typicode

import caliban.client.ArgEncoder
import caliban.client.Argument
import caliban.client.FieldBuilder.*
import caliban.client.Operations.RootQuery
import caliban.client.SelectionBuilder
import caliban.client.SelectionBuilder.*
import sttp.client3.*
import typicode.domain.*
import cats.implicits.*

object Client:

  type GeoView
  object GeoView:
    def lat: SelectionBuilder[GeoView, Double] = Field("lat", Scalar())
    def lng: SelectionBuilder[GeoView, Double] = Field("lng", Scalar())

  type AddressView
  object AddressView:
    def street: SelectionBuilder[AddressView, String]  = Field("street", Scalar())
    def suite: SelectionBuilder[AddressView, String]   = Field("suite", Scalar())
    def city: SelectionBuilder[AddressView, String]    = Field("city", Scalar())
    def zipcode: SelectionBuilder[AddressView, String] = Field("zipcode", Scalar())

    def geo[A](innerSelection: SelectionBuilder[GeoView, A]): SelectionBuilder[AddressView, A] =
      Field("geo", Obj(innerSelection))

  type CompanyView
  object CompanyView:
    def name: SelectionBuilder[CompanyView, String]        = Field("name", Scalar())
    def catchPhrase: SelectionBuilder[CompanyView, String] = Field("catchPhrase", Scalar())
    def bs: SelectionBuilder[CompanyView, String]          = Field("bs", Scalar())

  type TodoView
  object TodoView:
    def title: SelectionBuilder[TodoView, String]      = Field("title", Scalar())
    def completed: SelectionBuilder[TodoView, Boolean] = Field("completed", Scalar())

  type CommentView
  object CommentView:
    def name: SelectionBuilder[CommentView, String]  = Field("name", Scalar())
    def email: SelectionBuilder[CommentView, String] = Field("email", Scalar())
    def body: SelectionBuilder[CommentView, String]  = Field("body", Scalar())

  type PostView
  object PostView:
    def title: SelectionBuilder[PostView, String] = Field("title", Scalar())
    def body: SelectionBuilder[PostView, String]  = Field("body", Scalar())

    def comments[A](
        innerSelection: SelectionBuilder[CommentView, A]
    ): SelectionBuilder[PostView, scala.Option[List[A]]] =
      Field("comments", OptionOf(ListOf(Obj(innerSelection))))

  type PhotoView
  object PhotoView:
    def title: SelectionBuilder[PhotoView, String]        = Field("title", Scalar())
    def url: SelectionBuilder[PhotoView, String]          = Field("url", Scalar())
    def thumbnailUrl: SelectionBuilder[PhotoView, String] = Field("thumbnailUrl", Scalar())

  type AlbumView
  object AlbumView:
    def title: SelectionBuilder[AlbumView, String] = Field("title", Scalar())

    def photos[A](
        innerSelection: SelectionBuilder[PhotoView, A]
    ): SelectionBuilder[AlbumView, scala.Option[List[A]]] =
      Field("photos", OptionOf(ListOf(Obj(innerSelection))))

  type UserView
  object UserView:
    def id: SelectionBuilder[UserView, Int]          = Field("id", Scalar())
    def name: SelectionBuilder[UserView, String]     = Field("name", Scalar())
    def username: SelectionBuilder[UserView, String] = Field("username", Scalar())
    def email: SelectionBuilder[UserView, String]    = Field("email", Scalar())
    def phone: SelectionBuilder[UserView, String]    = Field("phone", Scalar())
    def website: SelectionBuilder[UserView, String]  = Field("website", Scalar())

    def address[A](
        innerSelection: SelectionBuilder[AddressView, A]
    ): SelectionBuilder[UserView, A] =
      Field("address", Obj(innerSelection))

    def company[A](
        innerSelection: SelectionBuilder[CompanyView, A]
    ): SelectionBuilder[UserView, A] =
      Field("company", Obj(innerSelection))

    def todos[A](
        innerSelection: SelectionBuilder[TodoView, A]
    ): SelectionBuilder[UserView, scala.Option[List[A]]] =
      Field("todos", OptionOf(ListOf(Obj(innerSelection))))

    def posts[A](
        innerSelection: SelectionBuilder[PostView, A]
    ): SelectionBuilder[UserView, scala.Option[List[A]]] =
      Field("posts", OptionOf(ListOf(Obj(innerSelection))))

    def albums[A](
        innerSelection: SelectionBuilder[AlbumView, A]
    ): SelectionBuilder[UserView, scala.Option[List[A]]] =
      Field("albums", OptionOf(ListOf(Obj(innerSelection))))

  end UserView

  type Queries = RootQuery
  object Queries:

    /** Return users
      */
    def users[A](
        innerSelection: SelectionBuilder[UserView, A]
    ): SelectionBuilder[RootQuery, scala.Option[List[A]]] =
      Field("users", OptionOf(ListOf(Obj(innerSelection))))

    /** Return user
      */
    def user[A](userId: Int)(
        innerSelection: SelectionBuilder[UserView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[RootQuery, scala.Option[A]] =
      Field(
        "user",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    /** Return user todos
      */
    def userTodos[A](userId: Int)(
        innerSelection: SelectionBuilder[TodoView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[RootQuery, scala.Option[List[A]]] =
      Field(
        "userTodos",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    /** Return user posts
      */
    def userPosts[A](userId: Int)(
        innerSelection: SelectionBuilder[PostView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[RootQuery, scala.Option[List[A]]] =
      Field(
        "userPosts",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    // TODO: how?
    def getUsers: SelectionBuilder[RootQuery, List[User]]       = ???
    def getUser(userId: Int): SelectionBuilder[RootQuery, User] = ???

  end Queries

  import scala.concurrent.ExecutionContext.Implicits.global

  def getUsers =
    Queries
      .getUsers
      .toRequest(uri"http://localhost:8088/users")
      .send(sttp.client3.FetchBackend())
      .map(_.body)

  def getUser(userId: Int) =
    Queries
      .getUser(userId)
      .toRequest(uri"http://localhost:8088/user")
      .send(sttp.client3.FetchBackend())
      .map(_.body)

end Client

package typicode.graphql.client

import caliban.client.FieldBuilder.*
import caliban.client.*
import caliban.client.SelectionBuilder.*

object Client:

  type Geo
  object Geo:
    def lat: SelectionBuilder[Geo, Double] = Field("lat", Scalar())
    def lng: SelectionBuilder[Geo, Double] = Field("lng", Scalar())

  type Address
  object Address:
    def street: SelectionBuilder[Address, String]  = Field("street", Scalar())
    def suite: SelectionBuilder[Address, String]   = Field("suite", Scalar())
    def city: SelectionBuilder[Address, String]    = Field("city", Scalar())
    def zipcode: SelectionBuilder[Address, String] = Field("zipcode", Scalar())

    def geo[A](innerSelection: SelectionBuilder[Geo, A]): SelectionBuilder[Address, A] =
      Field("geo", Obj(innerSelection))

  type Company
  object Company:
    def name: SelectionBuilder[Company, String]        = Field("name", Scalar())
    def catchPhrase: SelectionBuilder[Company, String] = Field("catchPhrase", Scalar())
    def bs: SelectionBuilder[Company, String]          = Field("bs", Scalar())

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

  type TodoView
  object TodoView:
    def title: SelectionBuilder[TodoView, String]      = Field("title", Scalar())
    def completed: SelectionBuilder[TodoView, Boolean] = Field("completed", Scalar())

  type UserView
  object UserView:
    def name: SelectionBuilder[UserView, String]     = Field("name", Scalar())
    def username: SelectionBuilder[UserView, String] = Field("username", Scalar())
    def email: SelectionBuilder[UserView, String]    = Field("email", Scalar())
    def phone: SelectionBuilder[UserView, String]    = Field("phone", Scalar())
    def website: SelectionBuilder[UserView, String]  = Field("website", Scalar())

    def address[A](innerSelection: SelectionBuilder[Address, A]): SelectionBuilder[UserView, A] =
      Field("address", Obj(innerSelection))

    def company[A](innerSelection: SelectionBuilder[Company, A]): SelectionBuilder[UserView, A] =
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

  type Queries = Operations.RootQuery
  object Queries:

    /** Return users
      */
    def users[A](username: scala.Option[String] = None)(innerSelection: SelectionBuilder[UserView, A])(using
        ArgEncoder[scala.Option[String]]
    ): SelectionBuilder[Operations.RootQuery, scala.Option[List[A]]] =
      Field(
        "users",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("username", username, "String")),
      )

    /** Return user
      */
    def user[A](userId: Int)(
        innerSelection: SelectionBuilder[UserView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[Operations.RootQuery, scala.Option[A]] =
      Field(
        "user",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    /** Return user todos
      */
    def userTodos[A](userId: Int)(
        innerSelection: SelectionBuilder[TodoView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[Operations.RootQuery, scala.Option[List[A]]] =
      Field(
        "userTodos",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    /** Return user posts
      */
    def userPosts[A](userId: Int)(
        innerSelection: SelectionBuilder[PostView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[Operations.RootQuery, scala.Option[List[A]]] =
      Field(
        "userPosts",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("userId", userId, "Int!")),
      )

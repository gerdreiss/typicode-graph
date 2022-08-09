package typicode

import caliban.client.FieldBuilder.{ ListOf, Obj, OptionOf, Scalar }
import caliban.client.Operations.RootQuery
import caliban.client.SelectionBuilder.Field
import caliban.client.{ ArgEncoder, Argument, CalibanClientError, SelectionBuilder }
import sttp.client3.*
import typicode.domain.*

object Client:

  type GeoView
  object GeoView:
    def lat: SelectionBuilder[GeoView, Double] = Field("lat", Scalar())
    def lng: SelectionBuilder[GeoView, Double] = Field("lng", Scalar())

    def geo: SelectionBuilder[GeoView, Geo] =
      (lat ~ lng).mapN(Geo.apply)

  type AddressView
  object AddressView:
    def street: SelectionBuilder[AddressView, String]  = Field("street", Scalar())
    def suite: SelectionBuilder[AddressView, String]   = Field("suite", Scalar())
    def city: SelectionBuilder[AddressView, String]    = Field("city", Scalar())
    def zipcode: SelectionBuilder[AddressView, String] = Field("zipcode", Scalar())

    def geo[A](innerSelection: SelectionBuilder[GeoView, A]): SelectionBuilder[AddressView, A] =
      Field("geo", Obj(innerSelection))

    def address: SelectionBuilder[AddressView, Address] =
      (street ~ suite ~ city ~ zipcode ~ geo(GeoView.geo))
        .mapN(Address.apply)

  type CompanyView
  object CompanyView:
    def name: SelectionBuilder[CompanyView, String]        = Field("name", Scalar())
    def catchPhrase: SelectionBuilder[CompanyView, String] = Field("catchPhrase", Scalar())
    def bs: SelectionBuilder[CompanyView, String]          = Field("bs", Scalar())

    def company: SelectionBuilder[CompanyView, Company] =
      (name ~ catchPhrase ~ bs).mapN(Company.apply)

  type TodoView
  object TodoView:
    def userId: SelectionBuilder[TodoView, Int]        = Field("userId", Scalar())
    def id: SelectionBuilder[TodoView, Int]            = Field("id", Scalar())
    def title: SelectionBuilder[TodoView, String]      = Field("title", Scalar())
    def completed: SelectionBuilder[TodoView, Boolean] = Field("completed", Scalar())

    def todo: SelectionBuilder[TodoView, Todo] =
      (userId ~ id ~ title ~ completed).mapN(Todo.apply)

  type CommentView
  object CommentView:
    def postId: SelectionBuilder[CommentView, Int]   = Field("postId", Scalar())
    def id: SelectionBuilder[CommentView, Int]       = Field("id", Scalar())
    def name: SelectionBuilder[CommentView, String]  = Field("name", Scalar())
    def email: SelectionBuilder[CommentView, String] = Field("email", Scalar())
    def body: SelectionBuilder[CommentView, String]  = Field("body", Scalar())

    def comment: SelectionBuilder[CommentView, Comment] =
      (postId ~ id ~ name ~ email ~ body).mapN(Comment.apply)

  type PostView
  object PostView:
    def userId: SelectionBuilder[PostView, Int]   = Field("userId", Scalar())
    def id: SelectionBuilder[PostView, Int]       = Field("id", Scalar())
    def title: SelectionBuilder[PostView, String] = Field("title", Scalar())
    def body: SelectionBuilder[PostView, String]  = Field("body", Scalar())

    def comments[A](
        innerSelection: SelectionBuilder[CommentView, A]
    ): SelectionBuilder[PostView, Option[List[A]]] =
      Field("comments", OptionOf(ListOf(Obj(innerSelection))))

    def post: SelectionBuilder[PostView, Post] =
      (userId ~ id ~ title ~ body /* ~ comments(CommentView.comment)*/ )
        .mapN(Post.apply)

  type PhotoView
  object PhotoView:
    def userId: SelectionBuilder[PhotoView, Int]          = Field("userId", Scalar())
    def id: SelectionBuilder[PhotoView, Int]              = Field("id", Scalar())
    def title: SelectionBuilder[PhotoView, String]        = Field("title", Scalar())
    def url: SelectionBuilder[PhotoView, String]          = Field("url", Scalar())
    def thumbnailUrl: SelectionBuilder[PhotoView, String] = Field("thumbnailUrl", Scalar())

    def photo: SelectionBuilder[PhotoView, Photo] =
      (userId ~ id ~ title ~ url ~ thumbnailUrl).mapN(Photo.apply)

  type AlbumView
  object AlbumView:
    def userId: SelectionBuilder[AlbumView, Int]   = Field("userId", Scalar())
    def id: SelectionBuilder[AlbumView, Int]       = Field("id", Scalar())
    def title: SelectionBuilder[AlbumView, String] = Field("title", Scalar())

    def photos[A](
        innerSelection: SelectionBuilder[PhotoView, A]
    ): SelectionBuilder[AlbumView, Option[List[A]]] =
      Field("photos", OptionOf(ListOf(Obj(innerSelection))))

    def album: SelectionBuilder[AlbumView, Album] =
      (userId ~ id ~ title).mapN(Album.apply)

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
    ): SelectionBuilder[UserView, Option[List[A]]] =
      Field("todos", OptionOf(ListOf(Obj(innerSelection))))

    def posts[A](
        innerSelection: SelectionBuilder[PostView, A]
    ): SelectionBuilder[UserView, Option[List[A]]] =
      Field("posts", OptionOf(ListOf(Obj(innerSelection))))

    def albums[A](
        innerSelection: SelectionBuilder[AlbumView, A]
    ): SelectionBuilder[UserView, Option[List[A]]] =
      Field("albums", OptionOf(ListOf(Obj(innerSelection))))

    def user: SelectionBuilder[UserView, User] =
      (
        id ~
          name ~
          username ~
          email ~
          phone ~
          website ~
          address(AddressView.address) ~
          company(CompanyView.company)
      ).mapN(User.apply)

  end UserView

  type Queries = RootQuery
  object Queries:

    /** Return users
      */
    def users[A](
        innerSelection: SelectionBuilder[UserView, A]
    ): SelectionBuilder[RootQuery, Option[List[A]]] =
      Field("users", OptionOf(ListOf(Obj(innerSelection))))

    /** Return user
      */
    def user[A](userId: Int)(
        innerSelection: SelectionBuilder[UserView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[RootQuery, Option[A]] =
      Field(
        "user",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    /** Return user todos
      */
    def userTodos[A](userId: Int)(
        innerSelection: SelectionBuilder[TodoView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[RootQuery, Option[List[A]]] =
      Field(
        "userTodos",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    /** Return user posts
      */
    def userPosts[A](userId: Int)(
        innerSelection: SelectionBuilder[PostView, A]
    )(using ArgEncoder[Int]): SelectionBuilder[RootQuery, Option[List[A]]] =
      Field(
        "userPosts",
        OptionOf(ListOf(Obj(innerSelection))),
        arguments = List(Argument("userId", userId, "Int!")),
      )

    def getUsers: SelectionBuilder[RootQuery, Option[List[User]]] =
      users(UserView.user)

    def getUser(userId: Int): SelectionBuilder[RootQuery, Option[User]] =
      user(userId)(UserView.user)

    def getUserTodos(userId: Int): SelectionBuilder[RootQuery, Option[List[Todo]]] =
      userTodos(userId)(TodoView.todo)

    def getUserPosts(userId: Int): SelectionBuilder[RootQuery, Option[List[Post]]] =
      userPosts(userId)(PostView.post)

    def allUserSelections(userId: Int): SelectionBuilder[
      RootQuery,
      (Option[List[User]], Option[User], Option[List[Todo]], Option[List[Post]]),
    ] =
      users(UserView.user) ~
        user(userId)(UserView.user) ~
        userTodos(userId)(TodoView.todo) ~
        userPosts(userId)(PostView.post)

  end Queries

  import concurrent.ExecutionContext.Implicits.global

  def executeSelection[A](
      selection: SelectionBuilder[RootQuery, Option[A]]
  ): concurrent.Future[Either[CalibanClientError, Option[A]]] =
    selection
      .toRequest(uri"http://localhost:8088/api/graphql")
      .send(FetchBackend())
      .map(_.body)

  def getUsers: concurrent.Future[Either[CalibanClientError, Option[List[User]]]] =
    executeSelection(Queries.getUsers)

  def getUser(userId: Int): concurrent.Future[Either[CalibanClientError, Option[User]]] =
    executeSelection(Queries.getUser(userId))

  def getUserTodos(userId: Int): concurrent.Future[Either[CalibanClientError, Option[List[Todo]]]] =
    executeSelection(Queries.getUserTodos(userId))

  def getUserPosts(userId: Int): concurrent.Future[Either[CalibanClientError, Option[List[Post]]]] =
    executeSelection(Queries.getUserPosts(userId))

end Client

package typicode.domain

import zio.json.*

case class Geo(
    lat: Double,
    lng: Double,
)
object Geo:
  given JsonDecoder[Geo] = DeriveJsonDecoder.gen[Geo]

case class Address(
    street: String,
    suite: String,
    city: String,
    zipcode: String,
    geo: Geo,
)
object Address:
  given JsonDecoder[Address] = DeriveJsonDecoder.gen[Address]

case class Company(
    name: String,
    catchPhrase: String,
    bs: String,
)
object Company:
  given JsonDecoder[Company] = DeriveJsonDecoder.gen[Company]

case class User(
    id: UserId,
    name: String,
    username: String,
    email: String,
    phone: String,
    website: String,
    address: Address,
    company: Company,
)
object User:
  given JsonDecoder[User] = DeriveJsonDecoder.gen[User]

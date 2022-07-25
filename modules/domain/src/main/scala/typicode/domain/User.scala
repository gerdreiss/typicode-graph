package typicode.domain

import zio.json.*

case class Geo(
    lat: Double,
    lng: Double,
)

case class Address(
    street: String,
    suite: String,
    city: String,
    zipcode: String,
    geo: Geo,
)

case class Company(
    name: String,
    catchPhrase: String,
    bs: String,
)

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
  given JsonDecoder[Geo]     = DeriveJsonDecoder.gen[Geo]
  given JsonDecoder[Address] = DeriveJsonDecoder.gen[Address]
  given JsonDecoder[Company] = DeriveJsonDecoder.gen[Company]
  given JsonDecoder[User]    = DeriveJsonDecoder.gen[User]

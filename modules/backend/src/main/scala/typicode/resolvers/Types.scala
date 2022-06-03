package typicode.resolvers

import sttp.client3.httpclient.zio.*

import zio.query.*

import typicode.services.*

type ZQ[A] = RQuery[SttpClient & TypicodeService, A]
type DS[A] = DataSource[SttpClient & TypicodeService, A]

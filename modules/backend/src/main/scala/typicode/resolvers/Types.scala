package typicode.resolvers

import zio.query.*

import typicode.services.*

type ZQ[A] = RQuery[TypicodeService, A]
type DS[A] = DataSource[TypicodeService, A]

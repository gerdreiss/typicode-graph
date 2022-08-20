package typicode
package resolvers

import zio.query.*

import services.*

type ZQ[A] = RQuery[TypicodeService, A]
type DS[A] = DataSource[TypicodeService, A]

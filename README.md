# Typicode JSONPlaceholder API Client

This is an experimental project to explore [GraphQL](https://graphql.org/) and [Scala.js](https://www.scala-js.org/) using

- [Typicode](https://jsonplaceholder.typicode.com/) as data storage
- [Caliban](https://caliban.io/) as GraphQL server and client library
- [Tyrian](https://tyrian.indigoengine.io/) as a [Scala.js](https://www.scala-js.org/) UI library

### Note!

Currently, running the backend of this project fails with:

```console
java.lang.NoClassDefFoundError: zio/IO$
        at caliban.validation.Validator$.validateSchema(Validator.scala:37)
        at caliban.GraphQL.validateRootSchema(GraphQL.scala:30)
        at caliban.GraphQL.validateRootSchema$(GraphQL.scala:23)
        at caliban.GraphQL$$anon$7.validateRootSchema(GraphQL.scala:244)
        at caliban.GraphQL.interpreter(GraphQL.scala:78)
        at caliban.GraphQL.interpreter$(GraphQL.scala:23)
        at caliban.GraphQL$$anon$7.interpreter(GraphQL.scala:244)
        at typicode.Main$.<clinit>(Main.scala:22)
        at typicode.Main.main(Main.scala)
...
```

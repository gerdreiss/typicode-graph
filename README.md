# Typicode JSONPlaceholder API Client

This is an experimental project to explore [GraphQL](https://graphql.org/) and [Scala.js](https://www.scala-js.org/) using

- [Typicode](https://jsonplaceholder.typicode.com/) as data storage
- [Caliban](https://caliban.io/) as GraphQL server and client library
- [Tyrian](https://tyrian.indigoengine.io/) as a [Scala.js](https://www.scala-js.org/) UI library

### Note!

Currently, trying to execute the interpreter directly fails with:

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

When uncommenting zhttp.service.Server.start(Main.main) compilation fails with:

```console
Error while emitting Main.scala
[info] exception occurred while compiling /Users/g/Workspace/Projects/typicode-graph/modules/backend/src/main/scala/typicode/Main.scala
java.lang.AssertionError: assertion failed while compiling /Users/g/Workspace/Projects/typicode-graph/modules/backend/src/main/scala/typicode/Main.scala
[error] ## Exception when compiling 10 sources to /Users/g/Workspace/Projects/typicode-graph/modules/backend/target/scala-3.1.2/classes
[error] java.lang.AssertionError: assertion failed
[error] scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:11)
...
```

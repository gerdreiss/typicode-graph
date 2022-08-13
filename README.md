# Expose Typicode JSONPlaceholder API via GraphQL

This is an experimental project to explore [GraphQL](https://graphql.org/) and [Scala.js](https://www.scala-js.org/) using

- [Typicode](https://jsonplaceholder.typicode.com/) as data storage
- [Caliban](https://caliban.io/) as GraphQL server and client library
- [Laminar](https://laminar.dev/) as a [Scala.js](https://www.scala-js.org/) UI library

### Run project

Open conmmand line and enter
```console
sbt backend/run
```
Open second command line and enter
```console
sbt frontend/fastOptJS
npm install -g http-server
http-server -c-1
```
Open http://localhost:8080/modules/frontend/index-dev.html in the browser

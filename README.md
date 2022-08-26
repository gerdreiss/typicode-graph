# Expose Typicode JSONPlaceholder API via GraphQL

[![Scala CI](https://github.com/gerdreiss/typicode-graph/actions/workflows/scala.yml/badge.svg)](https://github.com/gerdreiss/typicode-graph/actions/workflows/scala.yml)

This is an experimental project to explore [GraphQL](https://graphql.org/) and [Scala.js](https://www.scala-js.org/) using

- [Typicode](https://jsonplaceholder.typicode.com/) as data storage
- [Caliban](https://caliban.io/) as GraphQL server and client library
- [Laminar](https://laminar.dev/) as a [Scala.js](https://www.scala-js.org/) UI library

## Run project

### Backend

Open command line and enter

```console
sbt backend/run
```

### Frontend

Open second command line and enter

```console
sbt frontend/fastOptJS
```

Install http-server if necessary:

- using [homebrew](https://brew.sh/):
  ```console
  brew install http-server
  ```
- using [node's](https://nodejs.org/en/) `npm` command:
  ```console
  npm install -g http-server
  ```
  Start http-server

```console
http-server -c-1
```

Or directly run without installing, using [node's](https://nodejs.org/en/) `npx` command:

```console
npx http-server -c-1
```

Or use Python's [SimpleHTTPServer](https://docs.python.org/2/library/simplehttpserver.html) module:

```console
python -m SimpleHTTPServer
# or the Python 3 equivalent
python3 -m http.server
```

```

Open http://localhost:8080/modules/frontend/index-dev.html in the browser
```

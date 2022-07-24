package typicode

import com.raquo.laminar.api.L.*

object Main:
  def main(args: Array[String]): Unit =
    render(
      org.scalajs.dom.document.querySelector("#app"),
      Views.renderApp,
    )

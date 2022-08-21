package typicode

import com.raquo.laminar.api.L.*
import org.scalajs.dom.document

object Main:
  def main(args: Array[String]): Unit =
    render(
      document.querySelector("#app"),
      Views.renderApp,
    )

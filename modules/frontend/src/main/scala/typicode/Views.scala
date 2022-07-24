package typicode

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLElement

object Views:
  def renderApp: ReactiveHtmlElement[HTMLElement] =
    div(
      cls := "ui raised very padded container segment",
      h1(
        cls   := "ui header",
        i(cls   := "circular users icon"),
        div(cls := "content", "Users"),
      ),
      div(cls := "ui divider"),
    )

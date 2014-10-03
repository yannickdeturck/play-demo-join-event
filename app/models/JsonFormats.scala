package models

import play.api.libs.json._
import java.util.Date
import java.text.{SimpleDateFormat}
import anorm._

/**
 * Created with IntelliJ IDEA.
 * User: yannickdeturck
 * Date: 30/01/14
 * Time: 22:12
 * To change this template use File | Settings | File Templates.
 */
object JsonFormats {
  implicit val dateFormat = new Format[Date] {
    def writes(date: Date): JsValue = {
      val format = new SimpleDateFormat("dd/MM/yyyy")
      Json.toJson(format.format(date))
    }
    def reads(jv: JsValue): JsResult[Date] = JsError() // TODO zou nog geimplementeerd moeten worden
  }
  implicit def pizzaModelFormat = Json.format[Pizza]
  implicit def pizzaOrderModelFormat = Json.format[PizzaOrder]
}

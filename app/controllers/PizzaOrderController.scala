package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.i18n.Messages
import anorm._

import models.{PizzaOrder, Pizza}

object PizzaOrderController extends Controller {

  val quantityList = List(1,2,3,4,5)

  val orderForm: Form[PizzaOrder] = Form(
    mapping(
      "orderID" -> ignored(None: Option[Long]),
      "pizzaID" -> of[Long],
      "quantity" -> number(min=1, max=5),
      "orderDate" -> ignored(new java.util.Date()),
      "customerName" -> nonEmptyText,
      "remarks" -> optional(text),
      "isSent" -> ignored(false),
      "sentDate" -> optional(date("dd/MM/yyyy"))
    )((orderID, pizzaID, quantity, orderDate, customerName, remarks, isSent, sentDate) => PizzaOrder(orderID, Pizza.find(pizzaID).get,
      quantity, orderDate, customerName, remarks, isSent, sentDate)
      )(
        (order: PizzaOrder) => Some((order.orderID, order.pizza.pizzaID.get, order.quantity, order.orderDate, order.customerName,
          order.remarks, order.isSent, order.sentDate))
      )
  )

  def index = Action { implicit request =>
    Ok(views.html.order.index(PizzaOrder.findAll()))
  }

  def newOrder = Action {
    Ok(views.html.order.create(orderForm, Pizza.findAll(), quantityList))
  }

  def createOrder = Action { implicit request =>
    orderForm.bindFromRequest.fold(
      errors => BadRequest(views.html.order.create(errors, Pizza.findAll(), quantityList)),
      pizzaOrder => {
        PizzaOrder.create(pizzaOrder)
        Redirect(routes.PizzaOrderController.index)
      }
    )
  }

  def deleteOrder(orderID: Long) = Action {
    PizzaOrder.delete(orderID)
    Redirect(routes.PizzaOrderController.index)
  }

  def sendOrder(orderID: Long) = Action { implicit request =>
    PizzaOrder.send(orderID)
    Redirect(routes.PizzaOrderController.index).flashing(
      "success" -> Messages("order.index.order_sent")
    )
  }
}
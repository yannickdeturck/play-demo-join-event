package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.i18n.Messages
import anorm._
import models.Pizza
import models.JsonFormats._

import play.api.libs.json._

object PizzaController extends Controller {

  val pizzaForm: Form[Pizza] = Form(
    mapping(
      "pizzaID" -> ignored(None: Option[Long]),
      "pizzaNaam" -> nonEmptyText(maxLength=25),
      "hasTomato" -> boolean,
      "hasMozzarella" -> boolean,
      "hasCheese" -> boolean,
      "hasHam" -> boolean,
      "hasSalami" -> boolean,
      "isUsed" -> ignored(false)
    )(Pizza.apply)(Pizza.unapply)
  )


  def index = Action { implicit request =>
    Ok(views.html.pizza.index(Pizza.findAll()))
  }

  def newPizza = Action {
    Ok(views.html.pizza.create(pizzaForm))
  }

  def createPizza = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      errors => BadRequest(views.html.pizza.create(errors)),
      pizza => {
        Pizza.create(pizza)
        Redirect(routes.PizzaController.index)
      }
    )
  }

  def deletePizza(pizzaID: Long) = Action {
    Pizza.delete(pizzaID)
    Redirect(routes.PizzaController.index)
  }

  def lookUp() = Action { implicit request =>
    val pizzas = Pizza.findAll()
    val json = Json.toJson(pizzas)
    Ok(json).as("application/json")
  }

}

package controllers

import play.api._
import play.api.mvc._
import models.PizzaOrder

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.order.index(PizzaOrder.findAll()))
  }

}
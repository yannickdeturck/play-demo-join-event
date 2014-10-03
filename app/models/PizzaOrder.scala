package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import java.util.Date

case class PizzaOrder(orderID: Option[Long],
                      pizza: Pizza,
                      quantity: Int,
                      orderDate: Date,
                      customerName: String,
                      remarks: Option[String],
                      isSent: Boolean,
                      sentDate: Option[Date]
                       )

object PizzaOrder {
  val simple = {
      get[Option[Long]]("orderID") ~
      get[Long]("pizzaID") ~
      get[Int]("quantity") ~
      get[Date]("orderDate") ~
      get[String]("customerName") ~
      get[Option[String]]("remarks") ~
      get[Boolean]("isSent") ~
      get[Option[Date]]("sentDate") map{
      case orderID~pizzaID~quantity~orderDate~customerName~remarks~isSent~sentDate => PizzaOrder(orderID, Pizza.find(pizzaID).get, quantity, orderDate, customerName, remarks, isSent, sentDate)
    }
  }

  def findAll(): List[PizzaOrder] = {
    DB.withConnection { implicit connection =>
      SQL("""
          select *
          from pizzaorder
          where isSent = false
          """).as(PizzaOrder.simple *)
    }
  }

  def create(o: PizzaOrder): Unit = {
    DB.withConnection{ implicit connection =>
      SQL("""
        insert into pizzaorder(pizzaID, quantity, orderDate, customerName, remarks, isSent, sentDate) 
        values ({pizzaID}, {quantity}, {orderDate}, {customerName}, {remarks}, {isSent}, {sentDate})
          """).on(
          'pizzaID -> o.pizza.pizzaID,
          'quantity -> o.quantity,
          'orderDate -> o.orderDate,
          'customerName -> o.customerName,
          'remarks -> o.remarks,
          'isSent -> o.isSent,
          'sentDate -> o.sentDate
        ).executeInsert()
    }
  }

  def delete(orderID: Long) = {
    DB.withConnection{ implicit connection =>
      SQL("delete from pizzaorder where orderID = {orderID}").on(
        'orderID -> orderID
      ).executeUpdate()
    }
  }

  def send(orderID: Long) = {
    DB.withConnection{ implicit connection =>
      SQL("update pizzaorder set isSent = {isSent}, sentDate = {sentDate} where orderID = {orderID}").on(
        'isSent -> true,
        'sentDate -> new java.util.Date,
        'orderID -> orderID
      ).executeUpdate()
    }
  }
}
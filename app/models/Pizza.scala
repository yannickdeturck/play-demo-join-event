package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Pizza(pizzaID: Option[Long],
                 pizzaName: String,
                 hasTomato: Boolean,
                 hasMozzarella: Boolean,
                 hasCheese: Boolean,
                 hasHam: Boolean,
                 hasSalami: Boolean,
                 isUsed: Boolean)

object Pizza {

  val simple = {
      get[Option[Long]]("pizzaID") ~
      get[String]("pizzaName") ~
      get[Boolean]("hasTomato") ~
      get[Boolean]("hasMozzarella") ~
      get[Boolean]("hasCheese") ~
      get[Boolean]("hasHam") ~
      get[Boolean]("hasSalami") ~
      get[Boolean]("isUsed") map{
      case pizzaID~pizzaName~hasTomato~hasMozzarella~hasCheese~hasHam~hasSalami~isUsed => Pizza(pizzaID, pizzaName, hasTomato, hasMozzarella, hasCheese, hasHam, hasSalami, isUsed)
    }
  }


  def findAll(): List[Pizza] = {
    DB.withConnection { implicit connection =>
      SQL("""
          select pizzaID,
              pizzaName,
              hasTomato,
              hasMozzarella,
              hasCheese,
              hasHam,
              hasSalami,
              (CASE WHEN (select 1 from pizzaorder o where o.pizzaID = p.pizzaID group by o.pizzaID) is null then false else true end) as "isused"
          from pizza p
          """).as(Pizza.simple *)
    }
  }


  def find(pizzaID: Long): Option[Pizza] = {
    DB.withConnection { implicit connection =>
      SQL("""
          select pizzaID, 
                 pizzaName,
                 hasTomato,
                 hasMozzarella,
                 hasCheese,
                 hasHam,
                 hasSalami,
                 (CASE WHEN (select 1 from pizzaorder o where o.pizzaID = p.pizzaID group by o.pizzaID) IS NULL THEN false ELSE true END) as "isused"
          from pizza p
          where p.pizzaID = {pizzaID}
          """).on(
          'pizzaID -> pizzaID
        ).as(Pizza.simple.singleOpt)
    }
  }

  def create(p: Pizza): Unit = {
    DB.withConnection{ implicit connection =>
      SQL("""
        insert into pizza(pizzaName, hasTomato, hasMozzarella, hasCheese, hasHam, hasSalami) 
        values ({pizzaName}, {hasTomato}, {hasMozzarella}, {hasCheese}, {hasHam}, {hasSalami})
          """).on(
          'pizzaName -> p.pizzaName,
          'hasTomato -> p.hasTomato,
          'hasMozzarella -> p.hasMozzarella,
          'hasCheese -> p.hasCheese,
          'hasHam -> p.hasHam,
          'hasSalami -> p.hasSalami
        ).executeInsert()
    }
  }

  def delete(pizzaID: Long) = {
    DB.withConnection{ implicit connection =>
      SQL("delete from pizza where pizzaID = {pizzaID}").on(
        'pizzaID -> pizzaID
      ).executeUpdate()
    }
  }
}
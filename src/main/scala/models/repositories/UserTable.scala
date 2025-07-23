package models.tables

import models.User
import slick.jdbc.PostgresProfile.api._

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def email = column[String]("email")
  def * = (id.?, name, email) <> (User.tupled, User.unapply)
//  def * = (id, name, email) <> (User.tupled, User.unapply)
}

object UserTable {
  val users = TableQuery[UserTable]
}

package models.repositories

import models.User
import models.tables.UserTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class UserRepository(db: Database)(implicit ec: ExecutionContext) {

  import UserTable._

  def getAllUsers(): Future[Seq[User]] = {
    db.run(users.result)
  }

  def getUserById(id: Int): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }

  def createUser(user: User): Future[Int] = {
    val userToInsert = (user.name, user.email)
    val insertQuery = users.map(u => (u.name, u.email)) returning users.map(_.id)
    db.run(insertQuery += userToInsert)
  }

  def updateUser(id: Int, updatedUser: User): Future[Boolean] = {
    val query = users.filter(_.id === id)
      .map(u => (u.name, u.email))
      .update((updatedUser.name, updatedUser.email))

    db.run(query).map(_ > 0)
  }

  def deleteUser(id: Int): Future[Boolean] = {
    db.run(users.filter(_.id === id).delete).map(_ > 0)
  }
}

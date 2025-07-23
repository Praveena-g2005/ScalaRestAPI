package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import controllers.UserController
import models.repositories.UserRepository

import scala.concurrent.ExecutionContext

object UserRoutes {
  def apply(userRepo: UserRepository)(implicit ec: ExecutionContext): Route = {
    val userController = new UserController(userRepo)
      userController.route
  }
}

package controllers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import models.User
import models.repositories.UserRepository
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class UserController(userRepo: UserRepository)(implicit ec: ExecutionContext) {

  implicit val formats: DefaultFormats.type = DefaultFormats

  val route: Route =
    pathPrefix("api" / "users") {
      concat(
        // GET /api/users
        pathEndOrSingleSlash {
          get {
            onComplete(userRepo.getAllUsers()) {
              case Success(users) => complete(StatusCodes.OK, write(users))
              case Failure(ex)    => complete(StatusCodes.InternalServerError, s"Error: ${ex.getMessage}")
            }
          } ~
            post {
              entity(as[String]) { body =>
                val user = read[User](body)
                onComplete(userRepo.createUser(user)) {
                  case Success(_)  => complete(StatusCodes.Created, "User created successfully")
                  case Failure(ex) => complete(StatusCodes.InternalServerError, s"Error: ${ex.getMessage}")
                }
              }
            }
        },

        // GET /api/users/{id}
        path(IntNumber) { id =>
          get {
            onComplete(userRepo.getUserById(id)) {
              case Success(Some(user)) => complete(StatusCodes.OK, write(user))
              case Success(None)       => complete(StatusCodes.NotFound, "User not found")
              case Failure(ex)         => complete(StatusCodes.InternalServerError, s"Error: ${ex.getMessage}")
            }
          } ~
            put {
              entity(as[String]) { body =>
                val updatedUser = read[User](body)
                onComplete(userRepo.updateUser(id, updatedUser)) {
                  case Success(true)  => complete(StatusCodes.OK, "User updated")
                  case Success(false) => complete(StatusCodes.NotFound, "User not found")
                  case Failure(ex)    => complete(StatusCodes.InternalServerError, s"Error: ${ex.getMessage}")
                }
              }
            } ~
            delete {
              onComplete(userRepo.deleteUser(id)) {
                case Success(true)  => complete(StatusCodes.OK, "User deleted")
                case Success(false) => complete(StatusCodes.NotFound, "User not found")
                case Failure(ex)    => complete(StatusCodes.InternalServerError, s"Error: ${ex.getMessage}")
              }
            }
        }
      )
    }
}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContextExecutor

import slick.jdbc.PostgresProfile.api._

import routes.UserRoutes
import models.repositories.UserRepository

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("user-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    // ✅ Database config loaded from application.conf
    val db = Database.forConfig("mydb")

    // ✅ Pass db and executionContext to the repository
    val userRepo = new UserRepository(db)(executionContext)

    val routes = UserRoutes(userRepo)

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)

    println(s"Server started at http://localhost:8080")
  }
}

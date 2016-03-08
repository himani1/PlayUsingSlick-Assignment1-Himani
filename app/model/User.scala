package model

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{Await, Future}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._

case class User(id: Long, name: String, password: String, email: String)

case class UserFormData(name: String, password: String,email: String)

object UserForm {

  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> email
    )(UserFormData.apply)(UserFormData.unapply)
  )
}

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def password = column[String]("password")
  def email = column[String]("email")

  override def * =
    (id, name,password,email) <>(User.tupled, User.unapply)
}

object Users {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val users = TableQuery[UserTableDef]

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }

  def checkUser(email:String,password:String): Boolean= {
   val res=dbConfig.db.run(users.filter(det=>det.email===email && det.password===password).result)
    val resultAsync = Await.result(res, 60.second).toList
   /* println("value is "+ resultAsync)
    println(resultAsync.toList)*/
    if(resultAsync.map(_.email==email)==List(true)) true else false
  }


}

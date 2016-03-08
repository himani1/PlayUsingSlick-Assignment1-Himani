package services

import com.google.inject.ImplementedBy
import model.{User, Users}
import scala.concurrent.Future

@ImplementedBy(classOf[UserService])
trait UserServiceApi{
  def addUser(user: User): Future[String]
  def deleteUser(id: Long): Future[Int]
  def getUser(id: Long): Future[Option[User]]
  def listAllUsers: Future[Seq[User]]
  def checkUsers(email:String,password:String): Boolean
}

class UserService extends UserServiceApi{

  def addUser(user: User): Future[String] = {
    Users.add(user)
  }

  def deleteUser(id: Long): Future[Int] = {
    Users.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    Users.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }

  def checkUsers(email:String,password:String): Boolean= {
    Users.checkUser(email,password)
  }
}
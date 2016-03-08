package controllers

import com.google.inject.Inject
import model.{User, UserForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.Future
import services.{UserServiceApi, UserService}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

case class UserData(email:String,password:String)

class ApplicationController @Inject()(UserService:UserServiceApi) extends Controller {


  val userForm = Form(
    mapping(
      "email"->nonEmptyText,
      "password"->nonEmptyText
    )(UserData.apply)(UserData.unapply)/*.verifying("Failed from constraint",userData=>UserService.checkUsers(userData.email,userData.password))*/
  )

  def index = Action.async { implicit request =>
    UserService.listAllUsers map { users =>
      Ok(views.html.index(UserForm.form, users))
    }
  }

  def signUp = Action { implicit request =>
    Ok(views.html.signUp(UserForm.form))
  }

  def addUser() = Action.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.index(errorForm, Seq.empty[User]))),
      data => {
        val newUser = User(0, data.name, data.password, data.email)
        UserService.addUser(newUser).map(res =>
          Redirect(routes.ApplicationController.getLogin())

        )
      })
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    UserService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationController.index())
    }
  }


  def getLogin =Action {implicit request=>
    Ok(views.html.login(userForm))
  }

  def login =Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Redirect(routes.ApplicationController.getLogin())
      },
      userData => {
        val res=UserService.checkUsers(userData.email,userData.password)
        println(res)
        if(res==true)
        Redirect(routes.ApplicationController.account()).withSession("email"->userData.email)
        else Redirect(routes.ApplicationController.getLogin())
      }
    )
  }

  def account=Action { request =>
    request.session.get("email").map { email => Ok(views.html.account(email)) }.getOrElse {
      Unauthorized("You are not Logged In.")
    }
  }

  def logout=Action{implicit request=>
    Redirect(routes.ApplicationController.signUp()).withNewSession
  }

}

package controllers

import javax.inject._
import play.api._
import play.api.mvc._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
  config: Configuration,
  val controllerComponents: ControllerComponents
) extends BaseController {

  private val unifiedApiKey = config.get[String]("unified.api_key")
  private val logger = Logger(this.getClass).logger

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }

  def status(): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] => {

      logger.warn(s" MY DUMMY API KEY ${unifiedApiKey} ")

      Ok("haha it works pretty well.")
    }}
}

package ansible

import scala.util.{Failure, Success, Try}
import scala.io.Source
import play.api.libs.json._

/**
  * Created by Thomas Hamm on 29.03.17.
  */
class AnsibleModule(specs: Seq[AnsibleArgument], argPath: String, debug: Boolean = false) {
  private var argSpec: Option[Map[String, AnsibleArgument]] = None
  private var args: Option[Map[String, JsValue]] = None

  init(specs, argPath)

  private val name: String = getArg("_ansible_module_name").getOrElse(JsString("")).as[String]
  private val version: String = getArg("_ansible_version").getOrElse(JsString("")).as[String]

  private def init(specs: Seq[AnsibleArgument], argPath: String): Unit = {
    argSpec = Some(specs.map(arg => (arg.name, arg)) toMap)

    val argsIn = Source.fromFile(argPath).mkString

    argsIn.charAt(0) match {
      case '{' => {
        parseJsonArgs(argsIn) match {
          case Success(a) => args = Some(a)
          case Failure(e) => failJson("Failed to parse arguments")
        }
      }
      case _ => {
        parseNativeArgs(argsIn) match {
          case Success(a) => args = Some(a)
          case Failure(e) => failJson("Only json arguments are supported, specify WANT_JSON within your module")
        }
      }
    }

    val missingArgs = validateSpecs()

    if (missingArgs.length > 0) failJson("Missing required arguments: " + missingArgs.map(spec => spec.name).mkString(", "))
  }

  private def validateSpecs(): Seq[AnsibleArgument] = argSpec.get.filter(arg => (arg._2.required == true && arg._2.default.isEmpty && !args.getOrElse(Map[String, JsValue]()).contains(arg._1))).values.toSeq

  private def parseNativeArgs(argsIn: String): Try[Map[String, JsValue]] = Try(throw new Exception)

  private def parseJsonArgs(argsIn: String): Try[Map[String, JsValue]] = Try(Json.parse(argsIn).as[Map[String, JsValue]])

  def getName(): String = name

  def getVersion(): String = version

  def getArg(name: String): Option[JsValue] = {
    Try(args get name) match {
      case Success(a) => Some(a)
      case Failure(e) => {
        Try(argSpec get name) match {
          case Success(a) => a.default
          case Failure(e) => None
        }
      }
    }
  }

  def exitJson(changed: Boolean, data: JsObject = JsObject(Seq())): Unit = {
    print(data + ("changed", JsBoolean(changed)))
    if(!debug) sys.exit(0)
  }

  def failJson(msg: String): Unit = {
    print(JsObject(Seq("failed" -> JsBoolean(true), "msg" -> JsString(msg))))
    if(!debug) sys.exit(1)
  }
}

object AnsibleModule {
  def apply(specs: Seq[AnsibleArgument], argPath: String, debug: Boolean = false): AnsibleModule = {
    new AnsibleModule(specs, argPath, debug)
  }
}

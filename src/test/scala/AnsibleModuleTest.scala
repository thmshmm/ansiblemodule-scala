import ansible.{AnsibleArgument, AnsibleModule}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import play.api.libs.json.{JsNumber, JsString}

/**
  * Created by Thomas Hamm on 29.03.17.
  */
class AnsibleModuleTest extends FlatSpec with Matchers with BeforeAndAfter {
  val argsJsonFile = getClass getResource("/args-json.txt") getPath

  val am1 = AnsibleModule(Seq(
    AnsibleArgument("arg1"),
    AnsibleArgument("arg2", true, Some(JsString("test2test2"))),
    AnsibleArgument("arg3", true),
    AnsibleArgument("arg77")
  ), argsJsonFile, true)

  "AnsibleModule (valid arguments file)" should "have a name" in {
    assert(am1.getName().nonEmpty)
    assert(am1.getName().equals("test-module"))
  }

  it should "have a version" in {
    assert(am1.getVersion().nonEmpty)
    assert(am1.getVersion().equals("2.2.0.0"))
  }

  it should "return available arguments if present" in {
    assert(am1.getArg("arg1").nonEmpty)
    assert(am1.getArg("arg1").getOrElse(JsString("empty")).as[String].equals("test1"))
    assert(am1.getArg("arg2").nonEmpty)
    assert(am1.getArg("arg2").getOrElse(JsString("empty")).as[String].equals("test 2"))
    assert(am1.getArg("arg3").nonEmpty)
    assert(am1.getArg("arg3").getOrElse(0) == JsNumber(3))
    assert(am1.getArg("arg4").nonEmpty)
  }

  val argsJsonFileInvalid = getClass getResource("/args-json-invalid.txt") getPath

  "AnsibleModule (invalid json arguments file)" should "print an Ansible error message" in {
    val stream = new java.io.ByteArrayOutputStream()

    Console.withOut(stream) {
      AnsibleModule(Seq(
        AnsibleArgument("arg1", false)
      ), argsJsonFileInvalid, true)
    }

    assert(stream.toString.equals("{\"failed\":true,\"msg\":\"Failed to parse arguments\"}"))
  }

  val argsKeyValueFile = getClass getResource("/args-key-value.txt") getPath

  "AnsibleModule (key-value arguments file)" should "print an Ansible error message" in {
    val stream = new java.io.ByteArrayOutputStream()

    Console.withOut(stream) {
      AnsibleModule(Seq(
        AnsibleArgument("arg1", false)
      ), argsKeyValueFile, true)
    }

    assert(stream.toString.equals("{\"failed\":true,\"msg\":\"Only json arguments are supported, specify WANT_JSON within your module\"}"))
  }
}

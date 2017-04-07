import ansible.AnsibleArgument
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.JsString

/**
  * Created by Thomas Hamm on 29.03.17.
  */
class AnsibleArgumentTest extends FlatSpec with Matchers {
  val arg1 = AnsibleArgument("arg1")

  "AnsibleArgument (only one argument)" should "have its specified name" in {
    assert(arg1.name.equals("arg1"))
  }

  it should "have no default value" in {
    assert(arg1.default.isEmpty)
  }

  it should "not be defined as required" in {
    assert(arg1.required == false)
  }

  val arg2 = AnsibleArgument("arg2", true, Some(JsString("test")))

  "AnsibleArgument" should "have its variables properly set" in {
    assert(arg2.name.equals("arg2"))
    assert(arg2.required == true)
    assert(arg2.default.get.as[String].equals("test"))
  }

  it should "only have immutable variables" in {
    "arg2.name = \"test\"" shouldNot compile
    "arg2.required = false" shouldNot compile
    "arg2.default = Some(JsString(\"test\"))" shouldNot compile
  }
}

package ansible

import play.api.libs.json.JsValue

/**
  * Created by Thomas Hamm on 29.03.17.
  */
case class AnsibleArgument(name: String, required: Boolean = false, default: Option[JsValue] = None) {
  def this(name: String, default: Option[JsValue]) = this(name, false, default)
}

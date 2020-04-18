package helpers

import internals.Form
import play.api.libs.json.{Json, OFormat}
import utils.ResponseCodes

object FormControllerHelper{

  case class FormResponse(id: Option[String], name: String, elements: String)

  case class FormsResponse(responseCode: Int, responseMessage: String, forms: Seq[FormResponse])

  implicit val formResponseFormat: OFormat[FormResponse] = Json.format[FormResponse]
  implicit val formsResponseFormat: OFormat[FormsResponse] = Json.format[FormsResponse]

  def toFormResponse(form:Form) = {
    FormResponse(form.id,form.name,form.elements)
  }

  def toFormsResponse(forms:Seq[Form]) = {
    FormsResponse(ResponseCodes.SUCCESS, "Success", forms.map(toFormResponse))
  }

}


import scala.io.StdIn.readLine
import scala.collection.mutable.ListBuffer
import play.api.libs.json._
import sttp.client3._
import sttp.model.Uri

object Main {
  case class Data(icc: String, lastConnStart: Option[String])
  case class ApiResponse(status: Boolean, data: Option[Data], message: String)

  implicit val dataReads: Reads[Data] = Json.reads[Data]
  implicit val apiResponseReads: Reads[ApiResponse] = Json.reads[ApiResponse]

  def main(args: Array[String]): Unit = {
    // Obtener los ICC de la entrada
    print("Ingresa los ICC separados por comas: ")
    val iccs = readLine().split(",").map(_.trim)

    // Crear un backend para sttp
    val backend = HttpURLConnectionBackend()

    // Consultar la API externa para cada ICC
    iccs.foreach { icc =>
      val url = uri"https://m2mcenter.app/apiclient/v1/sims/simDetails/icc/$icc"
      val apiKey = "2055839e-cd18-4bab-a118-b93fd05e63b6"
      val request = basicRequest.header("X-API-KEY", apiKey).get(url)

      try {
        val response = request.send(backend)
        response.body match {
          case Right(body) =>
            println(s"Respuesta JSON para ICC $icc: $body") // Imprimir el JSON recibido
            Json.parse(body).validate[ApiResponse] match {
              case JsSuccess(apiResponse, _) =>
                apiResponse.data match {
                  case Some(data) =>
                    println(s"ICC: ${data.icc}")
                    data.lastConnStart match {
                      case Some(lastConnStart) =>
                        println(s"Fecha de última conexión: $lastConnStart")
                      case None =>
                        println(s"El dato de última conexión no existe para ICC: ${data.icc}")
                    }
                  case None =>
                    println(s"Error en la solicitud para ICC: $icc, Mensaje: ${apiResponse.message}")
                }
              case JsError(errors) =>
                println(s"Error al parsear JSON para ICC: $icc, Errores: $errors")
            }
          case Left(error) =>
            println(s"Error en la solicitud HTTP para ICC: $icc, Error: $error")
        }
      } catch {
        case e: Exception =>
          println(s"Excepción al consultar ICC: $icc, Mensaje: ${e.getMessage}")
      }
    }
  }
}
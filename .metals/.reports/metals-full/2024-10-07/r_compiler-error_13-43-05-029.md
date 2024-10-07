file://<WORKSPACE>/src/main/scala/Main.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1369
uri: file://<WORKSPACE>/src/main/scala/Main.scala
text:
```scala
import scala.io.StdIn.readLine
import scala.collection.mutable.ListBuffer
import play.api.libs.json._
import scalaj.http._

object Main {
  case class SimDetails(icc: String, lastConnStart: String)

  implicit val simDetailsReads: Reads[SimDetails] = Json.reads[SimDetails]

  def main(args: Array[String]): Unit = {
    // Obtener los ICC de la entrada
    print("Ingresa los ICC separados por comas: ")
    val iccs = readLine().split(",")

    // Crear una lista para almacenar los resultados
    val resultados = ListBuffer[SimDetails]()

    // Consultar la API externa para cada ICC
    for (icc <- iccs) {
      val url = s"https://m2mcenter.app/apiclient/v1/sims/simDetails/icc/${icc.trim}"
      val apiKey = "ecadc0e3-01c0-43f5-a4ee-d82c193a1077"
      val headers = Map("X-API-KEY" -> apiKey)

      try {
        val respuesta = Http(url).headers(headers).asString
        if (respuesta.is2xx) {
          Json.parse(respuesta.body).validate[SimDetails] match {
            case JsSuccess(simDetails, _) =>
              resultados += simDetails
              println(s"ICC: ${simDetails.icc}")
              println(s"Fecha de última conexión: ${simDetails.lastConnStart}")
            case JsError(errors) =>
              println(s"Error al parsear JSON para ICC: ${icc.trim}, Errores: $errors")
          }
        } else {
          println(s"Error en @@la solicitud HTTP para ICC: ${icc.trim}, Código de estado: ${respuesta.code}")
        }
      } catch {
        case e: Exception =>
          println(s"Excepción al consultar ICC: ${icc.trim}, Mensaje: ${e.getMessage}")
      }
    }

    // Mostrar los resultados en la consola
    for (resultado <- resultados) {
      println(resultado)
    }
  }
}
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2607)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.isWrappedToplevelDef(SymDenotations.scala:677)
	dotty.tools.dotc.core.SymDenotations$ClassDenotation.invalidateMemberCachesFor(SymDenotations.scala:1848)
	dotty.tools.dotc.core.SymDenotations$ClassDenotation.enterNoReplace(SymDenotations.scala:2068)
	dotty.tools.dotc.core.SymDenotations$ClassDenotation.enter(SymDenotations.scala:2058)
	dotty.tools.dotc.core.ContextOps$.enter(ContextOps.scala:21)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath$$anonfun$2(Interactive.scala:305)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:307)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:284)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:284)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:284)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:284)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:284)
	dotty.tools.dotc.interactive.Interactive$.contextOfPath(Interactive.scala:284)
	scala.meta.internal.mtags.MtagsEnrichments$.localContext(MtagsEnrichments.scala:75)
	scala.meta.internal.pc.PcDefinitionProvider.ctx$lzyINIT1$1(PcDefinitionProvider.scala:51)
	scala.meta.internal.pc.PcDefinitionProvider.ctx$4(PcDefinitionProvider.scala:51)
	scala.meta.internal.pc.PcDefinitionProvider.definitions(PcDefinitionProvider.scala:52)
	scala.meta.internal.pc.PcDefinitionProvider.definitions(PcDefinitionProvider.scala:34)
	scala.meta.internal.pc.ScalaPresentationCompiler.definition$$anonfun$1(ScalaPresentationCompiler.scala:165)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner
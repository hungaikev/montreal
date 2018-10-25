package com.lightbend.room

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.http.scaladsl.{ Http, HttpConnectionContext }
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.UseHttp2.Always
import akka.stream.ActorMaterializer
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory

object RoomServer {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on").withFallback(ConfigFactory.defaultApplication())
    val system: ActorSystem = ActorSystem("BookingServer", conf)
    new RoomServer(system).run()
  }

}

class RoomServer (system: ActorSystem)  {
  def run(): Future[Http.ServerBinding] = {
    implicit val sys = system
    implicit val mat: Materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = sys.dispatcher

    val service: HttpRequest => Future[HttpResponse] = RoomServiceHandler(new RoomServiceImpl(mat))

    val bound = Http().bindAndHandleAsync(
      service,
      interface = "127.0.0.1",
      port = 8080,
      parallelism = 256,
      connectionContext = HttpConnectionContext(http2 = Always)
    )

    bound.foreach { binding =>
      println(s"gRPC server bound to: ${binding.localAddress}")
    }
    bound
  }

}

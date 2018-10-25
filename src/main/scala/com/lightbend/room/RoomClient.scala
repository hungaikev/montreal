package com.lightbend.room

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import akka.Done
import akka.NotUsed
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

object RoomClient {

  def main(args: Array[String]): Unit = {
    implicit val sys = ActorSystem("BookingClient")
    implicit val mat = ActorMaterializer()
    implicit val ec = sys.dispatcher

    val client = new RoomServiceClient(GrpcClientSettings.fromConfig("room.RoomService"))

    val rooms =
      if (args.isEmpty) List("Room1", "Room2", "Room3")
      else args.toList

    rooms.foreach(singleRequestReply)

    def singleRequestReply(room: String): Unit = {
      println(s"Performing request: $room")
      val reply = client.bookRoom(BookRoomRequest(room))
      reply.onComplete {
        case Success(msg) =>
          println(msg)
        case Failure(e) =>
          println(s"Error: $e")
      }
    }


  }


}


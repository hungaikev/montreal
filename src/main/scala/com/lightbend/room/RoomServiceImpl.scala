package com.lightbend.room

import scala.concurrent.Future

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.BroadcastHub
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.MergeHub
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source

class RoomServiceImpl(materializer: Materializer) extends RoomService {

  private implicit val mat: Materializer = materializer

  val (inboundHub: Sink[BookRoomRequest, NotUsed], outboundHub: Source[BookRoomReply, NotUsed]) =
    MergeHub.source[BookRoomRequest]
      .map(request => BookRoomReply(s"Booked you, ${request.name}"))
      .toMat(BroadcastHub.sink[BookRoomReply])(Keep.both)
      .run()

  override def bookRoom(in: BookRoomRequest): Future[BookRoomReply] = {
    Future.successful(BookRoomReply(s"BOOKED ROOM, ${in.name}"))
  }
}

## Sample GRPC Application

**RoomService** (request-response): Client sends a booking request at which the server response with a successfull booking response


### How to Run the Application

Compile the project 

```
sbt compile

```
In one Terminal

```
sbt "runMain com.lightbend.room.RoomServer"

```

In the second Terminal

```
sbt "runMain com.lightbend.room.RoomClient"

```
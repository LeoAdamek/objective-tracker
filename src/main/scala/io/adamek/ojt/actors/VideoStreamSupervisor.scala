package io.adamek.ojt.actors

import akka.actor.{ActorRef, Props, ActorLogging, Actor}
import akka.routing.{Router, RoundRobinRoutingLogic, ActorRefRoutee}
import org.opencv.videoio.VideoWriter

object VideoStreamSupervisor {
  case object StreamFinished
  case object StreamError
}

/**
  * Video Stream Suprvision Actor -- Supervises the video stream pipeline
  */
class VideoStreamSupervisor extends Actor with ActorLogging {
  var streamInput: ActorRef = null
  var streamOutput: ActorRef = null
  var outputStarted = false

  override def preStart(): Unit = {
    streamInput = context.actorOf(Props[VideoFileCaptureActor], "stream_input")
    streamOutput = context.actorOf(Props[VideoFileOuputActor], "stream_output")
    streamInput ! VideoFileCaptureActor.StartFileStream("/home/leo/documents/Rush_sample.mkv")
  }

  def router = {
    val routees = Vector.fill(4) {
      val pr = context.actorOf(Props[FaceTrackingProcessingActor])
      context watch pr
      ActorRefRoutee(pr)
    }

    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case VideoStreamSupervisor.StreamFinished => {
      streamOutput ! VideoStreamSupervisor.StreamFinished
      log.info("Video stream finished. Terminating")
      context.stop(self)
    }
    case frame: VideoStreamCaptureActor.VideoStreamFrame => {
      log.debug("Got frame:" + frame.toString)

      router.route(frame, self)
    }

    case processedFrame: VideoStreamProcessingActor.ProcessedFrame => {
      log.debug("Got a processed frame" + processedFrame.toString)
      streamInput ! VideoStreamCaptureActor.GetNextFrame

      outputStarted match {
        case false => {
          streamOutput ! VideoFileOuputActor.BeginStream(
            "/home/leo/documents/Rush_faces.mkv",
            VideoWriter.fourcc('H','2','6','4'),
            processedFrame.processedFrame)
          outputStarted = true
        }
        case true => {
          streamOutput ! processedFrame
        }
      }
    }
  }
}

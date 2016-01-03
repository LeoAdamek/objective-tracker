package io.adamek.ojt.actors

import akka.actor.{Actor, ActorLogging}
import org.opencv.core.Mat

object VideoStreamProcessingActor {
  case class ProcessedFrame(processedFrame: Mat)
  case class FrameProcessingError(error: Throwable)
}

/**
  * Abstract video stream processing actor
  */
abstract class VideoStreamProcessingActor extends Actor with ActorLogging {

  def processFrame(frame: Mat): Mat

  def receive = {
    case frame: VideoStreamCaptureActor.VideoStreamFrame => {
      try {
        val processedFrame = processFrame(frame.frame)
        sender ! VideoStreamProcessingActor.ProcessedFrame(processedFrame)
      } catch {
        case e: Throwable => {
          sender ! VideoStreamProcessingActor.FrameProcessingError(e)
        }
      }
    }
  }

}

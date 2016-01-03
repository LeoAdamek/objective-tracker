package io.adamek.ojt.actors

import akka.actor.{ActorLogging, Actor}
import org.opencv.core.Mat

/**
  * Created by leo on 02/01/16.
  */
abstract class VideoStreamOutputActor extends Actor with ActorLogging {

  def writeFrame(frame: Mat): Unit

  def receive = {
    case frame: VideoStreamCaptureActor.VideoStreamFrame => {
      writeFrame(frame.frame)
    }

    case frame: VideoStreamProcessingActor.ProcessedFrame => {
      writeFrame(frame.processedFrame)
    }
  }

}

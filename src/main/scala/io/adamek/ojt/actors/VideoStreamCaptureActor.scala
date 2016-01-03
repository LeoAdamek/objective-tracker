package io.adamek.ojt.actors

import akka.actor.{Actor, ActorLogging}
import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture

object VideoStreamCaptureActor {
  case class VideoStreamFrame(frame: Mat)
  case object GetNextFrame
}

/**
  * Created by leo on 02/01/16.
  */
abstract class VideoStreamCaptureActor extends Actor with ActorLogging {
  var captureSource: VideoCapture = null

  def getVideoCaptureSource() = captureSource

}

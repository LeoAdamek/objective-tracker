package io.adamek.ojt.actors

import io.adamek.ojt.actors.VideoFileCaptureActor.StartFileStream
import org.opencv.core.{Mat, CvException}
import org.opencv.videoio.{Videoio, VideoCapture}


object VideoFileCaptureActor {
  case class StartFileStream(filePath: String)
}

/**
  * Created by leo on 02/01/16.
  */
class VideoFileCaptureActor extends VideoStreamCaptureActor {

  def receive = {
    case start: StartFileStream => {
      startVideoStream(start.filePath)
    }

    case VideoStreamCaptureActor.GetNextFrame => {
      getNextFrame()
    }
  }

  private def startVideoStream(filePath: String): Unit = {
    log.info("Starting stream: " + filePath)

    try {
      captureSource = new VideoCapture(filePath)
    } catch {
      case e: CvException => {
        log.error("Error opening video stream: ${e}")
        sender ! VideoStreamSupervisor.StreamError
      }
    }

    getNextFrame
  }

  private def getNextFrame(): Unit = {

    if(captureSource.grab) {
      val frame = new Mat

      captureSource.read(frame)
      log.info("Capture Position: " + captureSource.get(Videoio.CAP_PROP_POS_MSEC))
      sender ! VideoStreamCaptureActor.VideoStreamFrame(frame)
    } else {
      sender ! VideoStreamSupervisor.StreamFinished
    }
  }
}

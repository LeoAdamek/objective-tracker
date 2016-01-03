package io.adamek.ojt.actors

import io.adamek.ojt.actors.VideoFileOuputActor.BeginStream
import org.opencv.core.{CvException, Size, Mat}
import org.opencv.videoio.VideoWriter

object VideoFileOuputActor {
  case class BeginStream(streamFile: String, videoFormat: Int, firstFrame: Mat)
}

/**
  * Stream output actor for writing output to a video file
  */
class VideoFileOuputActor extends VideoStreamOutputActor {

  val videoOutput = new VideoWriter()

  override def writeFrame(frame: Mat): Unit = {
    videoOutput.write(frame)
    log.info("Wrote a frame to output file")
  }

  override def receive = {
    case start: BeginStream => {
      log.info("Started Writing Ouput to " + start.streamFile)
      val isOpened = videoOutput.open(start.streamFile, start.videoFormat, 24, new Size(start.firstFrame.cols, start.firstFrame.rows))

      if(!isOpened) {
        log.error("Couldn't open file :(")
        sender ! VideoStreamSupervisor.StreamError
      }

      try {
        videoOutput.write(start.firstFrame)
      } catch {
        case cvError: CvException => {
          log.error("Error starting stream output: " + cvError.getMessage)
          log.error("Frame Info: ")
          log.error("Width: " + start.firstFrame.rows)
          log.error("Height:  " + start.firstFrame.cols)
          log.error("Channels: " + start.firstFrame.channels)

          sender ! VideoStreamSupervisor.StreamError
        }
      }
    }

    case VideoStreamSupervisor.StreamFinished => {
      videoOutput.release
    }

    case frame: VideoStreamProcessingActor.ProcessedFrame => {
      writeFrame(frame.processedFrame)
    }
  }

}

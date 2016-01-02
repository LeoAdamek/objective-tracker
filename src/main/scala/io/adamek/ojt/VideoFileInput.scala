package io.adamek.ojt

import com.typesafe.scalalogging.LazyLogging
import org.opencv.core.Mat
import org.opencv.videoio.{Videoio, VideoCapture}

/**
  * Created by leo on 01/01/16.
  */
class VideoFileInput(filePath: String) extends CaptureSource with LazyLogging {

  val captureSource = new VideoCapture()

  captureSource.open(filePath)

  override def getCaptureSource(): VideoCapture = captureSource

  override def getFrame(): Stream[Mat] = {
    if(!captureSource.grab) Stream.empty

    val capturedFrame = new Mat
    captureSource.read(capturedFrame)

    Stream.cons(capturedFrame, getFrame)
  }
}

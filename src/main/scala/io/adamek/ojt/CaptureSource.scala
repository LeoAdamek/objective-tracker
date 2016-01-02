package io.adamek.ojt

import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture

/**
  * Abstract image capture source
  */
abstract class CaptureSource {

  def getCaptureSource(): VideoCapture
  def getFrame(): Stream[Mat]

}

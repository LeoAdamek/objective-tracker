package io.adamek.ojt

import org.opencv.core.Mat

/**
  * Abstract image Processor
  */
abstract class ImageProcessor {
  def processCaptureSource(source: CaptureSource): Stream[Mat]
}

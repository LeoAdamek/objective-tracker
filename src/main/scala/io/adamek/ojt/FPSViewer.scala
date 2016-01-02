package io.adamek.ojt

import org.opencv.core.Mat


/**
  * An image viewer which displays the number of frames received each second.
  */
class FPSViewer(imageStream: Stream[Mat]) {
  var framesReceived: BigInt = 0

  imageStream.foreach(_ => {
    framesReceived += 1
  })
}

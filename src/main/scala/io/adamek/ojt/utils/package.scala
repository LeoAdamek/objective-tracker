package io.adamek.ojt

import java.awt.Image
import java.awt.image.{BufferedImage, DataBufferByte}
import javax.swing.UIManager

import org.opencv.core.Mat

/**
  * Created by leo on 01/01/16.
  */
package object utils {
  def toBufferedImage(imageMatrix: Mat): Image = {
    var imageType = BufferedImage.TYPE_BYTE_GRAY

    if (imageMatrix.channels > 1) {
      imageType = BufferedImage.TYPE_3BYTE_BGR
    }

    val bufferSize = imageMatrix.channels * imageMatrix.cols * imageMatrix.rows
    val buffer = new Array[Byte](bufferSize)

    imageMatrix.get(0, 0, buffer)

    val image = new BufferedImage(imageMatrix.cols, imageMatrix.rows, imageType)

    val targetPixels = image.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData

    System.arraycopy(buffer, 0, targetPixels, 0, buffer.length)

    image
  }

  def setSystemLookAndFeel(): Unit = {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

}

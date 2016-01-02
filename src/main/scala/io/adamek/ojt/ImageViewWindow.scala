package io.adamek.ojt

import javax.swing.{ImageIcon, JLabel, JFrame}

import org.opencv.core.Mat

/**
  * Image viewer using javax.swing
  */
class ImageViewWindow(imageStream: Stream[Mat]) {

  utils.setSystemLookAndFeel
  val mainFrame = new JFrame("Image View")
  val imageViewer = new JLabel

  mainFrame.add(imageViewer)
  mainFrame.setVisible(true)

  imageStream.foreach(imageFrame => {
    val bufferedImage = utils.toBufferedImage(imageFrame)
    val imageIcon = new ImageIcon(bufferedImage)

    imageViewer.setIcon(imageIcon)
    mainFrame.pack
  })
}

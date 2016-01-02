package io.adamek.ojt

import java.io.File

import org.opencv.core.{CvException, Core}
import org.slf4j.LoggerFactory

/**
  * Main executable app for ObjectiveTracker, loads OpenCV and prepares the RTE.
  */
object ObjectiveTrackerMain extends App {

  val logger = LoggerFactory.getLogger(this.getClass)

  logger.info(s"Starting Application: Using OpenCV library ${Core.NATIVE_LIBRARY_NAME}")

  // Load the OpenCV Library
  try {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  } catch {

    // We couldn't load OpenCV: Can't continue.
    case e: java.lang.UnsatisfiedLinkError => {
      logger.error("Error loading OpenCV!")
      logger.error("java.library.path = " + System.getProperty("java.library.path"))
      logger.error("Error Message: " + e.getMessage())

      System.exit(130)
    }
  }


  val captureSource = new VideoFileInput(args.last)
  val processor = new FaceTrackingProcessor
  val viewer = new FPSViewer(processor.processCaptureSource(captureSource))


}

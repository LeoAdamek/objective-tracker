package io.adamek.ojt.actors

import org.opencv.core.{Scalar, Point, MatOfRect, Mat}
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier

/**
  * Face tracking processor actor
  */
class FaceTrackingProcessingActor extends VideoStreamProcessingActor {

  val classifier = new CascadeClassifier(getClass.getResource("/lbpcascade_frontalface.xml").getPath)

  override def processFrame(frame: Mat): Mat = {
    val classifiedObjects = new MatOfRect
    classifier.detectMultiScale(frame, classifiedObjects)

    val processedFrame = new Mat
    frame.copyTo(processedFrame)

    log.info("Classified " + classifiedObjects.toArray.length + " objects")

    for(cobj <- classifiedObjects.toArray) {
      Imgproc.rectangle(
        processedFrame,
        new Point(cobj.x, cobj.y),
        new Point(cobj.x + cobj.width, cobj.y + cobj.height),
        new Scalar(0, 255, 255),
        2
      )
    }
    processedFrame
  }
}

package io.adamek.ojt

import com.typesafe.scalalogging.LazyLogging
import org.opencv.core.{Scalar, Point, MatOfRect, Mat}
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier

/**
  * An image processor which uses a cascading classifier with the OpenCV
  * reference "frontal face" training data
  */
class FaceTrackingProcessor extends ImageProcessor with LazyLogging {

  val classifier = new CascadeClassifier(getClass.getResource("/lbpcascade_frontalface.xml").getPath)

  val captureFrame  = new Mat
  val annotatedFrame = new Mat
  val classifiedRects = new MatOfRect

  override def processCaptureSource(source: CaptureSource): Stream[Mat] = {
    val capture = source.getCaptureSource()

    if(!capture.grab) return Stream.empty

    capture.read(captureFrame)
    classifier.detectMultiScale(captureFrame, classifiedRects)
    captureFrame.copyTo(annotatedFrame)
    val classifiedObjects = classifiedRects.toArray

    if (classifiedObjects.length > 1) {
      for (classifiedObject <- classifiedObjects) {
        Imgproc.rectangle(
          annotatedFrame,
          new Point(classifiedObject.x, classifiedObject.y),
          new Point(
            classifiedObject.x + classifiedObject.width,
            classifiedObject.y + classifiedObject.height
          ),
          new Scalar(0,255,255),
          2
        )

      }
    }
    return Stream.cons(annotatedFrame, processCaptureSource(source))
  }
}

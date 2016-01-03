package io.adamek.ojt

import org.opencv.core.Core

/**
  * Main class for Objective Tracker -- Spawns with Akka Main
  */
object Main extends App {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  akka.Main.main(Array(classOf[actors.VideoStreamSupervisor].getName))
}

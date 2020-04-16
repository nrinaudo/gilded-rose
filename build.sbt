name          := "gilded-rose"
version       := "1.0"
scalaVersion  := "2.13.1"
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "org.scalatest"     %% "scalatest"       % "3.1.1",
  "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1"
)

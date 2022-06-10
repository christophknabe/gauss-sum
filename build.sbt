//Import into IDEA preferably with option "Use SBT shell". Otherwise might not get compiled.
//Tested on Java 8 and Java 11.

name := "gauss-sum"


version := "1.1"

scalaVersion := "2.12.4"

lazy val akkaVersion = "2.5.12"

scalacOptions ++= Seq("-encoding", "UTF-8", "-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
   //vendor %% scalaVersionDependentArtifact % ownVersion % scope
  //"com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scalatest" %% "scalatest" % "3.0.4", // % "test",
  // Akka
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion
)

/*The operator %% builds an artifact name from the specified scalaVersionDependentArtifact name, 
* an underscore sign, and the upper mentioned scalaVersion. 
* So the artifact name will result here in scalatest_2.12,
* as the last number in a Scala version is not API relevant.
*/

//See http://www.scalatest.org/user_guide/using_scalatest_with_sbt
logBuffered in Test := false


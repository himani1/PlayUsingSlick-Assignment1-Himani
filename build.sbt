name := """helloworld"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.play"   %%   "play-slick"              %   "1.1.1",
  "com.typesafe.play"   %%   "play-slick-evolutions"   %   "1.1.1",
  "com.h2database"    % 	   "h2"                    %   "1.4.187" ,
   specs2 % Test
)
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "com.github.nscala-time" %% "nscala-time" % "2.10.0",
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P24-B3-SNAPSHOT"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

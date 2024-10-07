name := "MyProject"

version := "0.1"

scalaVersion := "3.5.0"

resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"
resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC5"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.3"
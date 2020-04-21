name := "CopyDynamo"
version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
libraryDependencies += "com.amazon.emr" % "emr-dynamodb-hadoop" % "4.12.0"


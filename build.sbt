import sbt._
import sbt.Keys._
import java.io.File

name := "simple-project"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0" 

mainClass in assembly := Some("WordCount")
//jarName in assembly := "simple-scala.jar"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { mergeStrategy => {  
 case entry => {  
   val strategy = mergeStrategy(entry)  
   if (strategy == MergeStrategy.deduplicate) MergeStrategy.first  
   else strategy  
 }  
}} 
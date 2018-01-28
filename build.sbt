name := "simple-project"

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.1"

mainClass in assembly := Some("WordCount")
//jarName in assembly := "simple-scala.jar"
//
//mergeStrategy in assembly := (mergeStrategy in assembly) { mergeStrategy => {
// case entry => {
//   val strategy = mergeStrategy(entry)
//   if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
//   else strategy
// }
//}}
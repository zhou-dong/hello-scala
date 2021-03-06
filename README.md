# hello-scala

## Step 1: install 

- install Spark
- install Scala
- install SBT
- install Eclipse (Scala IDE)

## Step 2: set up sbt environment

1. create file: build.sbt
2. create folder: project
3. create plugins.sbt(file) in project(folder)
4. add content in plugins.sbt file: 
	- addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.1.0")
	- addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4")
	
## Step 3: run command: sbt

- reload (optional)
- eclipse

## Step 4: 

- import project into eclipse
- add code into project

## Step 5: compile to jar and run jar

- sbt package
	+ keeps all the library JARs intact, 
	+ moves them into target/pack directory (as opposed to ivy cache where they would normally live), and makes a shell script for you to run them.
- sbt assembly
	+ creates a fat JAR
	+ a single JAR file containing all class files from your code and libraries. 

- Run command:
	+ java -jar project_name-assembly-x.y.jar

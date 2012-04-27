println "Creating files for new Gradle project ..."

def groupId = args[0]
def artifactId = args[1]
def version = args[2]

new File(".gitignore").withPrintWriter{ w ->
     "*.iml,*.project,*.metadata,*..settings,*.classpath,target,build,.gradle".split(",").each{ 
         w.println(it)
     }
}

new File("build.gradle") << """\
apply plugin: 'groovy'
apply plugin: 'idea'
apply plugin: 'eclipse'
 
repositories {
    mavenCentral()
	maven {url "http://10.25.12.20:8081/artifactory/development-repo"}
	maven {url "http://10.25.12.20:8081/artifactory/libs-release"}
	maven {url "http://10.25.12.20:8081/artifactory/plugins-release"}
}
 
dependencies {
    groovy 'org.codehaus.groovy:groovy:1.8.6'
    compile 'org.apache.ivy:ivy:2.2.0'
}
 
task createSourceDirs(description : 
   'Create empty source directories for all defined sourceSets') 
   << {
        sourceSets*.allSource.srcDirs.flatten().each { 
            File sourceDirectory ->        
            if (!sourceDirectory.exists()) {
                println "Making \$sourceDirectory"
                sourceDirectory.mkdirs()
            }
        }
}
 
idea {
    project {
        jdkName = '1.6'
    }
}

""" // end content

"cmd /c gradle createSourceDirs".execute()

"cmd /c git init".execute()

Thread.start{
	sleep 5000 // allow time for all files to be created
	new File(".").eachFile{
		println it
	}
}
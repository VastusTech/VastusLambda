# How to maven this big boi

So when you're packaging this guy, there's a few things you need to worry about while trying to make sure that 
you're correctly making the `.jar` file. So I already included the `pom.xml` file that's necessary for the 
compilation, but if you want to add another external jar to this project, here's what you have to do. 

You have to add it using the IntelliJ "Project Structure -> Libraries -> '+' " strategy. There are two options: 
either download it as a Maven Repo or add it as a Java library.
    
## As a Maven Repo

If you find this repo on the maven website and find the "<dependency>" script, then it's very easy, you just simply 
include that statment into the `pom.xml` where all the other dependencies are. Once this is done, you can also use 
the above IntelliJ strategy so that you can write with awesome autocomplete, but that's up to you. (When you add it 
that way, you don't have to include it in the `lib` folder). 

## As an external Java jar

Now this is a little trickier because not only do you want to include it in the shade-compilation, you also want it 
to work on runtime. So for this, you need to make a repository for it. First, go into the `pom.xml` file and include 
a section near the top like this:

```xml
<repositories>
  <repository>
    <id>name-describing-jar-repository</id>
    <url>file://${project.basedir}/lib/name-describing-jar-repository</url>
  </repository>
</repositories>
```

This will define a Maven repository inside the project and allow you to store external libraries in it. In order to 
store the specific jar into the repository, however, you need to do a simple command on the command line.

```bash
$ mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file \
    -Dfile=<Path to your jar file (i reccomend it to be in the lib folder)> \
    -DgroupId=<groupId for the jar (can be found inside the actual jar sometimes)> \
    -DartifactId=<artifactId for the jar (can be found inside sometimes) \
    -Dversion=<version for the jar (can be found inside) \
    -Dpackaging=jar \
    -DlocalRepositoryPath=<Path to the local repository you defined in the pom.xml file>
```

This will install the jar into that repo and allow you to use it. The last thing you do is you take the groupId, 
artifactId, and the version to make a dependency statement that fits the description of the others. It will look like
 this:
 
 ```xml
<dependencies>
  <dependency>
    <groupId>[the jar's groupId]</groupId>
    <artifactId>[the jar's artifactId]</artifactId>
    <version>[the jar's version]</version>
  </dependency>
</dependencies>
```
    
    
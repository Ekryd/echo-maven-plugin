# Introduction #

This cookbook will tell you how to achieve different outputs with the Echo-maven-plugin.

# Table of Contents #



# How do I ... #

Each recipe will present a question and then tell the solution to it.

## configure my maven build to use the plugin? ##

Add the following section to your pom-file. The section should be placed under project->build->plugins.
Observe that you need configure a message to output too.

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>

  ...

  <build>
    <plugins>
      <plugin>
        <groupId>com.google.code.echo-maven-plugin</groupId>
        <artifactId>echo-maven-plugin</artifactId>
        <version>1.0.0</version>
        <inherited>false</inherited>
        <configuration>
          <!-- You will place configuration stuff here -->
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>echo</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      ...

    </plugins>
  </build>

  ...

</project>
```

## find a live example of using the plugin? ##

Have a look at my [SortPom](https://code.google.com/p/sortpom/) plugin that uses the echo maven plugin. See [Pom-file](https://code.google.com/p/sortpom/source/browse/pom.xml)

## output a message during a build? ##

Add the following in the configuration:
```
<message>All you base are belong to us!</message>
```

## output a newline/line separator in the message? ##

Add the following in the configuration:
```
<message>${line.separator}   ->  Please use -Ddev to compile without signing the output${line.separator}</message>
```

## output system properties in a message? ##

Java system properties can be found [here](http://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html) and [here](http://books.sonatype.com/mvnref-book/reference/resource-filtering-sect-properties.html)
Add the following in the configuration:
```
<message>You are using Java version: ${java.version}, Maven timestamp: ${maven.build.timestamp}</message>
```

## output variables in a message? ##

Properties are defined in the properties section of the pom-file :
```
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  ...

  <properties>
    <compileSource>1.6</compileSource>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <my.property>Special value</my.property>
  </properties>

  ...

  <build>
  </build>

</project>
```
Add the following in the configuration:
```
<message>This is the value: ${my.property}</message>
```

The current pom-file can also be referenced:
```
<message>Submodule ${project.artifactId}. All tests are ok</message>
```
## output a message from a file? ##

Create a file in the project root (same level as the pom-file). Add the following in the configuration:
```
<fromFile>welcome.txt</fromFile>
```

## debug the echoed message? ##

Sometimes you need to know why a message is scrambled, especially of special characters are output. Add the following in the configuration:
```
<message>Test åäö</message>
<characterOutput>true</characterOutput>
```

This will output the message as well as character ascii codes:
```
[info] [['T' , 84 ],['e' , 101 ],['s' , 115 ],['t' , 116 ],[' ' , 32 ],['å' , 229 ],['ä' , 228 ],['ö' , 246 ]]
[info] Test åäö
```

## output a message to a file? ##

Add the following in the configuration:
```
<message>Starting build with timestamp: ${maven.build.timestamp}${line.separator}</message>
<toFile>output.txt</toFile>
```

## append a message to a file? ##

Add the following in the configuration:
```
<message>Starting build with timestamp: ${maven.build.timestamp}${line.separator}</message>
<toFile>output.txt</toFile>
<append>true</append>
```

## overwrite a read-only file? ##

Add the following in the configuration:
```
<message>Starting build with timestamp: ${maven.build.timestamp}${line.separator}</message>
<toFile>output.txt</toFile>
<force>true</force>
```

## standardize the line separator in a message? ##

The line separator can be either \n, \r\n or \r. Add the following in the configuration:
```
<message>${line.separator}   ->  This is a ${line.separator}line ${line.separator}separated message</message>
<toFile>output.txt</toFile>
<lineSeparator>\r\n</lineSeparator>
```

## change encoding for a message? ##

The encoding will affect both fromFile and toFile settings. Add the following in the configuration:
```
<message>Test åäö</message>
<toFile>output.txt</toFile>
<encoding>UTF-16</encoding>
```

## change the output level of a message? ##

The plugin have the 5 different output levels:
  * DEBUG - This message is only shown if maven is used with switch -X
  * INFO - Default level
  * WARNING - Will be shown as warning level in maven build
  * ERROR - Will be shown as error level in maven build
  * FAIL - Will stop the build

Add the following in the configuration:
```
<message>You are probably using the wrong profile. This one is for the build server!!!</message>
<level>WARNING</level>
```

## stop the build with an error message? ##

This will actually stop the build by throwing an exception:
```
<message>This is a synthetic Maven profile. Never use this when compiling!</message>
<level>FAIL</level>
```

## change when the message is output during the build? ##

The default phase for the plugin is during the initialization, so that messages are shown when the build starts. There are times when the message should be output in other phases. I.e. to write a log message each time all tests are passed during automated tests.

This could be an entry in a multi-module Maven setup. The entry would be in each sub module and the results would be aggregated beside the super pom.
Please notice that phase has been set to 'verify' (which is after phase 'test').
```
      <plugin>
        <groupId>com.google.code.echo-maven-plugin</groupId>
        <artifactId>echo-maven-plugin</artifactId>
        <version>1.0.0</version>
        <inherited>false</inherited>
        <configuration>
          <message>Submodule ${project.artifactId}. All tests are ok, timestamp: ${maven.build.timestamp} ${line.separator}</message>
          <!-- Aggregated in parent folder -->
          <toFile>../build.txt</toFile>
          <append>true</append>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>echo</goal>
            </goals>
            <!-- Different phase -->
            <phase>verify</phase>
          </execution>
        </executions>
      </plugin>
```

## run the plugin without a project? ##

The plugin is not dependent on a pom-file:
```
mvn com.google.code.echo-maven-plugin:echo-maven-plugin:1.0.0:echo -Decho.message="No strings attached"
```

# Questions #
Add an [issue](https://code.google.com/p/echo-maven-plugin/issues/list)
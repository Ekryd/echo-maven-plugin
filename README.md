# Echo Maven Plugin ![Icon](https://raw.githubusercontent.com/Ekryd/echo-maven-plugin/master/misc/echo.png)

[![Build Status](https://travis-ci.org/Ekryd/echo-maven-plugin.svg?branch=master)](https://travis-ci.org/Ekryd/echo-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/Ekryd/echo-maven-plugin/badge.svg?branch=master)](https://coveralls.io/r/Ekryd/echo-maven-plugin?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.echo-maven-plugin/echo-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.echo-maven-plugin/echo-maven-plugin)

A maven plugin that lets you output text during Maven build

It can be used with a file:

```xml
      <plugin>
        <groupId>com.github.ekryd.echo-maven-plugin</groupId>
        <artifactId>echo-maven-plugin</artifactId>
        <version>1.2.0</version>
        <inherited>false</inherited>
        <executions>
          <execution>
            <id>welcome</id>
            <goals>
              <goal>echo</goal>
            </goals>
            <configuration>
              <fromFile>misc/welcome.txt</fromFile>
            </configuration>
          </execution>
        </executions>
      </plugin>

```

Or with a string directly (note that Maven properties can be used):

```xml
      <plugin>
        <groupId>com.github.ekryd.echo-maven-plugin</groupId>
        <artifactId>echo-maven-plugin</artifactId>
        <version>1.2.0</version>
        <inherited>false</inherited>
        <executions>
          <execution>
            <id>end</id>
            <goals>
              <goal>echo</goal>
            </goals>
            <phase>install</phase>
            <configuration>
              <message>${line.separator}       To run the plugin directly:${line.separator}       mvn ${project.groupId}:${project.artifactId}:${project.version}:sort${line.separator}</message>
            </configuration>
          </execution>
        </executions>
      </plugin>


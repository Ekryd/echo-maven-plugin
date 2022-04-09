# Echo Maven Plugin ![Icon](https://raw.githubusercontent.com/Ekryd/echo-maven-plugin/master/misc/echo.png)

[![Build Status](https://circleci.com/gh/Ekryd/echo-maven-plugin.svg?style=svg)](https://app.circleci.com/pipelines/github/Ekryd/echo-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/github/Ekryd/echo-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/Ekryd/echo-maven-plugin?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.echo-maven-plugin/echo-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.echo-maven-plugin/echo-maven-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.ekryd.echo-maven-plugin%3Aecho-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.ekryd.echo-maven-plugin%3Aecho-plugin)

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
```

See more in the [Cookbook](https://github.com/Ekryd/echo-maven-plugin/wiki/Cookbook)

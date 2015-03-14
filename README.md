# Echo-Maven-Plugin #
Maven plugin that outputs text during a maven build. The plugin is an alternative to the using antrun plugin and the echo ant command.

## News ##
  * 2014-04-21: Released 1.0.0. Fixed bugs, [issue 1](https://code.google.com/p/echo-maven-plugin/issues/detail?id=1) and [issue 2](https://code.google.com/p/echo-maven-plugin/issues/detail?id=2)
  * 2014-04-19: Documentation should be complete
  * 2014-04-18: More documentation
  * 2014-03-18: Released 0.0.1. Starting to write documentation.

## Goals Overview ##
The Echo Plugin has one goal: **echo** which outputs the message.

The plugin should emulate the ant command 'echo', but with the following exceptions:
  * The attribute 'file' is called 'toFile'
  * The attribute 'output' does not exist
  * The default value for the attribute 'level' is INFO, not WARN.

## How to use the plugin ##
[Cookbook](Cookbook.md)

## Example ##
Maven users can add this plugin with the following addition to their pom.xml file. This will output a message at the start of the build. The text in the message tag can be what you want.

```
<build>
  <plugins>
      <plugin>
        <groupId>com.google.code.echo-maven-plugin</groupId>
        <artifactId>echo-maven-plugin</artifactId>
        <version>1.0.0</version>
        <inherited>false</inherited>
        <configuration>
        <message>${line.separator} Notice: please use -Ddev to compile without signing the jar${line.separator}</message>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>echo</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
  </plugins>
</build>
```

## Version History ##
[Versions](Versions.md)

## Download ##
The plugin is hosted i [Maven Central](http://mvnrepository.com/artifact/com.google.code.echo-maven-plugin/echo-maven-plugin) and will be downloaded automatically if you include it as a plugin in your pom file.

## Donations ##
If you use it, then please consider some encouragement.

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=JB25X84DDG5JW&lc=SE&item_name=Encourage%20the%20development&item_number=Echo%2dmaven%2dplugin&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

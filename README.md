# I2b2 Export
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It is an i2b2 plugin and Java web service for exporting patient data to CSV files. 
This project includes three modules:
* i2b2-export-plugin: the web client plugin that the user interacts with
* i2b2-export-service: the Java service that manages users' export configurations and communicates with i2b2 to retrieve data
* i2b2-export-package: packages up the plugin and service into a single archive for distribution

## Version 1.1 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/i2b2-export/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/i2b2-export)

The 1.1 development series will migrate this package to the Eureka! Clinical build system.

## Version history
### Version 1.0
Initial release.

## Build requirements
* [Oracle Java JDK 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.2.5 or greater](https://maven.apache.org)

## Runtime requirements
* [Oracle Java JRE 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Tomcat 7](https://tomcat.apache.org) (for `i2b2-export-service`)
* [i2b2 version 1.7](http://www.i2b2.org)

## Building it
The project uses the maven build tool. Typically, you build it by invoking `mvn clean install` at the command line. For simple file changes, not additions or deletions, you can usually use `mvn install`. See https://github.com/eurekaclinical/dev-wiki/wiki/Building-Eureka!-Clinical-projects for more details.

## Installing it
See the READMEs for the `i2b2-export-plugin` and `i2b2-export-service` modules for installation instructions.

## Getting help
Feel free to contact us at help@eurekaclinical.org.


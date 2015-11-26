# gradle-camel-plugin

```groovy
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath 'com.github.lburgazzoli:gradle-camel-plugin:0.0.6-SNAPSHOT'
    }
}

apply plugin: 'com.github.lburgazzoli.camel'

group 'com.github.lburgazzoli'
version '1.0-SNAPSHOT'

ext {
    versions = [
        camel       : '2.16.1',
        slf4j       : '1.7.13',
        log4j2      : '2.4.1',
        weld        : '2.3.1.Final',
        deltaspike  : '1.5.1'
    ]
}

sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

    maven { url = 'https://oss.sonatype.org/content/repositories/snapshots/' }
    maven { url = 'https://repository.apache.org/content/repositories/snapshots/' }

}

dependencies {
    compile "org.slf4j:slf4j-api:$versions.slf4j"

    compile "org.apache.camel:camel-spring:$versions.camel"
    compile "org.apache.camel:camel-spring-javaconfig:$versions.camel"
    compile "org.apache.camel:camel-test-blueprint:$versions.camel"
    compile "org.apache.camel:camel-cdi:$versions.camel"

    compile "org.jboss.weld.se:weld-se:$versions.weld"
    compile "org.jboss.weld:weld-core:$versions.weld"
    compile "org.apache.deltaspike.cdictrl:deltaspike-cdictrl-weld:$versions.deltaspike"

    runtime "org.apache.logging.log4j:log4j-core:$versions.log4j2"
    runtime "org.apache.logging.log4j:log4j-slf4j-impl:$versions.log4j2"
    runtime "org.apache.logging.log4j:log4j-jul:$versions.log4j2"
}

camelRun {
    trace = true
    //duration = '60s'

    //springJavaConfig {
    //    configClass 'com.github.lburgazzoli.gradle.plugin.examples.camel.MySpringRouteConfiguration'
    //}

    //spring {
    //}

    //blueprint {
    //}

    cdi {
        // Workaround, needs to be fixed
        additionalPath jar.archivePath 
    }

    jvmArgs = [
        "-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager"
    ]
}

```

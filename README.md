# tapestry-geb [![Build Status](https://travis-ci.org/eddyson-de/tapestry-geb.svg?branch=master)](https://travis-ci.org/eddyson-de/tapestry-geb)
Use Geb (http://www.gebish.org/) to test Tapestry (http://tapestry.apache.org/) web applications.

This library provides basic integration for testing Tapestry webapps with Geb.
The original idea was cheekily stolen from https://tawus.wordpress.com/2012/08/03/spock-runjetty/.

## Usage

A basic usage example to load an application's Index page and check the page title.

### `build.gradle`:
```groovy
respositories {
  jcenter()
}

dependencies {
  testCompile 'de.eddyson:tapestry-geb:0.31.1'
  testCompile "org.seleniumhq.selenium:selenium-firefox-driver:3.0.1"
}


tasks.withType(Test){
  systemProperty 'tapestry.execution-mode', 'test'
  systemProperty 'jettyPort', 9023
  systemProperty 'webappLocation', 'src/main/webapp'
  systemProperty 'geb.env', 'firefox'
}
```

### `src/test/groovy/GebConfig.groovy`:
```groovy
import org.openqa.selenium.firefox.FirefoxDriver

reportsDir = 'build/reports/geb'
baseUrl = "http://localhost:${System.properties['jettyPort']}/"
environments {
  firefox {
    driver = { new FirefoxDriver() }
  }
}
```

### `src/test/groovy/org/exaple/app/integration/SimpleTest.groovy`:
```groovy
package org.example.app.integration

import de.eddyson.tapestrygeb.JettyGebSpec

class SimpleTest extends JettyGebSpec {

  def "Load index page"(){
    when:
      go page
    then:
      title == "Example application"
  }
}
```

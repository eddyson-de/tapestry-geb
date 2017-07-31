package de.eddyson.tapestrygeb

import static org.openqa.selenium.logging.LogType.BROWSER

import javax.servlet.ServletContext;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import geb.spock.GebReportingSpec
import groovy.transform.TypeChecked

@RunJetty
@TypeChecked
abstract class JettyGebSpec extends GebReportingSpec {

  protected static Logger logger = LoggerFactory.getLogger(JettyGebSpec)

  Registry getRegistry(){
    WebAppContext webappContext = (WebAppContext) JettyExtension.runner.server.handler
    ServletContext servletContext = webappContext.getServletContext()
    (Registry) servletContext.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME)
  }

  def getService(Class serviceInterface){
    getRegistry().getService(serviceInterface)
  }

  def setup() {
    RunJetty runJetty = getRunJettyAnnotation()
  }

  protected RunJetty getRunJettyAnnotation() {
    RunJetty runJetty = this.class.getAnnotation(RunJetty);
    if (runJetty == null) {
      runJetty = JettyGebSpec.getAnnotation(RunJetty.class);
    }
    return runJetty;
  }

  // from https://gist.github.com/antony/5bc8d768946972ab0a2bfca57b857313
  @TypeChecked
  void cleanup() {
    def specName = specificationContext.currentSpec.name
    def iterationName = specificationContext.currentIteration.name
    try {
      def logEntries = browser.driver.manage().logs().get(BROWSER).all
      println "START WebDriver $BROWSER logs for $specName.$iterationName"
      logEntries.each { println(it) }
      println "END WebDriver $BROWSER logs for $specName.$iterationName"
    } catch (error) {
      logger.warn("Failed to retrieve browser console logs: {}", error.message)
    }
  }
}

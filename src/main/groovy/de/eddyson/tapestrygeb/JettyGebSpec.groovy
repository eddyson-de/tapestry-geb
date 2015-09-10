package de.eddyson.tapestrygeb

import javax.servlet.ServletContext;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.eclipse.jetty.webapp.WebAppContext;

import geb.spock.GebReportingSpec

@RunJetty
abstract class JettyGebSpec extends GebReportingSpec {
  
  Registry getRegistry(){
    WebAppContext webappContext = JettyExtension.runner.server.handler
    ServletContext servletContext = webappContext.getServletContext()
    servletContext.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME)
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
}

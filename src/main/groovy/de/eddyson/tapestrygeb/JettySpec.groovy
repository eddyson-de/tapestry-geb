package de.eddyson.tapestrygeb

import org.apache.tapestry5.ioc.Registry
import spock.lang.Specification

@RunJetty
abstract class JettySpec extends Specification {

  Registry getRegistry(){
    return JettyExtension.getRegistry()
  }

  def getService(Class serviceInterface){
    return getRegistry().getService(serviceInterface)
  }

  def getJettyPort() {
    return Integer.parseInt(System.getProperty("jettyPort"))
  }

  def getJettyBaseUrl() {
    return "http://localhost:" + getJettyPort()
  }
}

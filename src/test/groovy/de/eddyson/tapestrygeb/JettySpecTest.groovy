package de.eddyson.tapestrygeb

import org.apache.tapestry5.SymbolConstants
import org.apache.tapestry5.ioc.services.SymbolSource
import spock.lang.Shared

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class JettySpecTest extends JettySpec {

  @Shared
  HttpClient http = HttpClient.newBuilder().build()

  def "Can access test page"() {
    when:
      HttpRequest req = HttpRequest.newBuilder(URI.create("${getJettyBaseUrl()}/test")).build()
      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString())
    then:
      res.statusCode() == 200
      res.body().contains("<pre>ok</pre>")
  }

  def "Can access test rest endpoint"() {
    when:
      HttpRequest req = HttpRequest.newBuilder(URI.create("${getJettyBaseUrl()}/rest/test")).build()
      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString())
    then:
      res.statusCode() == 200
      res.body() == "ok"
  }

  def "getRegistry works"() {
    when:
      def service = getRegistry().getService(SymbolSource)
    then:
      service != null
      // smoke test to see if service really works
      service.valueForSymbol(SymbolConstants.APPLICATION_VERSION) == "0.0.1"
  }
}

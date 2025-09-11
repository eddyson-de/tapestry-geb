package de.eddyson.tapestrygeb

import spock.lang.Shared
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class JettyRunnerTest extends Specification {

  @Shared
  HttpClient http = HttpClient.newBuilder().build()

  def "Can start JettyRunner from command line"() {
    setup:
      int port = 32379
      def t = new Thread({
        JettyRunner.main(["--directory", "src/test/webapp", "--port", "${port}", "--context", "/foo"] as String[])
      })
      t.setDaemon(true)
      t.start()
      sleep(1000)
    when:
      HttpRequest req = HttpRequest.newBuilder(URI.create("http://127.0.0.1:${port}/foo/test")).build()
      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString())
    then:
      res.statusCode() == 200
      res.body().contains("<pre>ok</pre>")
  }
}

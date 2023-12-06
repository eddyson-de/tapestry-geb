package de.eddyson.tapestrygeb

class JettyGebSpecTest extends JettyGebSpec {

  def "Can navigate to test page"() {
    setup:
      to TestPageGeb
    when:
      def pre = $("pre").text()
    then:
      pre == "ok"
  }
}

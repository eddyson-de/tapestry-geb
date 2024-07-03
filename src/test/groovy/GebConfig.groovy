import org.openqa.selenium.firefox.FirefoxDriver

reportsDir = "build/reports/geb"
baseUrl = "http://localhost:${System.getProperty("jettyPort")}/"

environments {
  "firefox" {
    driver = {
      new FirefoxDriver()
    }
  }
}

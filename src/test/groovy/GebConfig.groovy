import org.openqa.selenium.chrome.ChromeDriver

reportsDir = "build/reports/geb"
baseUrl = "http://localhost:${System.getProperty("jettyPort")}/"

environments {
  "chrome" {
    driver = {
      new ChromeDriver()
    }
  }
}

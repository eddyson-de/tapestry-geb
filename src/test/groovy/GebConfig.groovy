import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.firefox.FirefoxDriver

reportsDir = "build/reports/geb"
baseUrl = "http://localhost:${System.getProperty("jettyPort")}/"

environments {
  "firefox" {
    driver = {
      WebDriverManager.firefoxdriver().setup()
      new FirefoxDriver()
    }
  }
}

import de.eddyson.tapestrygeb.TapestryPage

class TestPageGeb extends TapestryPage {
  // pages can define their location, either absolutely or relative to a base
  static url = "test"

  // “at checkers” allow verifying that the browser is at the expected page
  static at = { title == "TestPage" }
}

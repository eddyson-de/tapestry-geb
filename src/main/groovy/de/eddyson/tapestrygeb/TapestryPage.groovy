package de.eddyson.tapestrygeb

import geb.Page

abstract class TapestryPage extends Page {

  @Override
  public void onLoad(Page previousPage) {
    waitFor { $('body').@'data-page-initialized' == 'true' }

    super.onLoad(previousPage)
  }
}

package de.eddyson.tapestrygeb;

import java.io.File;

import org.apache.tapestry5.test.Jetty7Runner;

public class NonExpandingJetty7Runner extends Jetty7Runner {

  @Override
  protected String expand(final String moduleLocalPath) {
    {
      File path = new File(moduleLocalPath);

      // Don't expand if the path provided already exists.
      if (path.isAbsolute() && (path.isDirectory() || path.isFile() && path.getName().endsWith(".war"))) {
        return moduleLocalPath;
      }

    }
    return super.expand(moduleLocalPath);
  }

}

package de.eddyson.tapestrygeb;

import org.eclipse.jetty.ee8.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;

public class JettyRunner {
  private Server jettyServer;
  private String description;

  private int port;

  public JettyRunner() {
    // un-configured runner
  }

  public JettyRunner(String webappFolder, String contextPath, int port) {
    configure(webappFolder, contextPath, port);
  }

  public void configure(String webappFolder, String contextPath, int port) {
    this.port = port;

    description = String.format("<JettyRunner: %s:%s (%s)", contextPath, port, webappFolder);

    jettyServer = new Server(port);


    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath(contextPath);
    webapp.setWar(webappFolder);

    jettyServer.setHandler(webapp);
  }

  public void start() throws Exception {
    jettyServer.start();
  }

  /**
   * Immediately shuts down the server instance.
   */
  public void stop() {
    System.out.printf("Stopping Jetty instance on port %d\n", port);

    try {
      // Stop immediately and not gracefully.
      jettyServer.stop();
    } catch (Exception ex) {
      throw new RuntimeException("Error stopping Jetty instance: " + ex, ex);
    }

    System.out.println("Jetty instance has stopped.");
  }

  public Server getServer() {
    return jettyServer;
  }

  @Override
  public String toString() {
    return description;
  }
}

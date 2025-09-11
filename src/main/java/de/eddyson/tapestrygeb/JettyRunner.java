package de.eddyson.tapestrygeb;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;

import java.io.File;

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

  /**
   * Main entrypoint used to run the Jetty instance from the command line.
   * Or in a JavaExec gradle task.
   * <br>
   * Arguments:
   * <ul>
   *   <li>-d, --directory | Webapp directory (defaults to 'src/main/webapp')</li>
   *   <li>-c, --context   | Context path for application (defaults to '/')</li>
   *   <li>-p, --port      | HTTP port (defaults to 8080)</li>
   * </ul>
   * <br>
   * Gradle example task:
   * <pre>
   * {@code
   * tasks.register("runTestApp", JavaExec) {
   *   mainClass = "de.eddyson.tapestrygeb.JettyRunner"
   *   args "-d", "src/test/webapp/", "-p", "9040"
   *   systemProperties["tapestry.execution-mode"] = "test"
   *   classpath = configurations.testRuntimeClasspath + sourceSets.test.output + sourceSets.main.output
   * }
   * }
   * </pre>
   */
  public static void main(String[] args) throws Exception {
    String webapp = "src/main/webapp";
    String context = "/";
    int httpPort = 8080;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-d":
        case "--directory":
          if (i + 1 < args.length) {
            webapp = args[i + 1];
            i++;
          } else {
            System.err.println("No value provided for --directory");
            System.exit(-1);
          }
          break;
        case "-c":
        case "--context":
          if (i + 1 < args.length) {
            context = args[i + 1];
            i++;
          } else {
            System.err.println("No value provided for --context");
            System.exit(-1);
          }
          break;
        case "-p":
        case "--port":
          if (i + 1 < args.length) {
            httpPort = Integer.parseInt(args[i + 1]);
            i++;
          } else {
            System.err.println("No value provided for --port");
            System.exit(-1);
          }
          break;
        default:
          System.err.println("Unknown argument: " + args[i]);
          System.exit(-1);
      }
    }

    if (!new File(webapp).exists()) {
      System.err.printf("Directory `%s' does not exist.\n", webapp);
      System.exit(-1);
    }

    new JettyRunner(webapp, context, httpPort).start();
  }
}

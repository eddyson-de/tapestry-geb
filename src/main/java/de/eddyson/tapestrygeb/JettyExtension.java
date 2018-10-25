package de.eddyson.tapestrygeb;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.test.Jetty7Runner;
import org.eclipse.jetty.webapp.WebAppContext;
import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;

import javax.servlet.ServletContext;

public class JettyExtension extends AbstractAnnotationDrivenExtension<RunJetty> {

  private boolean isSpecAnnotated;

  static Jetty7Runner runner = null;

  private static boolean shutdownHookAdded = false;

  static Registry getRegistry() {
    WebAppContext webappContext = (WebAppContext) runner.getServer().getHandler();
    ServletContext servletContext = webappContext.getServletContext();
    return (Registry) servletContext.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
  }

  @Override
  public void visitSpec(final SpecInfo spec) {

    IMethodInterceptor interceptor = new InjectInterceptor(spec);
    spec.addSharedInitializerInterceptor(interceptor);
    spec.addInitializerInterceptor(interceptor);
    super.visitSpec(spec);
  }

  @Override
  public void visitSpecAnnotation(final RunJetty runJetty, final SpecInfo specInfo) {
    isSpecAnnotated = true;
    specInfo.getBottomSpec().addListener(new AbstractRunListener() {

      @Override
      public void beforeSpec(final SpecInfo specInfo) {
        if (!shutdownHookAdded) {

          Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
              if (runner != null) {
                runner.stop();
              }

            }
          }));
          shutdownHookAdded = true;
        }
        if (runner == null || runJetty.restart()) {
          if (runner != null) {
            runner.stop();
            runner = null;
          }
          runner = createRunner();
        }
      }

    });

  }

  @Override
  public void visitFeatureAnnotation(final RunJetty runJetty, final FeatureInfo featureInfo) {
    if (isSpecAnnotated) {
      throw new RuntimeException(String.format(
          "A single specification cannot have both Specification and Feature annotated " + "by %s",
          RunJetty.class.getSimpleName()));
    }

    featureInfo.getParent().addListener(new AbstractRunListener() {

      @Override
      public void beforeFeature(final FeatureInfo featureInfo) {
        if (runner == null || runJetty.restart()) {
          if (runner != null) {
            runner.stop();
            runner = null;
          }
          runner = createRunner();
        }
      }

    });
  }

  private NonExpandingJetty7Runner createRunner() {
    try {
      NonExpandingJetty7Runner runner = new NonExpandingJetty7Runner();

      String webappLocationProperty = assertSystemPropertySet("webappLocation");
      String jettyPortProperty = assertSystemPropertySet("jettyPort");

      runner.configure(webappLocationProperty, "", Integer.parseInt(jettyPortProperty), -1).start();
      return runner;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String assertSystemPropertySet(final String propertyName) {
    String value = System.getProperty(propertyName);
    if (value == null) {
      throw new IllegalStateException("System property '" + propertyName + "' is not set!");
    }
    return value;
  }
}
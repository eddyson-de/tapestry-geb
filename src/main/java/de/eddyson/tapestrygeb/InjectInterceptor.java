package de.eddyson.tapestrygeb;

import org.apache.tapestry5.commons.AnnotationProvider;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.spockframework.runtime.extension.AbstractMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FieldInfo;
import org.spockframework.runtime.model.SpecInfo;
import org.spockframework.util.ReflectionUtil;
import spock.lang.Shared;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class InjectInterceptor extends AbstractMethodInterceptor {

  private final SpecInfo spec;

  public InjectInterceptor(final SpecInfo spec) {
    this.spec = spec;
  }

  @Override
  public void interceptSharedInitializerMethod(final IMethodInvocation invocation) throws Throwable {
    injectServices(invocation.getSharedInstance(), true);
    invocation.proceed();
  }

  @Override
  public void interceptInitializerMethod(final IMethodInvocation invocation) throws Throwable {
    injectServices(invocation.getInstance(), false);
    invocation.proceed();
  }

  private void injectServices(final Object target, final boolean sharedFields) throws IllegalAccessException {

    for (final FieldInfo field : spec.getAllFields()) {
      Field rawField = field.getReflection();
      if ((rawField.isAnnotationPresent(Inject.class)
          || ReflectionUtil.isAnnotationPresent(rawField, "javax.inject.Inject")
          || ReflectionUtil.isAnnotationPresent(rawField, "jakarta.inject.Inject")
          || rawField.isAnnotationPresent(Autobuild.class))
          && rawField.isAnnotationPresent(Shared.class) == sharedFields) {
        Object value = JettyExtension.getRegistry().getObject(rawField.getType(), createAnnotationProvider(field));
        rawField.setAccessible(true);
        rawField.set(target, value);
      } else if (rawField.isAnnotationPresent(InjectService.class)) {
        String serviceName = rawField.getAnnotation(InjectService.class).value();
        Object value = JettyExtension.getRegistry().getService(serviceName, rawField.getType());
        rawField.setAccessible(true);
        rawField.set(target, value);
      }
    }
  }

  private static AnnotationProvider createAnnotationProvider(final FieldInfo field) {
    return new AnnotationProvider() {
      @Override
      public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
      }
    };
  }

}

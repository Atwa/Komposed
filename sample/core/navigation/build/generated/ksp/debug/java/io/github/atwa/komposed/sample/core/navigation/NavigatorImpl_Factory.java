package io.github.atwa.komposed.sample.core.navigation;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NavigatorImpl_Factory implements Factory<NavigatorImpl> {
  @Override
  public NavigatorImpl get() {
    return newInstance();
  }

  public static NavigatorImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NavigatorImpl newInstance() {
    return new NavigatorImpl();
  }

  private static final class InstanceHolder {
    private static final NavigatorImpl_Factory INSTANCE = new NavigatorImpl_Factory();
  }
}

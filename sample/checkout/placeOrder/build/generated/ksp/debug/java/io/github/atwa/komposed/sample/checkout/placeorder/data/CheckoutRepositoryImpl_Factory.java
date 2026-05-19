package io.github.atwa.komposed.sample.checkout.placeorder.data;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class CheckoutRepositoryImpl_Factory implements Factory<CheckoutRepositoryImpl> {
  @Override
  public CheckoutRepositoryImpl get() {
    return newInstance();
  }

  public static CheckoutRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CheckoutRepositoryImpl newInstance() {
    return new CheckoutRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final CheckoutRepositoryImpl_Factory INSTANCE = new CheckoutRepositoryImpl_Factory();
  }
}

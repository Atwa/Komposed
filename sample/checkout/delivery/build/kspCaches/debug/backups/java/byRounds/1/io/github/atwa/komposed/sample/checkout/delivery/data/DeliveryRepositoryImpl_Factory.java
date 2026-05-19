package io.github.atwa.komposed.sample.checkout.delivery.data;

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
public final class DeliveryRepositoryImpl_Factory implements Factory<DeliveryRepositoryImpl> {
  @Override
  public DeliveryRepositoryImpl get() {
    return newInstance();
  }

  public static DeliveryRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DeliveryRepositoryImpl newInstance() {
    return new DeliveryRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final DeliveryRepositoryImpl_Factory INSTANCE = new DeliveryRepositoryImpl_Factory();
  }
}

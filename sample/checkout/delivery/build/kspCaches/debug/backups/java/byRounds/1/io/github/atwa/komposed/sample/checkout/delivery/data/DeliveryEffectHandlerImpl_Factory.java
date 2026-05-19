package io.github.atwa.komposed.sample.checkout.delivery.data;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DeliveryEffectHandlerImpl_Factory implements Factory<DeliveryEffectHandlerImpl> {
  private final Provider<DeliveryRepository> repositoryProvider;

  public DeliveryEffectHandlerImpl_Factory(Provider<DeliveryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeliveryEffectHandlerImpl get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeliveryEffectHandlerImpl_Factory create(
      Provider<DeliveryRepository> repositoryProvider) {
    return new DeliveryEffectHandlerImpl_Factory(repositoryProvider);
  }

  public static DeliveryEffectHandlerImpl newInstance(DeliveryRepository repository) {
    return new DeliveryEffectHandlerImpl(repository);
  }
}

package io.github.atwa.komposed.sample.checkout.placeorder.data;

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
public final class PlaceOrderEffectHandlerImpl_Factory implements Factory<PlaceOrderEffectHandlerImpl> {
  private final Provider<CheckoutRepository> repositoryProvider;

  public PlaceOrderEffectHandlerImpl_Factory(Provider<CheckoutRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PlaceOrderEffectHandlerImpl get() {
    return newInstance(repositoryProvider.get());
  }

  public static PlaceOrderEffectHandlerImpl_Factory create(
      Provider<CheckoutRepository> repositoryProvider) {
    return new PlaceOrderEffectHandlerImpl_Factory(repositoryProvider);
  }

  public static PlaceOrderEffectHandlerImpl newInstance(CheckoutRepository repository) {
    return new PlaceOrderEffectHandlerImpl(repository);
  }
}

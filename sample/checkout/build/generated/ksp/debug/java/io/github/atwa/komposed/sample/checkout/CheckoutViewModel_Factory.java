package io.github.atwa.komposed.sample.checkout;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.atwa.komposed.Navigator;
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffectHandler;
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffectHandler;
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffectHandler;
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
public final class CheckoutViewModel_Factory implements Factory<CheckoutViewModel> {
  private final Provider<DeliveryEffectHandler> deliveryEffectHandlerProvider;

  private final Provider<BillEffectHandler> billEffectHandlerProvider;

  private final Provider<PlaceOrderEffectHandler> placeOrderEffectHandlerProvider;

  private final Provider<Navigator> navigatorProvider;

  public CheckoutViewModel_Factory(Provider<DeliveryEffectHandler> deliveryEffectHandlerProvider,
      Provider<BillEffectHandler> billEffectHandlerProvider,
      Provider<PlaceOrderEffectHandler> placeOrderEffectHandlerProvider,
      Provider<Navigator> navigatorProvider) {
    this.deliveryEffectHandlerProvider = deliveryEffectHandlerProvider;
    this.billEffectHandlerProvider = billEffectHandlerProvider;
    this.placeOrderEffectHandlerProvider = placeOrderEffectHandlerProvider;
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public CheckoutViewModel get() {
    return newInstance(deliveryEffectHandlerProvider.get(), billEffectHandlerProvider.get(), placeOrderEffectHandlerProvider.get(), navigatorProvider.get());
  }

  public static CheckoutViewModel_Factory create(
      Provider<DeliveryEffectHandler> deliveryEffectHandlerProvider,
      Provider<BillEffectHandler> billEffectHandlerProvider,
      Provider<PlaceOrderEffectHandler> placeOrderEffectHandlerProvider,
      Provider<Navigator> navigatorProvider) {
    return new CheckoutViewModel_Factory(deliveryEffectHandlerProvider, billEffectHandlerProvider, placeOrderEffectHandlerProvider, navigatorProvider);
  }

  public static CheckoutViewModel newInstance(DeliveryEffectHandler deliveryEffectHandler,
      BillEffectHandler billEffectHandler, PlaceOrderEffectHandler placeOrderEffectHandler,
      Navigator navigator) {
    return new CheckoutViewModel(deliveryEffectHandler, billEffectHandler, placeOrderEffectHandler, navigator);
  }
}

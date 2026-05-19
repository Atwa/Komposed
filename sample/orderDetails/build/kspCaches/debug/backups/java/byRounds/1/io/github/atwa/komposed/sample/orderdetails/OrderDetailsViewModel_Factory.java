package io.github.atwa.komposed.sample.orderdetails;

import androidx.lifecycle.SavedStateHandle;
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
public final class OrderDetailsViewModel_Factory implements Factory<OrderDetailsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public OrderDetailsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public OrderDetailsViewModel get() {
    return newInstance(savedStateHandleProvider.get());
  }

  public static OrderDetailsViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new OrderDetailsViewModel_Factory(savedStateHandleProvider);
  }

  public static OrderDetailsViewModel newInstance(SavedStateHandle savedStateHandle) {
    return new OrderDetailsViewModel(savedStateHandle);
  }
}

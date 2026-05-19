package io.github.atwa.komposed.sample.checkout.bill.data;

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
public final class BillEffectHandlerImpl_Factory implements Factory<BillEffectHandlerImpl> {
  private final Provider<BillSummaryRepository> repositoryProvider;

  public BillEffectHandlerImpl_Factory(Provider<BillSummaryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public BillEffectHandlerImpl get() {
    return newInstance(repositoryProvider.get());
  }

  public static BillEffectHandlerImpl_Factory create(
      Provider<BillSummaryRepository> repositoryProvider) {
    return new BillEffectHandlerImpl_Factory(repositoryProvider);
  }

  public static BillEffectHandlerImpl newInstance(BillSummaryRepository repository) {
    return new BillEffectHandlerImpl(repository);
  }
}

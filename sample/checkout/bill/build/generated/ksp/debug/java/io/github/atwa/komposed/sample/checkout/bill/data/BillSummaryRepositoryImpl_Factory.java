package io.github.atwa.komposed.sample.checkout.bill.data;

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
public final class BillSummaryRepositoryImpl_Factory implements Factory<BillSummaryRepositoryImpl> {
  @Override
  public BillSummaryRepositoryImpl get() {
    return newInstance();
  }

  public static BillSummaryRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BillSummaryRepositoryImpl newInstance() {
    return new BillSummaryRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final BillSummaryRepositoryImpl_Factory INSTANCE = new BillSummaryRepositoryImpl_Factory();
  }
}

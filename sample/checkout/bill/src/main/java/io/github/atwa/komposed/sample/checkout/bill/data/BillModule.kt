package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffectHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BillModule {
    @Binds
    abstract fun bindBillEffectHandler(impl: BillEffectHandlerImpl): BillEffectHandler

    @Binds
    abstract fun bindBillSummaryRepository(impl: BillSummaryRepositoryImpl): BillSummaryRepository
}

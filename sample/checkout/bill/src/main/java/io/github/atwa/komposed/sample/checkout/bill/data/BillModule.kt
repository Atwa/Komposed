package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffect
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BillModule {
    @Binds
    abstract fun bindBillEffectHandler(impl: BillEffectHandlerImpl): EffectHandler<BillEffect, BillAction>

    @Binds
    abstract fun bindBillSummaryRepository(impl: BillSummaryRepositoryImpl): BillSummaryRepository
}

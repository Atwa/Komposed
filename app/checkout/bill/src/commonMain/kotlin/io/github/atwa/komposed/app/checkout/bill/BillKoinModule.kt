package io.github.atwa.komposed.app.checkout.bill

import io.github.atwa.komposed.effect.EffectHandler
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val billKoinModule = module {
    single<BillSummaryRepository> { BillSummaryRepositoryImpl() }
    single<EffectHandler<BillEffect, BillAction>>(qualifier<BillEffectHandlerImpl>()) { BillEffectHandlerImpl(get()) }
}

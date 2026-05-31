package io.github.atwa.komposed.app.checkout.bill.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.app.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.app.checkout.bill.presentation.BillEffect
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val billKoinModule = module {
    single<BillSummaryRepository> { BillSummaryRepositoryImpl() }
    single<EffectHandler<BillEffect, BillAction>>(qualifier<BillEffectHandlerImpl>()) { BillEffectHandlerImpl(get()) }
}

package com.duczxje.appdependencies

import com.duczxje.domain.repository.SignalingRepository
import com.duczxje.domain.repository.StreamingRepository
import com.duczxje.appcore.repository.SignalingControlRepository
import com.duczxje.appcore.repository.StreamingControlRepository
import com.duczxje.appcore.service.SignalingControlService
import com.duczxje.appcore.service.StreamingControlService
import com.duczxje.data.service.SignalingService
import com.duczxje.data.service.StreamingService
import org.koin.dsl.module

fun createDomainModule() = module {
    single<SignalingService> {
        SignalingControlService( get(), get() )
    }

    single<StreamingService> {
        StreamingControlService( get(), get() )
    }

    factory<SignalingRepository> {
        SignalingControlRepository( get() )
    }

    factory<StreamingRepository> {
        StreamingControlRepository( get() )
    }
}

package com.duczxje.appdependencies

import com.duczxje.appcore.capture.CaptureManagerV1
import com.duczxje.appcore.socket.UdpSocketManagerV1
import com.duczxje.appcore.capture.CaptureManager
import com.duczxje.appcore.socket.TcpSocketManager
import com.duczxje.appcore.socket.TcpSocketManagerV1
import com.duczxje.appcore.socket.UdpSocketManager
import org.koin.dsl.module

fun createCoreModule() = module {
    single<CaptureManager> {
        CaptureManagerV1()
    }

    single<TcpSocketManager> {
        TcpSocketManagerV1()
    }

    single<UdpSocketManager> {
        UdpSocketManagerV1()
    }
}

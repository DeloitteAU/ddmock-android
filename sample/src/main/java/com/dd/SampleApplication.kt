package com.dd

import android.app.Application

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DDMock.install(this)
    }
}

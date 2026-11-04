package com.puciec.ghiblikmp

import android.app.Application

class GhibliApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}

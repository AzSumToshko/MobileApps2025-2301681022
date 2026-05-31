package com.example.automarket

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AutoMarketApplication : Application()
// Runs before everything and does all of the injections in the di folder

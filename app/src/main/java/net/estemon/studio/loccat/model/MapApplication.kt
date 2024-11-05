package net.estemon.studio.loccat.model

import android.app.Application
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)
    }
}
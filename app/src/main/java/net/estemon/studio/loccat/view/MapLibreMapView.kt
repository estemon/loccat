package net.estemon.studio.loccat.view

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@Composable
fun MapLibreMapView(
    modifier: Modifier = Modifier,
    markerPosition: LatLng?,
    onMapClick: (LatLng) -> Unit,
    onMapReady: (MapLibreMap) -> Unit = {}
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx)
            mapView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mapView.getMapAsync { mapLibreMap ->
                mapLibreMap.setStyle(Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")) {
                    onMapReady(mapLibreMap)
                }
                mapLibreMap.addOnMapClickListener { latLng ->
                    onMapClick(LatLng(latLng.longitude, latLng.latitude))
                    true
                }
            }
            mapView
        },
        modifier = modifier,
        update = { mapView ->
            // TODO update MapView
        }
    )
}
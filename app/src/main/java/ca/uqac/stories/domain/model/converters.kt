package ca.uqac.stories.data.source

import androidx.room.TypeConverter
import org.osmdroid.util.GeoPoint

class Converters {
    @TypeConverter
    fun fromGeoPoint(geoPoint: GeoPoint?): String? {
        return geoPoint?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toGeoPoint(value: String?): GeoPoint? {
        return value?.split(",")?.let {
            GeoPoint(it[0].toDouble(), it[1].toDouble())
        }
    }
}
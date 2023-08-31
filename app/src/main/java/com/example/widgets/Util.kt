package com.example.widgets

import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import com.example.widgets.model.Providers
import com.example.widgets.model.Widget
import com.google.gson.Gson
import java.io.ByteArrayOutputStream

fun getAppIcon(context: Context, packageName: String) = context.packageManager.getApplicationIcon(packageName)

fun getWidgetPreviewImage(context: Context, widgetProviderInfo: AppWidgetProviderInfo): Drawable {
    val resources: Resources = context.packageManager.getResourcesForApplication(widgetProviderInfo.provider.packageName)
    return widgetProviderInfo.loadPreviewImage(context, resources.displayMetrics.densityDpi)
}

fun getAppNameFromPackageName(context: Context, packageName: String): String {
    val pm: PackageManager = context.packageManager
    val ai: ApplicationInfo? = try {
        pm.getApplicationInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
    return (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String
}

fun toJsonString(src: Any): String? = try {
    Gson().toJson(src)
} catch (e: Exception) {
    null
}

fun encodeToBase64(image: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
}

fun Providers.toWidget(context: Context): Widget {
    val widgetProvider = providerInfo
    return Widget(
        provider = widgetProvider.provider.className,
        ratio = widgetProvider.resizeMode.toString(),
        preview = encodeToBase64(getWidgetPreviewImage(context, widgetProvider).toBitmap())
    )
}
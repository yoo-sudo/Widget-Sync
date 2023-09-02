package com.example.widgets

import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.example.widgets.model.AppDetails
import com.example.widgets.model.Providers
import com.example.widgets.model.Widget
import java.io.ByteArrayOutputStream

fun getWidgetPreviewImage(context: Context, widgetProviderInfo: AppWidgetProviderInfo): Drawable {
    val resources: Resources = context.packageManager.getResourcesForApplication(widgetProviderInfo.provider.packageName)
    Log.d("XOXOXO", resources.displayMetrics.densityDpi.toString())
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

fun encodeToBase64(image: Bitmap?): String? {
    val baos = ByteArrayOutputStream()
    image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
}

fun Providers.toWidget(context: Context): Widget {
    val widgetProvider = providerInfo
    return Widget(
        name = widgetProvider.loadLabel(context.packageManager),
        provider = widgetProvider.provider.className,
        ratio = widgetProvider.resizeMode.toString(),
        preview = encodeToBase64(getWidgetPreviewImage(context, widgetProvider).toBitmap())
    )
}

fun AppWidgetProviderInfo.toAppDetails(context: Context): AppDetails {
    val packageName = provider.packageName
    return AppDetails(
        appName = getAppNameFromPackageName(context, packageName),
        packageName = packageName,
        appIcon = loadIcon(context, context.resources.displayMetrics.densityDpi)
    )
}
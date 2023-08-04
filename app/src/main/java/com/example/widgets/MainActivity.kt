package com.example.widgets

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.widgets.model.Providers
import com.example.widgets.ui.theme.WidgetsTheme
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.processProviders(this)

        setContent {
            WidgetsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        val onItemClick = { index: Int ->
                            mainViewModel.setSelectionStatus(index, mainViewModel.installedProviders[index].isChecked.not())
                        }
                        LazyColumnWithSelection(onClick = onItemClick, mainViewModel.installedProviders)
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Button(onClick = {
                                val selectedProviders = mainViewModel.installedProviders.filter { it.isChecked }
                                selectedProviders.forEach {
                                    Log.i("provider", toJsonString(it.providerInfo.provider)!!)
                                    Log.i("packageName", toJsonString(getAppNameFromPackageName(this@MainActivity, it.providerInfo.provider.packageName))!!)
                                    Log.i("minHeight", toJsonString(getAppNameFromPackageName(this@MainActivity, it.providerInfo.minHeight.toString()))!!)
                                    Log.i("minWidth", toJsonString(getAppNameFromPackageName(this@MainActivity, it.providerInfo.minWidth.toString()))!!)
                                }
                            })
                            {
                                Text(text = "Send Providers", Modifier.wrapContentSize(), color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LazyColumnWithSelection(onClick: (Int) -> Unit,installedProviders: SnapshotStateList<Providers>) {
    LazyColumn(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxWidth(),
    ){
        itemsIndexed(items = installedProviders) { index, appWidgetProviderInfo ->
            ItemView(
                index = index,
                onClick = onClick,
                appWidgetProviderInfo,
            )
        }
    }
}

@Composable
fun ItemView(index: Int, onClick: (Int) -> Unit, appWidgetProviderInfo: Providers){
    val configuration = LocalConfiguration.current
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = appWidgetProviderInfo.providerInfo.provider.className,
            modifier = Modifier
                .wrapContentHeight()
                .width(if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 700.dp else 450.dp)
        )
        Checkbox(checked = appWidgetProviderInfo.isChecked,
            onCheckedChange = {
                onClick.invoke(index)
            })
    }
}

fun toJsonString(src: Any): String? = try {
    Gson().toJson(src)
} catch (e: Exception) {
    null
}

fun getAppNameFromPackageName(context: Context, packageName: String): String {
    val pm: PackageManager = context.packageManager
    val ai: ApplicationInfo? = try {
        pm.getApplicationInfo(packageName, 0)
    } catch (e: NameNotFoundException) {
        null
    }
    return (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String
}
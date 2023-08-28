package com.example.widgets.ui.theme.composable

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.widgets.viewmodel.MainViewModel
import com.example.widgets.R
import com.example.widgets.getAppIcon
import com.example.widgets.getAppNameFromPackageName
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppList(mainViewModel: MainViewModel, onClick: (Int) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column {
            TopAppBar(title = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.displaySmall
                    )
                }
            })
            LazyColumnWithSelection(
                modifier = Modifier
                .height(700.dp), mainViewModel.installedPackages, onClick = onClick)
//            Box(modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .wrapContentSize()) {
//                Button(onClick = {
////                    val selectedProviders = mainViewModel.installedProviders.filter { it.isChecked }
////                    selectedProviders.forEach {
////                        Log.i("provider", toJsonString(it.providerInfo.provider)!!)
////                        Log.i("packageName", it.providerInfo.provider.packageName)
////                        Log.i("minHeight", it.providerInfo.minHeight.toString())
////                        Log.i("minWidth", it.providerInfo.minWidth.toString())
////                        Log.i("appName", getAppNameFromPackageName(this@MainActivity, it.providerInfo.provider.packageName))
////                        Log.i("appIcon", encodeToBase64(getAppIcon(this@MainActivity, it.providerInfo.provider.packageName)).toString())
////                    }
//                }) {
//                    Text(text = "Send Providers", Modifier.wrapContentSize(), color = Color.White)
//                }
//            }
        }
    }
}

@Composable
fun LazyColumnWithSelection(modifier: Modifier, installedPackages: SnapshotStateList<ApplicationInfo>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(items = installedPackages) { index, packageInfo ->
            ItemView(
                index = index,
                onClick = onClick,
                packageInfo,
            )
        }
    }
}

@Composable
fun ItemView(index: Int, onClick: (Int) -> Unit, packageInfo: ApplicationInfo) {
    val configuration = LocalConfiguration.current
    val activity = (LocalContext.current as Activity)
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = getAppIcon(activity, packageInfo.packageName)),
            contentDescription = "content description",
        )
        Text(
            text = getAppNameFromPackageName(activity, packageInfo.packageName),
            modifier = Modifier.padding(12.dp)
                .clickable {
                    onClick.invoke(index)
                }
                .wrapContentHeight()
                .width(if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 700.dp else 350.dp)
        )
    }
}
package com.example.widgets.composable

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.widgets.viewmodel.MainViewModel
import com.example.widgets.getWidgetPreviewImage
import com.example.widgets.model.Providers
import com.example.widgets.ui.theme.EerieBlack
import com.example.widgets.ui.theme.darkerGray
import com.example.widgets.ui.theme.white
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppWidgetDetails(mainViewModel: MainViewModel, onClick: () -> Unit) {
    val onCheck = { index: Int ->
        mainViewModel.setSelectionStatus(index, mainViewModel.providerInfo[index].isChecked.not())
    }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize(), color = darkerGray) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(EerieBlack),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = mainViewModel.selectedAppDetail?.appName.toString(),
                    color = white,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(modifier = Modifier.padding(24.dp), onClick = onClick) {
                    Text(text = "Send Providers", Modifier.wrapContentSize(), color = Color.White)
                }
            }
            WidgetDetails(context = context, onCheck = onCheck, installedProviders = mainViewModel.providerInfo)
        }
    }
}

@Composable
fun WidgetDetails(context: Context, onCheck: (Int) -> Unit, installedProviders: SnapshotStateList<Providers>) {
    if (installedProviders.isEmpty()) {
        Text(
            text = "No Widgets to show!", modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), fontSize = 16.sp
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 24.dp),
        ) {
            itemsIndexed(items = installedProviders) { index, appWidgetProviderInfo ->
                WidgetItem(
                    context = context,
                    index = index,
                    onCheck = onCheck,
                    appWidgetProvider = appWidgetProviderInfo,
                )
            }
        }
    }
}

@Composable
fun WidgetItem(context: Context, index: Int, onCheck: (Int) -> Unit, appWidgetProvider: Providers) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = "${index + 1}. ${appWidgetProvider.providerInfo.loadLabel(context.packageManager)}",
            color = white,
            fontSize = 18.sp,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 24.dp, bottom = 18.dp)
        )
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = appWidgetProvider.isChecked, colors = myCheckBoxColors(), onCheckedChange = {
                onCheck.invoke(index)
            })
            Log.d("XOXOXO", getWidgetPreviewImage(context, appWidgetProvider.providerInfo).toBitmap().toString())
            Image(
                painter = rememberDrawablePainter(drawable = getWidgetPreviewImage(context, appWidgetProvider.providerInfo)),
                contentDescription = "content description",
            )
        }
    }
}

@Composable
fun myCheckBoxColors(): CheckboxColors {
    return CheckboxDefaults.colors(
        uncheckedColor = white
    )
}
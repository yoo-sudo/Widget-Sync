package com.example.widgets.composable

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.widgets.viewmodel.MainViewModel
import com.example.widgets.getWidgetPreviewImage
import com.example.widgets.model.Providers
import com.example.widgets.ui.theme.EerieBlack
import com.example.widgets.ui.theme.darkerGray
import com.example.widgets.ui.theme.white
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWidgetDetails(mainViewModel: MainViewModel, onClick: () -> Unit) {
    val onCheck = { index: Int ->
        mainViewModel.setSelectionStatus(index, mainViewModel.providerInfo[index].isChecked.not())
    }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize(), color = darkerGray) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(title = {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = mainViewModel.selectedAppDetail?.appName.toString(), style = MaterialTheme.typography.titleLarge, color = white
                    )
                }
            }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = EerieBlack))
            WidgetDetails(context = context, onCheck = onCheck, installedProviders = mainViewModel.providerInfo)

        }
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
            Button(modifier = Modifier.padding(12.dp), onClick = onClick) {
                Text(text = "Send Providers", Modifier.wrapContentSize(), color = Color.White)
            }
        }
    }
}

@Composable
fun WidgetDetails(context: Context, onCheck: (Int) -> Unit, installedProviders: SnapshotStateList<Providers>) {
    if (installedProviders.isEmpty()) {
        Text(
            text = "No Widgets to show!", fontSize = 16.sp
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 24.dp, bottom = 60.dp),
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
    val widthCell = appWidgetProvider.providerInfo.minWidth
    val heightCell = appWidgetProvider.providerInfo.minHeight
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = "${index + 1}. ${appWidgetProvider.providerInfo.loadLabel(context.packageManager)}.  $widthCell * $heightCell",
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
            val preview = getWidgetPreviewImage(context, appWidgetProvider.providerInfo)
            if (preview != null) {
                Image(
                    painter = rememberDrawablePainter(drawable = preview),
                    contentDescription = "Widget preview",
                )
            } else {
                Text(
                    text = "**Preview not Available**", modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), fontSize = 18.sp, color = white
                )
            }
        }
    }
}

@Composable
fun myCheckBoxColors(): CheckboxColors {
    return CheckboxDefaults.colors(
        uncheckedColor = white
    )
}
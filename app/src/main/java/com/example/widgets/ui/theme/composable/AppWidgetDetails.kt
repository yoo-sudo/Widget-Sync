package com.example.widgets.ui.theme.composable

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.widgets.viewmodel.MainViewModel
import com.example.widgets.getAppNameFromPackageName
import com.example.widgets.getWidgetPreviewImage
import com.example.widgets.model.Providers
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppWidgetDetails(mainViewModel: MainViewModel, context: Context, onClick: () -> Unit) {
    val onCheck = { index: Int ->
        mainViewModel.setSelectionStatus(index, mainViewModel.providerInfo[index].isChecked.not())
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(24.dp)
                ) {
                    Text(
                        text = getAppNameFromPackageName(context, mainViewModel.selectPackageName), modifier = Modifier
                            .wrapContentSize()
                            .padding(12.dp), color = Color.Black
                    )
                }
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
                .height(350.dp),
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
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = appWidgetProvider.isChecked, onCheckedChange = {
            onCheck.invoke(index)
        })
        Image(
            painter = rememberDrawablePainter(drawable = getWidgetPreviewImage(context, appWidgetProvider.providerInfo)),
            contentDescription = "content description",
        )
    }
}
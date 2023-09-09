package com.example.widgets.composable

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.widgets.viewmodel.MainViewModel
import com.example.widgets.R
import com.example.widgets.model.AppDetails
import com.example.widgets.ui.theme.EerieBlack
import com.example.widgets.ui.theme.darkerGray
import com.example.widgets.ui.theme.white
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppList(mainViewModel: MainViewModel, onClick: (Int) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = darkerGray) {
        Column {
            TopAppBar(title = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleLarge, color = white
                    )
                }
            },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = EerieBlack ))
            LazyColumnWithSelection(
                modifier = Modifier
                .height(700.dp), mainViewModel.installedPackages, onClick = onClick)
        }
    }
}

@Composable
fun LazyColumnWithSelection(modifier: Modifier, appDetails: List<AppDetails>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(items = appDetails) { index, appDetail ->
            ItemView(
                index = index,
                onClick = onClick,
                appDetails = appDetail,
            )
        }
    }
}

@Composable
fun ItemView(index: Int, onClick: (Int) -> Unit, appDetails: AppDetails) {
    val configuration = LocalConfiguration.current
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable {
                onClick.invoke(index)
            }
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier= Modifier.size(48.dp),
            painter = rememberDrawablePainter(drawable = appDetails.appIcon),
            contentDescription = "App icon",
        )
        Text(
            text = appDetails.appName,
            color= white,
            modifier = Modifier.padding(12.dp)
                .wrapContentHeight()
                .width(if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 700.dp else 350.dp)
        )
    }
}
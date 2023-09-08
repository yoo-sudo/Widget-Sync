package com.example.widgets.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PermissionScreen(
    showAlert: Boolean,
    onClick: () -> Unit,
    onRationaleReply: (Boolean) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Provide permission to access camera",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onClick) {
                Text(text = "Grant Permission")
            }
        }
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { onRationaleReply(false) },
                title = {
                    Text(text = "Access Camera")
                },
                text = {
                    Text(text = "In order to scan qrcode you need to grant access by accepting the next permission dialog.\n\nWould you like to continue?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onRationaleReply(true)
                        }
                    ) {
                        Text("Continue")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onRationaleReply(false)
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}
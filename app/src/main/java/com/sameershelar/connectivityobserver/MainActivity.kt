package com.sameershelar.connectivityobserver

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sameershelar.connectivityobserver.ui.theme.ConnectivityObserverTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConnectivityObserverTheme {
                HomeScreen(
                    connectivityObserver = ConnectivityObserver(applicationContext)
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    connectivityObserver: ConnectivityObserver
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LaunchedEffect(null) {
                connectivityObserver.getConnectivityStatus().collectLatest { connection ->
                    snackbarHostState.showSnackbar(
                        message = connection.status,
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {
    ConnectivityObserverTheme {
        HomeScreen(
            connectivityObserver = ConnectivityObserver(object {} as Application)
        )
    }
}
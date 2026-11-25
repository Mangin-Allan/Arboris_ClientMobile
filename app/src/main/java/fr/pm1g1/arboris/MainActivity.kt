package fr.pm1g1.arboris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.pm1g1.arboris.Navigation.ArborisNavHost
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.ui.theme.ArborisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.initialize(applicationContext)

        setContent {
            ArborisTheme {
                ArborisNavHost()
            }
        }
    }
}

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray85
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.robotTheme


private val LightColorPalette = lightColors(primary = primaryBlue)
@Composable
fun FsTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(appGray85)
    systemUiController.statusBarDarkContentEnabled = true
    MaterialTheme(
        colors = LightColorPalette,
        typography = robotTheme,
        content = content
    )
}
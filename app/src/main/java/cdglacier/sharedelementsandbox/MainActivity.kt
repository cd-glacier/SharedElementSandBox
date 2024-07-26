package cdglacier.sharedelementsandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cdglacier.sharedelementsandbox.ui.compositionlocal.AnimatedVisibilityScopeProvider
import cdglacier.sharedelementsandbox.ui.compositionlocal.SharedTransitionScopeProvider
import cdglacier.sharedelementsandbox.ui.screen.detailview.DetailView
import cdglacier.sharedelementsandbox.ui.screen.detailview.DetailViewScreen
import cdglacier.sharedelementsandbox.ui.screen.gridview.GridView
import cdglacier.sharedelementsandbox.ui.screen.gridview.GridViewScreen
import cdglacier.sharedelementsandbox.ui.screen.transparentbackground.TransparentBackgroundView
import cdglacier.sharedelementsandbox.ui.screen.transparentbackground.TransparentBackgroundViewScreen
import cdglacier.sharedelementsandbox.ui.theme.SharedElementSandboxTheme
import cdglacier.sharedelementsandbox.unsplash.Photo
import cdglacier.sharedelementsandbox.unsplash.PhotoNavType
import cdglacier.sharedelementsandbox.unsplash.photos
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object Home

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedElementSandboxTheme {
                SharedTransitionScopeProvider {
                    val navController = rememberNavController()
                    val lazyGridState = rememberLazyGridState()

                    NavHost(
                        navController = navController,
                        startDestination = Home,
                    ) {
                        composable<Home> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Button(onClick = {
                                        navController.navigate(TransparentBackgroundView)
                                    }) {
                                        Text("Transparent Background")
                                    }

                                    Button(onClick = {
                                        navController.navigate(GridView)
                                    }) {
                                        Text("NOT Transparent Background")
                                    }
                                }
                            }
                        }
                        composable<TransparentBackgroundView> {
                            TransparentBackgroundViewScreen(photos = photos)
                        }
                        composable<GridView> {
                            AnimatedVisibilityScopeProvider(
                                animatedVisibilityScope = this,
                            ) {
                                GridViewScreen(
                                    lazyGridState = lazyGridState,
                                    photos = photos,
                                    onClickPhoto = { photo ->
                                        navController.navigate(
                                            DetailView(
                                                photo
                                            )
                                        )
                                    }
                                )
                            }
                        }
                        composable<DetailView>(
                            typeMap = mapOf(
                                typeOf<Photo>() to PhotoNavType
                            ),
                        ) { backStackEntry ->
                            AnimatedVisibilityScopeProvider(
                                animatedVisibilityScope = this
                            ) {
                                val detailView = backStackEntry.toRoute<DetailView>()
                                DetailViewScreen(
                                    lazyGridState = lazyGridState,
                                    initialPage = detailView.page,
                                    photos = photos,
                                    popBackStack = navController::popBackStack,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

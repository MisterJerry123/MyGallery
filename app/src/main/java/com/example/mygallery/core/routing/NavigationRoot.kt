package com.example.mygallery.core.routing

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.mygallery.presentation.screen.home.HomeRoot
import com.example.mygallery.presentation.screen.imageDetail.ImageDetailRoot

@Composable
fun NavigationRoot() {

    val backStack = rememberNavBackStack(Route.Home)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Home> {
                HomeRoot(
                    onImageClick = { image ->
                        backStack.add(
                            Route.ImageDetail(
                                id = image.id,
                                uri = image.contentUri.toString(),
                                name = image.name,
                                dateTaken = image.dateTaken
                            )
                        )
                    }
                )
            }
            entry<Route.ImageDetail> { route ->
                ImageDetailRoot(
                    route = route,
                    onBackClick = {
                        backStack.removeLast()
                    }
                )
            }
        }
    )

}
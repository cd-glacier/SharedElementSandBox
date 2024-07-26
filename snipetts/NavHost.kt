val navController = rememberNavController()

NavHost(
    navController = navController,
    startDestination = GridView,
) {
    composable<GridView> {
        GridViewScreen(
            photos = photos,
            onClickPhoto = { photo -> navController.navigate(DetailView(photo)) }
        )
    }
    composable<DetailView>(
        typeMap = mapOf(
            typeOf<Photo>() to PhotoNavType
        ),
    ) { backStackEntry ->
        val detailView = backStackEntry.toRoute<DetailView>()
        DetailViewScreen(detailView.photo)
    }
}

# weatherapp

## App Architecture
App follows MVVM architecture with Multi module structure

    App: Main entry point of the application. It has MainActivity and App Navgraph
        Core:
            data: exposing repository classes to UI layer
            network: Network client for API services
        feature:
            search: to handle search screen
            weatherinfo: to handle weather home screen

### Android Components
    Used below components to built the application
        Compose: For UI screens
        Compose Navigation: To navigate between the composable screens
        Coroutines/Flows: For Asynchronous programming
        Storage: Used Data store for preferences
        Hilt: Used hilt for dependency injection
        Retrofit: Used retrofit for Network API calls
        Location: Used fusedlocationprovider to get location information of the user
        mockk: for View model test cases


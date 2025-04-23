@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEventScreen(
    viewModel: AdminEventViewModel = viewModel(),
    onNavigateToWorkouts: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToMembers: () -> Unit = {}
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    val events by viewModel.events.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Event Screen") },
                actions = {
                    // Add any necessary actions here
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Add any necessary content here

                EventForm(
                    onEventCreated = { event ->
                        viewModel.addEvent(event)
                    }
                )

                // Add any necessary content here
            }
        }
    )
}

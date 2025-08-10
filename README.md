# Timeline App

## Time Spent
Approximately 3 hours were spent on this assignment, distributed as follows:
- 15 minutes: Initial project analysis and architecture planning
- 45 minutes: Project restructuring, applying Clean Architecture, and implementing dependency injection
- 1 hour: Implementation of core timeline functionality and swimlane algorithm
- 1 hour: UI refinement, testing, and documentation

## Implementation Details

### What I Like About My Implementation

1. **Clean Architecture**: The project follows Clean Architecture principles with clear separation of concerns:
   - Domain layer with models, repositories interfaces, and use cases
   - Data layer with repository implementations and data sources
   - Presentation layer with ViewModels and UI components

2. **Efficient Swimlane Algorithm**: The timeline uses a greedy algorithm with a min-heap to efficiently distribute events into lanes, ensuring optimal space usage.

3. **Responsive UI**: The timeline adapts to different screen sizes and orientations, with a horizontal scrolling timeline that shows events proportionally to their duration.

4. **Interactive Elements**: Events can be tapped to view detailed information in a bottom sheet, enhancing user experience without cluttering the main view.

5. **Dependency Injection**: Koin is used for dependency injection, making the code more testable and maintainable.

6. **Zoom Functionality**: Implemented pinch-to-zoom capability for the timeline, allowing users to zoom in for detailed views or zoom out for a broader perspective.

### What I Would Change

If I had more time, I’d rethink the UI to deliver a more polished experience with better accessibility. 
Timelines tend to work best in horizontal layouts, but that pattern doesn’t translate well to phones. 
I spent a good amount of time exploring vertical approaches, but the usability wasn’t great.

## Design Decisions

The timeline design was inspired by several popular project management tools like Gantt charts and calendar applications. Key design decisions include:

1. **Horizontal Timeline**: A horizontal layout was chosen as it's the most intuitive way to represent time progression from left to right.

2. **Lane Numbers**: Each lane is clearly numbered for easy reference.

3. **Proportional Event Sizing**: Events are sized proportionally to their duration, providing visual cues about event length.

4. **Minimal Visual Clutter**: The UI focuses on essential information, with additional details available on demand.

## Testing Strategy

With more time, I would implement the following testing approach:

1. **Unit Tests**:
   - Test the swimlane algorithm with various event configurations
   - Test date calculations and utilities
   - Test repository and use case implementations

2. **Integration Tests**:
   - Test the interaction between repositories, data sources, and use cases
   - Test ViewModel state management

3. **UI Tests**:
   - Test timeline rendering with different datasets
   - Test user interactions like scrolling and tapping events
   - Test responsive layout on different screen sizes

4. **Performance Tests**:
   - Test with large datasets to ensure smooth scrolling
   - Test memory usage during extended use

## Build and Run Instructions

1. **Requirements**:
   - Android Studio Arctic Fox (2021.3.1) or newer
   - JDK 11 or newer
   - Android SDK 31 or newer

2. **Steps to Run**:
   - Clone the repository
   - Open the project in Android Studio
   - Sync Gradle files
   - Run the app on an emulator or physical device

3. **Minimum Android Version**: API 24 (Android 7.0 Nougat)

## Libraries Used

- **Jetpack Compose**: For modern, declarative UI
- **Koin**: For dependency injection
- **Kotlin Coroutines**: For asynchronous programming
- **Material 3**: For UI components and theming

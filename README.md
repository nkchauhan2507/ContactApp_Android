# Contact App

[![Language: Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)

This Contact App enables users to manage their contacts stored in Firebase DataStore Database. It supports full CRUD operations, allowing users to add, read, update, and delete contacts seamlessly.

## Components Used

- **RecyclerView**: For displaying the list of contacts.
- **CardView**: To structure each contact item visually.
- **Navigation**: To handle user navigation within the app.
- **Firebase DataStore**: For storing and managing contact data.
- **Fragment**: For modularizing the UI.
- **XML**: For designing the UI layouts.
- **Material Design**: For ensuring a consistent and modern look and feel.
- **FAB (Floating Action Button)**: For adding new contacts.

## Google Services Configuration

To properly configure your project, please ensure you add your own `google-services.json` file. This file is essential for integrating Firebase services into your application.

### Steps to Add `google-services.json`

1. **Download the File**: Obtain your `google-services.json` from the Firebase Console after setting up your project.

2. **Location**: Place the `google-services.json` file in the following directory:

3. **File Name**: Ensure that the file is named exactly `google-services.json` without any additional characters.

4. **Version Control**: It is recommended to add `google-services.json` to your `.gitignore` file to prevent it from being committed to your repository, as it may contain sensitive information.

5. **Build the Project**: After adding the file, you can build your project. If you encounter issues related to the file being missing, double-check the placement and naming of the file.


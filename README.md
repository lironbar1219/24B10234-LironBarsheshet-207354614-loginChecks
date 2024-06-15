# Android Login Checks Application

This Android application demonstrates a login mechanism based on battery percentage, device orientation, and time constraints.

The user must enter a password that matches the current battery percentage, the device must be pointing north, and the current time must be within a specified range.

## Installation

To clone and run this application, you'll need Git installed on your computer. From your command line:

```bash
# Clone this repository
git clone https://github.com/lironbar1219/24B10234-LironBarsheshet-207354614-loginChecks.git

# Go into the repository
cd 24B10234-LironBarsheshet-207354614-loginChecks
```
## Agenda
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the project on an emulator or physical device.
5. Enter the password matching the current battery percentage.
6. Ensure the device is pointing north.
7. Check the time is within the specified range.

## Login Checks
The application performs the following checks to grant access:

1. Battery Percentage: The password entered must match the current battery percentage of the device.
2. Device Orientation: The device must be pointing north (azimuth angle between 345° and 15°).
3. Time Constraint: The current time must be between 12 PM and 11 PM.

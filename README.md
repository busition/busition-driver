# Busition Driver

Busition Driver is the Android application used by drivers in the Busition shuttle platform. It lets a driver sign in, load assigned schedules, open a route on Google Maps, start a driving session, track the bus in real time, and manage passenger boarding data through Bluetooth Low Energy (BLE) scanning and backend APIs.

The project is written in Kotlin and built with Gradle. It integrates Google Maps, Firebase Cloud Messaging, Socket.IO, Retrofit, and Room.

## Overview

This app is built around a simple driver workflow:

1. Sign in with a driver account or a company account.
2. Load the list of available schedules from the Busition backend.
3. Open a route and review stops, estimated time, and passenger counts.
4. Start the operation to enable GPS tracking and socket communication.
5. Use BLE scanning to detect passenger devices during boarding and drop-off.

## Main Features

- Driver login and company login flows
- Driver registration flow
- Schedule list fetched from the Busition API
- Route and stop detail view with Google Maps
- Foreground GPS service for live vehicle location updates
- Socket.IO connection for real-time route updates and passenger events
- BLE scanning for passenger UUID detection
- Local Room database storage for passenger data in company mode
- Floating overlay view for quick station list access during operation

## Tech Stack

- Kotlin
- Android SDK 33
- Gradle / Android Gradle Plugin 7.4.2
- AndroidX Navigation
- View Binding and Data Binding
- Retrofit with Gson and Scalars converters
- Firebase Messaging and Analytics
- Google Maps SDK
- Google Play Services Location
- Socket.IO client
- Room

## Requirements

- Android Studio with Android SDK 33 installed
- JDK 11 configured through `JAVA_HOME`
- Android device or emulator running Android 8.0 (API 26) or later
- Access to the Busition backend services
- Google Play Services support on the test device

## Backend and Service Dependencies

The current source code is wired to Busition development services by default.

- REST API base URL: `https://api.dev2.busition.com/latest/`
- Socket server: `https://socket.dev2.busition.com/user`

The app also depends on:

- Firebase configuration through `app/google-services.json`
- A Google Maps API key declared in `AndroidManifest.xml`

If you plan to use this project outside the original Busition environment, update those values before building or distributing the app.

## Permissions Used

The app requests or declares the following Android permissions:

- Fine and coarse location
- Background location
- Internet
- Overlay window permission
- Foreground service
- Bluetooth and BLE scan/connect permissions
- Notifications

Some permissions are effectively mandatory for the current app flow. In particular, overlay permission is requested at launch, and the app closes if it is not granted.

## Getting Started

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd busition_driver
```

### 2. Open the project

Open the repository in Android Studio and allow Gradle sync to complete.

### 3. Check local environment

Make sure the following are available:

- `JAVA_HOME` points to a JDK 11 installation
- Android SDK 33 is installed
- A connected Android device or emulator is available

### 4. Verify service configuration

Before running the app, confirm that these integrations are valid for your environment:

- `app/google-services.json`
- Google Maps API key in `app/src/main/AndroidManifest.xml`
- API host values in `app/src/main/java/io/busition/driver/tools.kt`

## Build

Build a debug APK with:

```bash
./gradlew assembleDebug
```

Install it on a connected device with:

```bash
./gradlew installDebug
```

Create a release bundle with:

```bash
./gradlew bundleRelease
```

## Project Structure

```text
.
├── app/
│   ├── src/main/java/io/busition/driver/
│   │   ├── Service/              # GPS, floating overlay, socket, Firebase services
│   │   ├── data/                 # Retrofit interfaces and API models
│   │   ├── main_screen/          # Main driver flow, schedule, route, passenger UI
│   │   ├── roomDB/               # Local database layer
│   │   ├── ui/login/             # Login and registration flow
│   │   └── tools.kt              # Shared helpers and API base URL constants
│   └── src/main/res/             # Layouts, drawables, navigation graphs, resources
├── gradle/                       # Gradle wrapper
└── README.md
```

## Runtime Flow

### Login

- `LoginActivity` hosts the authentication flow.
- The app supports email/password login and a separate company login path.
- Firebase Messaging is used to fetch a device token during sign-in.

### Schedule and Route

- After login, `MainScreen` opens the schedule list.
- Selecting a schedule opens the route operation screen.
- Route geometry and station data are fetched from the backend.

### Operation Mode

- Starting a run launches `GpsForegroundService`.
- The app sends location updates through Socket.IO.
- BLE scanning is used to identify nearby passenger devices.
- Passenger information can be displayed per station.

## Known Limitations

- The app is currently pointed to Busition development servers, not a configurable environment file.
- Sensitive service configuration is stored directly in the repository or source files instead of being fully externalized.
- Several UI strings are still in Korean.
- The Google sign-in button exists in the layout but is hidden and not part of the current login flow.
- T map navigation and the floating overlay flow appear to be partially integrated and may require device-specific testing.

## Troubleshooting

### `JAVA_HOME is not set`

Set your JDK 11 path before running Gradle:

```bash
export JAVA_HOME=/path/to/jdk-11
export PATH="$JAVA_HOME/bin:$PATH"
```

### Build succeeds but maps or notifications do not work

Check the following first:

- `app/google-services.json` matches your Firebase project
- The Google Maps API key is enabled for Android Maps SDK
- The test device has Google Play Services available

### Login or schedule loading fails

The app depends on the Busition backend and socket server. If those services are unavailable or credentials are invalid, login, schedule loading, route loading, and passenger synchronization will fail.

## License

This project is licensed under `GPL-3.0-only`. See [LICENSE](./LICENSE) for the full license text.

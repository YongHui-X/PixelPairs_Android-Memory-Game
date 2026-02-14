 # AndroidCA -- Memory Card Matching Game

  A collaborative Android application where users fetch images from the web and play a memory card matching game. Built
  with Kotlin (Android) and ASP.NET Core (.NET backend).

  ## Features

  - **User Authentication** -- Login system backed by a MySQL database via .NET REST API.
  - **Image Fetching** -- Enter a URL (e.g. StockSnap.io) to scrape and download up to 20 images, displayed in a grid
  with a progress bar.
  - **Image Selection** -- Select exactly 6 images from the fetched results to use as card faces.
  - **Memory Card Game** -- A 3x4 grid card-matching game with flip animations, timer, and pause/resume functionality.
  - **Leaderboard** -- Completion times are submitted as scores; top 5 players are displayed.
  - **Ad Support** -- Free-tier users see ads during gameplay; paid users do not.
  - **Background Music** -- Music service plays during login and gameplay.

  ## Tech Stack

  | Layer | Technology |
  |-------|-----------|
  | Android | Kotlin, Coroutines, View Binding, Glide, OkHttp, Jsoup |
  | Backend | ASP.NET Core (.NET 10), MySQL, MySqlConnector |
  | Architecture | Activities + Fragments, REST API, MVC |

  ## Screenshots
  In order: 
  1. Login
  2. Image fetch screen
  3. Mid fetch
  4. Fetch completed, select 6 images to play
  5. First pair that matches
  6. All pairs matched, game completed.
  7. Leaderboard with play button that goes back to fetch

  <img width="320" height="800" alt="Android_Register" src="https://github.com/user-attachments/assets/b73406dc-303b-48aa-a151-12c55ff7e77f" />

  <img width="320" height="800" alt="Pixel Pair_202602147" src="https://github.com/user-attachments/assets/0c18f929-6adf-40f7-b887-c20ed4f3e748" />
 
  <img width="320" height="800" alt="Pixel Pair_202602148" src="https://github.com/user-attachments/assets/6d323667-bec0-4623-85e1-c778afb6f20d" />
  
  <img width="320" height="800" alt="Pixel Pair_202602149" src="https://github.com/user-attachments/assets/e2f8f740-c372-42fb-b573-b587184185e8" />
  
  <img width="320" height="800" alt="Pixel Pair_2026021410" src="https://github.com/user-attachments/assets/308b9696-a77b-4099-8d72-4626778419ca" />
  
  <img width="320" height="800" alt="Pixel Pair_2026021411" src="https://github.com/user-attachments/assets/a689c466-abbd-457b-b07e-4aad34dced70" />
  
  <img width="320" height="800" alt="Pixel Pair_2026021412" src="https://github.com/user-attachments/assets/9437b741-f521-4c8d-b052-b458e263e8e0" />
  
  <img width="320" height="800" alt="Pixel Pair_2026021413" src="https://github.com/user-attachments/assets/546dcce3-9bc7-44d8-a0fd-2eb6b72c423d" />
  

  ## Prerequisites

  - **Android Studio** (Arctic Fox or later)
  - **.NET 10 SDK**
  - **MySQL Server** (e.g. MySQL 8.x)
  - **Android Emulator** or physical device (min SDK 29 / Android 10)

  ## How to Run

  ### 1. Database Setup

  1. Install and start MySQL Server.
  2. Create the database and required tables:

  ```sql
  CREATE DATABASE AndroidCA;
  USE AndroidCA;

  CREATE TABLE Users (
      Username VARCHAR(255) PRIMARY KEY,
      Password VARCHAR(255) NOT NULL
  );

  CREATE TABLE UserDetails (
      Id BIGINT AUTO_INCREMENT PRIMARY KEY,
      Username VARCHAR(255) NOT NULL,
      UserType VARCHAR(50) NOT NULL
  );

  CREATE TABLE Scores (
      Id BIGINT AUTO_INCREMENT PRIMARY KEY,
      UserDetailId BIGINT NOT NULL,
      Score BIGINT NOT NULL,
      FOREIGN KEY (UserDetailId) REFERENCES UserDetails(Id)
  );

  3. Insert sample users:

  INSERT INTO Users (Username, Password) VALUES ('player1', 'password123');
  INSERT INTO UserDetails (Username, UserType) VALUES ('player1', 'Free');

  INSERT INTO Users (Username, Password) VALUES ('player2', 'password123');
  INSERT INTO UserDetails (Username, UserType) VALUES ('player2', 'Paid');

  2. Backend (.NET API)

  1. Open a terminal in the DotNetBackend/DotNetBackend/ directory.
  2. Update the connection string in Data/Constants.cs with your MySQL credentials:
  public static string CONNECTION_STRING = @"server=localhost;uid=root;pwd=YOUR_PASSWORD;database=AndroidCA";
  3. Run the backend:
  dotnet run
  4. The API will start on http://localhost:5119. Verify by visiting http://localhost:5119/api/scores/leaderboard.
  5. Start my SQL DB as necessary.

  3. Android App

  1. Open the android/ folder in Android Studio.
  2. Wait for Gradle sync to complete.
  3. If using the Android Emulator, the backend URL http://10.0.2.2:5119 (already configured) maps to your host
  machine's localhost:5119.
    - If using a physical device, update the base URL in LoginActivity.kt to your machine's local IP (e.g.
  http://192.168.x.x:5119).
  4. Run the app on the emulator or device (min API 29).

  4. Using the App

  1. Log in with a valid username and password. For testing purposes: (johndoe, 12345)
  2. Enter a StockSnap.io URL (e.g. https://stocksnap.io/search/nature) in the fetch screen.
  3. Wait for up to 20 images to load.
  4. Tap to select exactly 6 images, then press Start Playing.
  5. Match all 6 pairs as fast as possible -- your time is your score!

  Project Structure

  android/                # Android application (Kotlin)
    adapters/             # ImageAdapter, CardAdapter, LeaderboardAdapter
    fragments/            # LeaderboardFragment, LoadingFragment
    models/               # ImageItem, LeaderboardEntry, Users
    services/             # MusicService
    FetchActivity.kt      # Fetch images from URL
    PlayActivity.kt       # Memory card matching game
    LoginActivity.kt      # User authentication
    LeaderboardActivity.kt

  DotNetBackend/          # .NET backend API
    Controllers/          # AuthController, ScoresController
    Models/               # Score, Leaderboard DTOs
    Data/                 # ScoresDAO, Constants (DB connection)

  API Endpoints

  ┌────────┬─────────────────────────────┬─────────────────────────┐
  │ Method │          Endpoint           │       Description       │
  ├────────┼─────────────────────────────┼─────────────────────────┤
  │ POST   │ /api/auth/login             │ Authenticate user       │
  ├────────┼─────────────────────────────┼─────────────────────────┤
  │ POST   │ /api/scores/submit          │ Submit a game score     │
  ├────────┼─────────────────────────────┼─────────────────────────┤
  │ GET    │ /api/scores/leaderboard     │ Get top 5 scores        │
  ├────────┼─────────────────────────────┼─────────────────────────┤
  │ GET    │ /api/scores/user/{username} │ Get a user's best score │
  └────────┴─────────────────────────────┴─────────────────────────┘

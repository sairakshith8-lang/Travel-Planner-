# ✈️ TravelMate - Full-Stack Travel Planner Web Application

**TravelMate** is a modern, responsive, and beginner-friendly full-stack travel planning web application built using **HTML5, CSS3, and JavaScript** on the frontend and **Java (Spring Boot 3), Spring Data JPA, and MySQL** on the backend.

Designed with a sleek **blue, white, and teal** tourism aesthetic, TravelMate enables travelers to explore destinations across India, generate intelligent day-by-day itineraries, track budgets across categories, and manage private trips.

---

## 🌟 Key Features

1. **Tourism-Inspired Landing Page (`index.html`)**:
   - Modern hero section with travel imagery and quick destination search.
   - Highlights of popular Indian destinations (Goa, Manali, Jaipur, Kerala, Hyderabad, Ooty, etc.).
   - Key stats banner, feature breakdown, and quick action buttons.

2. **Secure User Authentication (`login.html`, `register.html`)**:
   - User registration and login with input validation.
   - **BCrypt** password hashing for secure credential storage.
   - Session-based authentication with user-scoped data isolation (users can never access or modify other users' trips).
   - Pre-seeded demo account (`demo@travelmate.com` / `password123`) with an "Auto-Fill Demo" button for effortless presentations.

3. **Explore Destinations (`destinations.html`)**:
   - Catalog of Indian destinations with photos, best seasons, and daily cost estimates.
   - Search bar by name or state.
   - Filters by travel type (Beach, Hill Station, Heritage, Adventure, Nature, Spiritual) and budget limit.
   - Sorting by cost (low-to-high, high-to-low) and name.
   - "Add to Trip" CTA button prefilling the trip creation form.

4. **Trip Creation & Smart Itinerary Generator (`create-trip.html`)**:
   - Captures trip name, source, destination, dates, travelers, budget in INR (₹), and travel preference.
   - Form validation: return date cannot precede departure date, travelers $\ge 1$, budget $\ge 0$.
   - **Rule-based Java service** automatically creates a balanced day-by-day itinerary (morning, afternoon, evening activities with estimated costs).
   - Automatically initializes a proportional budget breakdown.

5. **Trip Details & Budget Calculator (`trip-details.html`)**:
   - **Day-by-Day Timeline**: View, add, edit, and delete activities for each day.
   - **Budget Calculator**: Tracks spending across Transportation, Accommodation, Food, Sightseeing, and Shopping.
   - Real-time progress bar and variance indicators (alerts if actual spending exceeds total budget).
   - Edit trip modal to update dates, budget, or travelers dynamically.

6. **User Dashboard (`my-trips.html`)**:
   - Displays all user journeys with filter tabs for **All Trips**, **Upcoming**, and **Completed**.
   - Friendly empty state and confirmation modal for deleting trips.

7. **About & Contact (`about.html`)**:
   - Project overview, architecture summary, and a contact form stored in MySQL (`contact_messages`).

---

## 🏗️ Technology Stack

- **Frontend**:
  - Semantic HTML5
  - Vanilla CSS3 (Custom design system with flexbox, CSS grid, glassmorphism, animations)
  - Plain JavaScript (ES6+ Fetch API, DOM manipulation)
  - *No frontend frameworks (No React, Angular, or Vue) as per requirements.*

- **Backend**:
  - Java 17+
  - Spring Boot 3.2.5
  - Spring Data JPA (Hibernate ORM)
  - Spring Validation (`jakarta.validation`)
  - Spring Security Crypto (BCrypt password encoder)
  - Maven (Dependency management and build)

- **Database**:
  - MySQL 8.x (Primary production database)
  - H2 In-Memory Database (Built-in demo profile for zero-config fallback)

---

## 📁 Project Folder Structure

```
Travel Planner/
├── pom.xml                                  # Maven project configuration
├── README.md                                # Project documentation & setup guide
└── src/
    ├── main/
    │   ├── java/com/travelmate/
    │   │   ├── TravelPlannerApplication.java # Spring Boot entry point
    │   │   ├── config/
    │   │   │   ├── AppConfig.java           # BCrypt password encoder bean
    │   │   │   └── DataInitializer.java     # Seeds demo user & sample trip
    │   │   ├── controller/
    │   │   │   ├── AuthController.java      # Register, login, logout, me
    │   │   │   ├── TripController.java      # Trip CRUD with user ownership
    │   │   │   ├── ItineraryController.java # Itinerary activity management
    │   │   │   ├── ExpenseController.java   # Budget and expense tracking
    │   │   │   ├── DestinationController.java# Destination catalog & search
    │   │   │   └── ContactController.java   # Contact message submission
    │   │   ├── dto/                         # Data Transfer Objects & validation
    │   │   ├── entity/                      # JPA Entities (User, Trip, etc.)
    │   │   ├── exception/                   # Global exception handling & custom errors
    │   │   ├── repository/                  # Spring Data JPA repositories
    │   │   └── service/                     # Business logic services
    │   └── resources/
    │       ├── application.properties       # Default configuration (MySQL)
    │       ├── application-h2.properties    # Zero-setup demo profile (H2)
    │       ├── data.sql                     # SQL schema & setup documentation
    │       └── static/                      # Frontend HTML, CSS & JavaScript
    │           ├── index.html
    │           ├── login.html
    │           ├── register.html
    │           ├── destinations.html
    │           ├── create-trip.html
    │           ├── my-trips.html
    │           ├── trip-details.html
    │           ├── about.html
    │           ├── css/style.css
    │           └── js/
    │               ├── api.js
    │               ├── navbar.js
    │               ├── home.js
    │               ├── login.js
    │               ├── register.js
    │               ├── destinations.js
    │               ├── create-trip.js
    │               ├── my-trips.js
    │               ├── trip-details.js
    │               └── about.js
    └── test/                                # JUnit & Spring Boot test suite
```

---

## ⚙️ Prerequisites

1. **Java Development Kit (JDK)**: Version 17 or higher (Java 17, 21, or 26).
   ```bash
   java -version
   ```
2. **Apache Maven**: Version 3.8 or higher.
   ```bash
   mvn -version
   ```
3. **MySQL Server**: Version 8.0 or higher (or use the built-in H2 profile if MySQL is not yet installed).

---

## 🗄️ MySQL Database Setup

### Step 1: Start MySQL Service
- **macOS (Homebrew)**: `brew services start mysql`
- **Linux (Ubuntu/Debian)**: `sudo systemctl start mysql`
- **Windows**: Start the MySQL service from the Services panel or XAMPP.

### Step 2: Create the Database
Log into your MySQL terminal:
```bash
mysql -u root -p
```
Run the following SQL commands:
```sql
CREATE DATABASE IF NOT EXISTS travelmate_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Step 3: Configure Database Credentials
You can either set environment variables or edit `src/main/resources/application.properties`:

**Option A (Environment Variables - Recommended):**
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=travelmate_db
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

**Option B (Directly in `application.properties`):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/travelmate_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

Hibernate automatically generates and manages the following tables on startup:
1. `users`
2. `trips`
3. `itinerary_items`
4. `expenses`
5. `destinations`
6. `contact_messages`

---

## 🚀 How to Run the Application

### Method 1: Run with MySQL (Default)
In your terminal, navigate to the project root and run:
```bash
mvn spring-boot:run
```

### Method 2: Run with Zero-Config In-Memory Database (H2 Demo Mode)
If you do not have MySQL installed on your computer or want an instant, zero-setup college presentation:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
*or using the compiled JAR:*
```bash
java -jar target/travel-planner-1.0.0.jar --spring.profiles.active=h2
```

### Method 3: Open and Run in VS Code
1. Open VS Code: `File -> Open Folder... -> Select "Travel Planner"`.
2. Ensure you have the **Extension Pack for Java** and **Spring Boot Extension Pack** installed.
3. Open `TravelPlannerApplication.java` (`src/main/java/com/travelmate/TravelPlannerApplication.java`).
4. Click **Run** or press `F5`.

---

## 🌐 Opening the Application

Once the server has started, open your web browser and navigate to:
```
http://localhost:8080/
```

### 👤 Pre-Seeded Demo Account
For instant demonstration:
- **Email**: `demo@travelmate.com`
- **Password**: `password123`
*(Or click the **Auto-Fill Demo** button on the Login page)*

---

## 🧪 Running Backend Tests

Run the automated test suite with Maven:
```bash
mvn test
```
All tests use an isolated in-memory test database, validating user registration, password verification, date validation, and private trip isolation.

---

## 📡 REST API Endpoints Overview

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a new user | No |
| `POST` | `/api/auth/login` | Authenticate and create session | No |
| `POST` | `/api/auth/logout` | Terminate session | Yes |
| `GET` | `/api/auth/me` | Fetch authenticated user profile | Yes |
| `GET` | `/api/destinations` | List/filter destinations (`query`, `travelType`, `maxCost`) | No |
| `GET` | `/api/destinations/{id}` | Get destination details | No |
| `GET` | `/api/trips` | Get current user's trips (`?filter=all/upcoming/completed`) | Yes |
| `POST` | `/api/trips` | Create trip + auto-generate itinerary & expenses | Yes |
| `GET` | `/api/trips/{id}` | Get full trip details with itinerary & budget | Yes |
| `PUT` | `/api/trips/{id}` | Update trip information | Yes |
| `DELETE` | `/api/trips/{id}` | Delete trip and its cascade data | Yes |
| `POST` | `/api/trips/{tripId}/itinerary` | Add new activity to itinerary | Yes |
| `PUT` | `/api/trips/{tripId}/itinerary/{id}` | Edit an existing activity | Yes |
| `DELETE`| `/api/trips/{tripId}/itinerary/{id}` | Remove an activity | Yes |
| `POST` | `/api/trips/{tripId}/expenses` | Add a new expense item | Yes |
| `PUT` | `/api/trips/{tripId}/expenses/{id}` | Update an expense (estimate/actual) | Yes |
| `DELETE`| `/api/trips/{tripId}/expenses/{id}` | Remove an expense item | Yes |
| `POST` | `/api/contact` | Submit contact inquiry | No |

---

## 🛠️ Common Errors & Solutions

1. **Error: `Communications link failure` / `Connection refused: connect`**
   - **Cause**: MySQL server is not running or the port/password is incorrect.
   - **Solution**: Check that MySQL is running (`mysql -u root -p`). Alternatively, run in H2 demo mode:
     ```bash
     mvn spring-boot:run -Dspring-boot.run.profiles=h2
     ```

2. **Error: `Access denied for user 'root'@'localhost'`**
   - **Cause**: Incorrect MySQL password in `application.properties`.
   - **Solution**: Set the environment variable `DB_PASSWORD=your_actual_password` or edit `application.properties`.

3. **Port 8080 already in use**
   - **Cause**: Another service or instance of Tomcat is running on port 8080.
   - **Solution**: Start the app on a different port:
     ```bash
     mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8085"
     ```
     Then open `http://localhost:8085/`.

4. **Changes to CSS/JS not reflecting in browser**
   - **Cause**: Browser cache.
   - **Solution**: Perform a hard refresh using `Ctrl + F5` (Windows/Linux) or `Cmd + Shift + R` (macOS).

---

## 🎓 Academic Demonstration Highlights

1. **Separation of Concerns**: Strict boundary between HTML/CSS/JS presentation and Spring Boot REST API services.
2. **Security Best Practice**: Zero plain-text passwords; BCrypt salted hashing; HTTP session cookie management; user trip ownership authorization.
3. **Responsive UI**: Custom CSS design system adhering to modern tourism aesthetics without third-party frontend dependencies.
4. **Data Integrity**: JPA foreign-key relations with cascade deletion ensuring database consistency.

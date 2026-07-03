# 🎬 CinePlex Pro – Movie Ticket Booking System

A full-featured Java Swing desktop application for booking movie tickets with a modern dark-themed UI.

---

## 🚀 Quick Start

### Prerequisites
- Java JDK 11 or higher  
  Download from: https://adoptium.net/

### Windows
```
Double-click build_and_run.bat
```

### Linux / macOS
```bash
chmod +x build_and_run.sh
./build_and_run.sh
```

### Manual Compile & Run
```bash
# Compile
javac -d out/classes src/moviebooking/*.java

# Create JAR
jar --create --file CineplexPro.jar --main-class moviebooking.Main -C out/classes .

# Run
java -jar CineplexPro.jar
```

---

## 🔑 Login Credentials

| Role  | Username | Password | Access                      |
|-------|----------|----------|-----------------------------|
| User  | `admin`  | `1234`   | Full movie booking flow     |
| Admin | `admin`  | `admin`  | Admin Dashboard (stats + records) |
| Guest | —        | —        | Click "Continue as Guest"   |

---

## 🎭 Application Flow (Updated)

```
Splash Screen
    └─► Login Screen
            ├─► [admin/admin] Admin Dashboard (stats, bookings table, clear)
            └─► City Selection
                    └─► Movie Browser
                            ├─► ▶ Trailer  (opens YouTube in browser)
                            └─► Book Now
                                    └─► Language Popup
                                            └─► Seat Count Popup
                                                    └─► Show Timings Screen  ← NEW
                                                            └─► Seat Selection Grid
                                                                    └─► Payment (UPI)
                                                                            └─► Confirmation + Ticket
```

---

## 📱 Screens & Features

### 1. 🎞️ Splash Screen
- Animated progress bar with gradient fill
- Glowing logo animation
- Film strip decorations
- Status messages during load

### 2. 🔐 Login Screen
- Split-layout: cinematic left panel + form right panel
- Custom styled text fields with rounded corners
- Animated sign-in button
- Guest login option
- Enter key support on password field

### 3. 🏙️ City Selection
- 8 major Indian cities with emoji icons
- Hover effects with accent glow
- City descriptions

### 4. 🎬 Movie Browser
- 6 latest Bollywood movies with:
  - Genre, rating (⭐), duration, certification
  - Color-coded poster art per movie
  - **▶ Trailer** button → opens YouTube in browser
  - **Book Now** button
- Language selector popup (Hindi, English, Tamil, Telugu, Kannada)
- Seat count popup (1–10 seats)

### 5. 💺 Seat Selection
- 10 rows × 12 columns grid (120 seats total)
- 3 price categories:
  | Category  | Rows  | Price  |
  |-----------|-------|--------|
  | 🩶 Silver | G–J   | ₹200   |
  | 🥇 Gold   | C–F   | ₹300   |
  | 💎 Recliner | A–B | ₹500  |
- Pre-occupied seats (random, shown greyed)
- Real-time price calculator
- Color-coded legend
- Seat limit enforcement with alert

### 6. 💳 Payment Screen
- UPI payment simulation
- 3 payment methods:
  - 📱 PhonePe
  - 💳 Google Pay
  - 👨‍👩‍👧 FamPay
- UPI ID validation
- Processing animation
- Order summary panel

### 7. 🎟️ Confirmation Screen
- Stylized ticket card with:
  - Perforation line effect
  - Barcode art
  - Booking ID
  - All booking details
- Booking saved to `bookings.txt`
- Options: Book Again | View File | Exit

---

## 🗂️ Project Structure

```
MovieTicketBooking/
├── src/moviebooking/
│   ├── Main.java                 # Entry point + global config
│   ├── UITheme.java              # Centralised styles, colours, factories ← NEW
│   ├── SplashScreen.java         # Animated loading screen
│   ├── LoginScreen.java          # Split-layout login (user + admin)
│   ├── CitySelectionScreen.java  # City picker with cards
│   ├── MovieSelectionScreen.java # Movie browser + popups
│   ├── ShowTimingsScreen.java    # Date & time slot picker ← NEW
│   ├── SeatSelectionScreen.java  # Seat grid + live pricing
│   ├── PaymentScreen.java        # UPI payment flow
│   ├── ConfirmationScreen.java   # Ticket display + file save
│   └── AdminDashboard.java       # Admin stats + bookings table ← NEW
├── out/classes/                  # Compiled bytecode (generated)
├── build_and_run.bat             # Windows build script
├── build_and_run.sh              # Linux/Mac build script
├── CineplexPro.jar               # Executable JAR (generated)
├── bookings.txt                  # Saved bookings (generated)
└── README.md
```

---

## 🎨 UI Design Highlights

- **Dark theme** throughout with deep navy/purple backgrounds
- **Gradient accents** — red/orange for headers, gold for titles
- **Custom-painted components** — all buttons, cards, fields hand-drawn via Graphics2D
- **Hover states** on every interactive element
- **Smooth layout** with absolute positioning for pixel-perfect control
- **No external libraries** — pure Java SE (Swing + AWT)

---

## ⚙️ Technical Details

| Feature | Implementation |
|---------|----------------|
| GUI Framework | Java Swing |
| Custom Painting | `paintComponent()` override with Graphics2D |
| Animations | `javax.swing.Timer` |
| File I/O | `FileWriter` + `BufferedWriter` (append mode) |
| Trailer Links | `Desktop.getDesktop().browse(URI)` |
| Sound Feedback | `Toolkit.getDefaultToolkit().beep()` |
| Seat State | 2D boolean arrays |
| Random Seeds | Fixed seed for reproducible occupied seats |

---

## 📄 Bookings File Format

Every booking is appended to `bookings.txt`:
```
==========================================
BOOKING ID   : CPX847291
DATE & TIME  : 12-Apr-2024 14:32
CUSTOMER     : admin
MOVIE        : Pushpa 2
CITY         : Pune
LANGUAGE     : Hindi
SEATS        : C3, C4, D5
AMOUNT PAID  : ₹930
PAYMENT VIA  : PhonePe (admin@upi)
==========================================
```

---

*Built with ❤️ using pure Java Swing — no external dependencies required.*

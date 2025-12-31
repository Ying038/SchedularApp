# 📅 Calendar Application - FOP Group 5 Assignment

A comprehensive JavaFX-based calendar application with event management, recurring events, reminders, statistics, and backup/restore functionality.

## 🎯 Features

### Core Features
- ✅ **Event Management** - Create, update, and delete events
- ✅ **Recurring Events** - Daily, weekly, and monthly repetitions
- ✅ **Calendar Views** - GUI grid view and CLI text format
- ✅ **Event Search** - Basic title search and advanced multi-filter search
- ✅ **Clash Detection** - Automatic conflict warnings
- ✅ **Reminder System** - Visual notification banners with color-coded urgency

### Additional Features
- ✅ **Event Statistics** - Analytics on busiest days, hours, distributions
- ✅ **Additional Fields** - Location, category, and priority for events
- ✅ **Backup & Restore** - Save and restore with append/overwrite options
- ✅ **CSV File Handling** - Separate files for events, recurring data, and additional fields

## 🚀 Quick Start

### Running the Application

**Windows PowerShell:**
```powershell
.\run-gui.ps1
```

**Or manually:**
```powershell
cd CalendarApp
java --module-path ../libs/javafx-sdk-17.0.2/lib --add-modules javafx.controls,javafx.fxml -cp "target/classes" com.mycompany.calendarapp.CalendarAppGUI
```

**Or use the batch file:**
```cmd
cd CalendarApp
run-gui.bat
```

## 📁 Project Structure

```
FOP-Grp5-Assignment/
├── CalendarApp/                    # Main application directory
│   ├── src/main/java/com/mycompany/calendarapp/
│   │   ├── CalendarAppGUI.java    # Main GUI application
│   │   ├── MainEvent.java         # Event class with additional fields
│   │   ├── RecurringEvent.java    # Recurring event implementation
│   │   ├── EventManager.java      # Event management logic
│   │   ├── EventStatistics.java   # Analytics engine
│   │   ├── NotificationManager.java # Visual reminder system
│   │   ├── CSVHandlerCompliant.java # CSV file operations
│   │   ├── AdditionalFieldsHandler.java # Extra fields CSV handler
│   │   └── ... (other classes)
│   ├── event.csv                  # Event data storage
│   ├── recurrent.csv              # Recurring event data
│   ├── additional.csv             # Additional fields data
│   ├── backups/                   # Backup files directory
│   └── run-gui.bat                # Windows batch launcher
├── libs/
│   └── javafx-sdk-17.0.2/         # JavaFX libraries
├── README.md                      # This file
└── run-gui.ps1                    # PowerShell launcher script
```

## 📊 CSV File Format

The application uses three CSV files:

### event.csv
```csv
eventId,title,description,startDateTime,endDateTime
1001,Team Meeting,Discuss Q1 goals,2024-01-15T09:00,2024-01-15T10:00
```

### recurrent.csv
```csv
eventId,recurrentInterval,recurrentTimes,recurrentEndDate
2001,1w,10,2024-03-15
```

### additional.csv
```csv
eventId,location,category,priority
1001,Boardroom A,Meeting,HIGH
```

## 🎮 Usage Guide

### Creating Events
1. Click "📝 Event Management"
2. Choose "Add Event" or "Add Recurring Event"
3. Fill in:
   - Title, description, date/time
   - Location (optional)
   - Category (General, Work, Personal, etc.)
   - Priority (HIGH, MEDIUM, LOW)

### Viewing Statistics
1. Click "📊 Event Statistics" from main menu
2. View comprehensive analytics including:
   - Busiest day of week
   - Busiest hour of day
   - Event distribution charts
   - Average metrics

### CLI Format Views
1. Go to "📆 Calendar View"
2. Click "View CLI Format"
3. Select Weekly or Monthly view
4. Choose date and view formatted text output

### Backup & Restore
1. Click "💾 Backup & Restore"
2. Create backup or restore from file
3. Choose **Append** (merge) or **Overwrite** (replace)

## 📝 Assignment Compliance

This project fulfills all requirements :

- ✅ Basic Requirements - Event management, calendar views, search, recurring events
- ✅ OOP Implementation - Proper class design, inheritance, encapsulation
- ✅ File Handling - 3 CSV files with proper format
- ✅ GUI - Professional JavaFX interface
- ✅ Additional Features - Reminders, clash detection, statistics, extra fields, backup


## 🛠️ Requirements

- Java 11 or higher
- JavaFX 17.0.2 (included in `libs/` folder)
- Windows OS (for batch/PowerShell scripts)

## 👥 Group 5

**Course:** Fundamentals of Programming  
**Year:** 2025/2026 

## 📄 License

Educational project for FOP assignment.

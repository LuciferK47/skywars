# Sky Wars: Grid-Based Simulation

A turn-based tactical simulation developed in Java using the Swing GUI framework. The application features a 4x4 grid environment where a Master Ship must survive against randomly spawning and roaming enemy raiders.

## Technical Specifications

* **Language:** Java 8+
* **GUI Framework:** Java Swing
* **Data Interchange:** JSON (via Google Gson)
* **Design Patterns:** Observer (UI Updates), Abstract Factory/Inheritance (Ship Types)

## Core Mechanics

### Movement and Grid
* **Dimensions:** 4x4 Grid.
* **Navigation:** Ships move to a random neighbor square each turn.
* **Adjacency:** 8-way movement (Horizontal, Vertical, and Diagonal).

### Conflict Resolution
Conflict is resolved only on the square currently occupied by the Master Ship:
* **Defensive Mode:** Master Ship is destroyed if 2 or more enemies occupy its square.
* **Offensive Mode:** Master Ship is destroyed if 3 or more enemies occupy its square.
* **Combat:** If exactly 1 enemy shares a square with the Master Ship, the Master Ship destroys the enemy and remains in play.
* **Enemy Interaction:** Enemy ships do not engage in combat with each other and can coexist on the same square.

### Spawning System
* **Probability:** 33.3% chance per turn to spawn a new enemy.
* **Spawn Point:** All new enemies originate at coordinates (0,0).
* **Enemy Variants:** BattleCruiser, BattleShooter, and BattleStar.

## Project Structure

* `Launcher.java`: Application entry point.
* `Engine.java`: Manages game state, turn logic, and randomization.
* `EngineInterface.java`: Handles the Swing-based graphical user interface.
* `Grid.java` / `Square.java`: Represents the spatial environment and individual cell contents.
* `Ship.java`: Abstract base class for all entities.
* `EngineState.java`: Data Transfer Object (DTO) for JSON serialization.

## Installation and Execution

### Requirements
* Java Development Kit (JDK) 11 or higher.
* `gson.jar` (provided in the `lib/` directory).

### Compiling from Command Line
Navigate to the project root directory and run:

```bash
# Create output directory
mkdir out

# Compile the source
javac -cp "lib/gson.jar" -d out src/skywars/*.java

# Run the application
java -cp "out:lib/gson.jar" skywars.Launcher

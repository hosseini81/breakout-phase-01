# Breakout Game

A classic Breakout arcade game implemented in Java using Swing and the ACM Graphics Library.

This project was developed as part of Phase 1 of the Advanced Programming course at the University of Tehran.

## Overview

The objective is to destroy all 40 bricks using a bouncing ball while preventing the ball from falling below the paddle.

The game includes a complete gameplay loop, collision detection, scoring, lives management, and a state machine.

## Features

- Smooth gameplay targeting 60 updates per second
- Paddle movement with keyboard controls
- Ball movement and elastic wall reflections
- Angle-based ball reflection from the paddle
- 40 bricks arranged in an 8 × 5 grid
- Collision detection and brick destruction
- Score and lives tracking
- High-score storage in memory
- Game states: READY, RUNNING, PAUSED, GAME_OVER, VICTORY
- Pause, resume, and restart functionality
- Heads-Up Display (HUD)

## Controls

| Key | Action |
|---|---|
| ← / → | Move the paddle |
| Space | Launch the ball |
| P | Pause / Resume |
| R | Restart the game |

## Technology Stack

- Java 11
- Java Swing
- ACM Graphics Library
- Object-Oriented Programming
- Model-View-Controller (MVC) Architecture

## Project Architecture

The project follows the MVC architectural pattern.

### Model

Contains the core game entities and state:

- `Ball`
- `Paddle`
- `Brick`
- `BrickField`
- `GameState`

### View

`BreakoutCanvas` is responsible for rendering game objects, updating the HUD, and displaying game messages.

### Controller

`GameEngine` coordinates the game loop, keyboard input, collision handling, scoring, and state transitions.

### Storage

`HighScoreStorage` defines the score-storage interface, while `InMemoryScoreStorage` provides its in-memory implementation.

### Configuration

`GameConstants` centralizes game dimensions, movement parameters, colors, and other configuration values.

## Running the Project

1. Make sure Java 11 is installed.
2. Open the project in IntelliJ IDEA.
3. Configure the project SDK to Java 11.
4. Add the ACM library (`acm.jar`) to the project classpath.
5. Run the `main` method in `breakout.AcmTest`.

## High Score

The current implementation stores the highest score in memory. The record is preserved during the current application session and resets when the application closes.

## Project Status

The core gameplay and MVC architecture for Phase 1 have been implemented.
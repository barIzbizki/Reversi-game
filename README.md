# Reversi Game Implementation

This project involves creating an expanded version of the strategic board game **Reversi**, implemented on an 8x8 board with special disc types. The goal is to apply Object-Oriented Programming (OOP) principles while creating a functional and interactive game that follows the rules specified.

## Project Structure

The provided files include:

- `GUI_for_chess_like_games` - Graphical User Interface for the game.
- `PlayableLogic` - Interface defining the game's rules.
- `Player` - Base class for players.
- `AIPlayer` - Base class for AI-controlled players.
- `Disc` - Abstract class representing a disc on the board.
- `Main` - Main class to run the game.
- Sample game file - Demonstrates basic gameplay.

For additional details, refer to the [assignment PDF](מימוש%20משחק%20רברסי.pdf) included in this repository.

## Classes to Implement

### 1. GameLogic
- Implements the `PlayableLogic` interface.
- Manages game state, rules, board, and player turns.
- Handles placing and flipping opponent discs.

### 2. Discs
Implement `Disc` interface for different disc types:
- **SimpleDisc** - Regular disc that follows standard Reversi rules.
- **UnflippableDisc** - Special disc that cannot be flipped once placed.
- **BombDisc** - When flipped, it causes surrounding discs to flip, potentially triggering other bombs.

### 3. Helper Classes
- **Position** - Represents a position on the board.
- **Move** - Represents a game move and supports undo functionality.

### 4. AI Players
- **RandomAI** - Randomly selects a legal move.
- **GreedyAI** - Chooses the move that flips the maximum number of opponent discs.

## Game Rules

- The objective is to finish with the highest number of discs in your color.
- The game begins with four discs in the center.
- Three types of discs exist: Regular, Unflippable, and Bomb (special flipping rules apply).
- The game ends when no legal moves are available for the next player.



---


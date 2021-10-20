# Othello Game 
Implementation of the Othello game (aka Reversi) with various game features including reset, undo, skip turn and save/load. Read about the game here: https://en.wikipedia.org/wiki/Reversi.

## Game.java
Sets up the frame and widgets for the GUI. It handles the layout of the game
and creates buttons with action listeners. It is the class that is run
to run the actual game. 

## Othello.java
This class includes all the methods that support the functions necessary in the game, such as flipping the discs after a valid move is played,
determining whether a move is valid, undo-ing a move, saving and loading a game. 
  	
## Coordinate.java
This class creates an object of type Coordinate. It stores the row and column 
index that a disc is placed in. It is used to support the undo function of the game,
which relies on a LinkedList of type Coordinate. 
  	
## GameBoard.java
Instantiates a model of the game. Updates and repaints the game board as
a user interacts with it. It will also throw error messages to the user
if the user tries to play invalid moves or encounters errors while saving 
or loading the game. Instructions can also be accessed here. 

## Core Concepts

1. 2D Arrays  
  	I have used a 2D array to represent the board (8x8 grid). 0 represents a
  	blank square, 1 represents a black disc and 2 represents a white disc. 
  	At the start of the game, all entries in the array are 0, except for the 
  	center 2x2 square that has “1 2 2 1” representing the standard start game condition. 
  	This is an appropriate use of 2D arrays because it helps keep track of the current
  	state of each square of the game board.
  	

  2. Collections  
  	I have used a LinkedList to keep track of a player's moves each turn.
  	Every time a player tries to add a disc to a new square, the LinkedList
  	will be updated with the most recent move at the end of the list. 
  	The position played is represented using a Coordinate object
  	that stores the row and col picked. When a player undoes a move, the 
  	last element of the list is removed (most recent). A LinkedList is appropriate
  	since I need to keep track of the order of elements in order to figure out
  	which element to remove, and it is also easy to access the last element.

  3. File I/O  
  	I have used File I/O to store the current game state such that players 
  	can save and load the game. The game state will be loaded into a "game.txt"
  	file. The current state of the 2D array will be written into a .txt file 
  	(combination of 0s, 1s and 2s) when the user decides to save the game.
  	Next, the player's moves will also be stored with each index separated by "/". 
  	This is so when the game is loaded players can still undo their moves. 
  	Lastly, a boolean value is stored to keep track of whose turn it is when 
  	the game is loaded.  
    The location to save and load the game is by default. The players cannot
  	pick an arbitrary .txt file to load into the game. Hence a game can only be loaded
  	if a game was previously saved (so a valid "game.txt" file exists). Only 1 game
  	state can be saved at a time. If a game is saved twice, the older version will be
  	rewritten with the newer version. 

  4. Testable Component  
  	I have tested whether the game state (2D array) is updated correctly after 
  	a player makes a valid move. A valid move should “flip” the discs in the 
  	row/column/diagonal which corresponds to changing the entries in the 2D array. 
  	I also tested edge cases such as when a player tries to place a disc in a 
  	spot that would not result in a valid move, or when they try to place a disc 
  	in a spot that already has a disc. I also tested that the game correctly 
  	ends when all grids are filled with discs or no more moves are possible
  	for either player, and that the winner is calculated correctly by counting 
  	the number of 1s and 2s in the array. There are some tests for the undo 
  	function of the game as well. 
    
 

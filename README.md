# ConnectFivehaaks

The Program takes input and prints the output through the console.

There are four packages. 

* Paths which contains the http Get and Post requests.
* Dao is the logic of the Connect 5 game, with any requests are called from the Game class.
* MainConnection contains the connection, which calls the REST API (Paths) to get the relevant data. 
  - Simplifications:- 
		  - Made in the Connection class, is that each player run this class, so 2 players runs the class twice.
		  - When the user remains idle for 25 seconds, the player is considered disconnected and the game finishes.
* Test contains the test cases.



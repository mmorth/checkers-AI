# checkers-AI
For our second project in COM S 472 (Principles of Artificial Intelligence), I created a Checkers AI program. The program allows the user to input moves to play against the Checkers AI. The Checkers AI then uses the Alpha-Beta pruning algorithm (with a depth of 5 moves into the future) with a domain-specific evaluation function to determine the best move.

The evaluation function takes into account the number of pieces the agent has compared to the human player, the number of agent pieces in safer positions (in a corner or on the sides), and the number of agent pieces that are in a position to jump a human piece. These features are then weighted accordingly to generate the evaluation function value for each agent move.

The results were impressive. I do not consider myself a good checkers player, but I could no longer win against the checkers AI I created.

# Running the Program:
To compile the my program, in the checkers-AI directory, type:

	javac *.java

To run my program, in the checkers-AI directory, type:

	java Checkers

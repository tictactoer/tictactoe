# tic-tac-toe

tic-tac-toe is a RESTful tic-tac-toe game server. The only supported game mode is a human (or API user) against the server that applies the strategy of random choice.

# Build
`mvn clean install dockerfile:build`

The result is a docker image tictactoer/tic-tac-toe:0.0.1

JDK 11, relatively recent Maven and Docker are required.

# Deployment
`docker run -d -p 8080:8080 --name tic-tac-toe tictactoer/tic-tac-toe:0.0.1`


# Use

The examples below assume that you started the container on 192.168.99.100.

### Create a game
```
curl -s -d '{"name":"Mati","character":"x"}' -H "Content-Type: application/json" 192.168.99.100:8080/game/
1
```

The returned "1" is the id of created game that is needed for URLs to identify the game. Each request like above creates a new game and return a new id. The examples below assume that game "1".


### Get the state of a game
```
curl -s 192.168.99.100:8080/game/1
   A B C
A | | | |
B |o| |o|
C |x|x|x|       Human won!
```

### Make a move
```
curl -s -d '{"row": "C", "column":"A"}' -H "Content-Type: application/json" 192.168.99.100:8080/game/1/move
```

# Specification Interpretations
Encoding of the responses is not really specified except for get game operation. As that operation is specified to be ASCII, that has been applied generally also for others.

The game creation example includes a name, presumably for the human player, but it is not used in any other examples despite natural opportunity in get game operation output. Thus the name given in create operation is completely ignored.

Player characters, row ids and column ids are case insensitive in requests to server but are capitalized according to examples in responses.

It is not specified which player should start or if this choice should be up to the user. In this implementation the user always starts. The server "computer" always makes its move immediately as part of processing of the user's move, so from an API point of view it is always the user's turn.

No moves are allowed after a winning condition or draw as been reached.

# Design

The code is heavily built on Spring Boot. It does not follow MVC pattern to the end but Controller also contains all the small pieces of View there is.

H2 database is used for persistence. However, as H2 is run internally in the container as an in-memory database, so it is not actually
persistent beyond the lifetime of the container.

Using a 1-dimensional character array as the board representation is a very low level way of doing this. Splitting it to more complex
structure did not seem to offer any real benefit for understandability or simplicity though and this keeps things very simple down to the single table DB.

# Testing

By no means complete.

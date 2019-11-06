package com.example.tictactoer.tictactoe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
 * Database wrapper
 */
@Repository
public interface GameRepo extends CrudRepository<Game, Long> {
	Game findById(long id);
}

package nl.tudelft.jpacman.game;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

import java.util.List;

public class GameTest {

	private Level level = mock(Level.class);

	public class GameChild extends Game {

		@Override
		public List<Player> getPlayers() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Level getLevel() {
			// TODO Auto-generated method stub
			return level;
		}

	}

	private GameChild newGame;

	/*
	 * Validates that when freeze is called, this games level is frozen.
	 */
	@Test
	public void testFreeze() {
		// init new GameChild class
		newGame = new GameChild();
		GameChild spyClass = spy(newGame);
		// mock functions to get game started
		when(level.isAnyPlayerAlive()).thenReturn(true);
		when(level.remainingPellets()).thenReturn(1);
		// start the game
		spyClass.start();

		spyClass.freeze();
		verify(level, times(1)).freeze();
	}

	/*
	 * Validates that when freeze is called, this games level is frozen.
	 */
	@Test
	public void testFreezeFalse() {
		// init new GameChild class
		newGame = new GameChild();
		GameChild spyClass = spy(newGame);
		// mock functions to get game started
		when(level.isAnyPlayerAlive()).thenReturn(true);
		when(level.remainingPellets()).thenReturn(1);
		spyClass.freeze();
		verify(level, times(0)).freeze();
	}

}

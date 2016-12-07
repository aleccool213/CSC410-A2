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
import org.junit.Before;


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
	
	private GameChild spyClass;
	
	/*
	 * Setup this test suite.
	 */
	@Before
	public void setUp() {
		// init new GameChild class
		newGame = new GameChild();
		spyClass = spy(newGame);
		// mock functions to get game started
		when(level.isAnyPlayerAlive()).thenReturn(true);
		when(level.remainingPellets()).thenReturn(1);
	}

	/*
	 * Validates that when start is called, level.start is called
	 */
	@Test
	public void testStart() {
		// make it for sure not frozen
		when(level.getFrozen()).thenReturn(false);
		// start the game the first time
		spyClass.start();

		verify(level, times(1)).start();
	}
	
	/*
	 * Validates that when start is called, level.start does not call start when frozen.
	 */
	@Test
	public void testStartFrozen() {
		// make it frozen
		when(level.getFrozen()).thenReturn(true);
		// start the game
		spyClass.start();

		verify(level, times(0)).start();
	}
	
	/*
	 * Validates that when start is called, level.start not get called when already in progress
	 */
	@Test
	public void testStartInProgress() {
		// make it for sure not frozen
		when(level.getFrozen()).thenReturn(false);
		// start the game the first time
		spyClass.start();
		
		// start the game the second time
		spyClass.start();

		verify(level, times(1)).start();
	}
	
	/*
	 * Validates that when stop is called, level.stop is not called if start was not called previously
	 */
	@Test
	public void testStop() {
		// make it frozen
		when(level.getFrozen()).thenReturn(false);
		
		// stop the game
		spyClass.stop();

		verify(level, times(0)).stop();
	}
	
	/*
	 * Validates that when stop is called, level.stop is not called
	 */
	@Test
	public void testStopFrozen() {
		// make it unfrozen
		when(level.getFrozen()).thenReturn(false);
		
		//start the game
		spyClass.start();
		
		// make it frozen
		when(level.getFrozen()).thenReturn(true);

		// stop the game
		spyClass.stop();

		verify(level, times(0)).stop();
	}
	
	/*
	 * Validates that when stop is called, level.stop is called
	 */
	@Test
	public void testStopInProgress() {
		// make it frozen
		when(level.getFrozen()).thenReturn(false);
		// start the game
		spyClass.start();
		
		// stop the game the first time
		spyClass.stop();
		
		// stop the game the second time
		spyClass.stop();
		verify(level, times(1)).stop();
	}

	/*
	 * Validates that when freeze is called, this games level is frozen.
	 */
	@Test
	public void testFreeze() {
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
		spyClass.freeze();
		verify(level, times(0)).freeze();
	}

}

package nl.tudelft.jpacman.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.npc.NPC;


/**
 * Tests various aspects of PacManUIBuilder.
 *
 * @author Alec Brunelle
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyStaticImports"})
public class PacManUIBuilderTest {

	/**
   	* The PacManUiBuilder under test.
   	*/
	private PacManUiBuilder pacManUiBuilder;
  
  	@Rule
  	public final ExpectedException exception = ExpectedException.none();
  
  	/**
	 * A Game object to be used in this Builder.
	 */
  	private final Game game = mock(Game.class);
  	
  	/**
	 * A Level object to be used in this Builder.
	 */
  	private final Level level = mock(Level.class);
  	
  	/**
	 * A Board object to be used in this Builder.
	 */
  	private final Board board = mock(Board.class);
  	
  	/**
	 * A PacManUI object to be used in this Builder.
	 */
  	private final PacManUI pacManUI = mock(PacManUI.class);

  /**
	 * Sets up PacManUiBuilder
	 */
	@Before
	public void setUp() {
		// mock functions
		when(game.getLevel()).thenReturn(level);
		when(level.getBoard()).thenReturn(board);
		
		// build new PacManUiBuilderObject
		pacManUiBuilder = new PacManUiBuilder();
		pacManUiBuilder.withDefaultButtons();
		
	}

	/*
	 * Validates that build calls addFreezeButton.
	 */
	@Test
	public void buildAddFreezeButton() {
		PacManUiBuilder spyClass = spy(PacManUiBuilder.class);
		
		// build the game
		pacManUiBuilder.build(game);
		
		verify(spyClass).addFreezeButton(game);
	}
	
	/*
	 * Validates that build cannot be called with null game.
	 */
	@Test
	public void buildNullGame() {
		exception.expect(AssertionError.class);
		pacManUiBuilder.build(null);
	}

//  /*
//	 * Validates that addFreezeButton() adds freeze button.
//   *
//   * Freeze button should call appropriate function.
//	 */
//	@Test
//	public void addFreezeButtonTestFreeze() {
//		fail("Not yet implemented");
//	}



}

## Part 1: Assess Quality of Tests using Line Coverage

### 1. Report three different scenarios you have tested and the corresponding coverage results.

1. A scenario which was tested was starting the game with the `start` button and then stopping the game with the `stop` button. This appears to freeze the game in its current state.

	This code is from: `/jpacman-framework/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java`.
	
	```java
	/**
	 * Adds a button with the caption {@value #STOP_CAPTION} that stops the
	 * game.
	 * 
	 * @param game
	 *            The game to stop.
	 */
	private void addStopButton(final Game game) {
		assert game != null;
	
		buttons.put(STOP_CAPTION, new Action() {
			@Override
			public void doAction() {
				game.stop();
			}
		});
	}
	```
	
	It appears that as the UI is built, the `stop` button is added to `this.buttons`. This functionality works but the test coverage is lacking. The action which is performed is `game.stop()` on line 14. After observing coverage results, it seems that line 9 doesn't have full branch coverage. The prediction here is that only one state of `game` is tested. Also on line 14 where `game.stop()` is called, no coverage exists. The prediction here is that no test exists where `doAction` is called.

2. A scenario tested was when the `stop` button was pressed, that no more moves could be made. 

	This code is from `/jpacman-framework/src/main/java/nl/tudelft/jpacman/level/Level.java`.
	
	```java
	/**
	 * Moves the unit into the given direction if possible and handles all
	 * collisions.
	 * 
	 * @param unit
	 *            The unit to move.
	 * @param direction
	 *            The direction to move the unit in.
	 */
	public void move(Unit unit, Direction direction) {
		assert unit != null;
		assert direction != null;

		if (!isInProgress()) {
			return;
		}

		synchronized (moveLock) {
			...
		}
	}
	```
	
	While this functionality works the branch coverage is lacking. On line 14, only one value of the predicate `isInProgress()` is used thus not coverage every branch test scenario.

3. A scenario tested was pressing the arrow keys to move pacman around the board.

	This code is from `/jpacman-framework/src/main/java/nl/tudelft/jpacman/ui/PacKeyListener.java`.
	
	```java
	@Override
	public void keyPressed(KeyEvent e) {
		assert e != null;
		Action action = mappings.get(e.getKeyCode());
		if (action != null) {
			action.doAction();
		}
	}
	```
	
	This is run everytime a key is pressed from the keyboard. It handles mapping a `KeyEvent` is a specific action, as this functionality works, the entire function does not have test coverage.
	
### 2. Report the coverage percentage. Identify the three least covered application classes. Identify the three least covered application classes. Explain why the tests for them are adequate or how they can be improved.

Three application classes exist with 0% coverage.

1. `/jpacman-framework/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java` 
	* Simply no tests import `CollisionInteractionMap` so none of its functions are used in tests.
2. `/jpacman-framework/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java`
	* Simply no tests import `DefaultPlayerInteractionMap` so none of its functions are used in tests.
3. `/jpacman-framework/src/main/java/nl/tudelft/jpacman/PacmanConfigurationException.java`
	* Simply no tests import `PacmanConfigurationException` so none of its functions are used in tests.

### 3. Measure the code coverage again, but this time with a configuration that has runtime assertion enabled (add ‘ -ea ’ as VM argument). To do this, right click on the project, select “Coverage As”, then go to “Coverage Configurations”. Then under “Arguments” add “ -ea ” to VM arguments. Explain the coverage changes you see.

* Without `-ea`

	![](/Users/alecbrunelle/Downloads/csc419_without_ea.png)
	
* With `-ea`

	![](/Users/alecbrunelle/Downloads/csc410_with_ea.png)	
	
Runtime assertions enable `assert` statements to be run. Some functions which act as invariants are held within `assert` statements thus causing less coverage to be obtained when running without `-ea`.

An example is here in `/jpacman-framework/src/main/java/nl/tudelft/jpacman/board/Board.java`. This is the coverage when run without `-ea`.

![](/Users/alecbrunelle/Downloads/csc410_with_ea_code.png)

And this is the coverage when run with `-ea`.

![](/Users/alecbrunelle/Downloads/csc410_code_with_ea.png)
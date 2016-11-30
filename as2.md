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


As you can see the function inside the `assert invariant()` was run during the tests when Runtime Assertion was enabled. However all branch coverage does not exist still around the `assert` statements indicating a lack of test coverage still exists.


## Part 2: Assess Quality of Tests using Mutation Testing

### 1. Run PIT with the default set of mutation operators on the existing test suite and measure mutation coverage. Report the results and compare them with line coverage results you got earlier. Explain what you see.

* Test coverage results:
	![](/Users/alecbrunelle/Downloads/csc419_without_ea.png)

* PIT coverage results:
	![](/Users/alecbrunelle/Downloads/csc410_pit_coverage_results.png)
	
As you can see line coverage seems to be very similar but mutation coverage has a large margin in difference.

Lets look at an example where there is a large difference in line coverage and mutatation coverage.

As you can see in `/jpacman-framework/src/main/java/nl/tudelft/jpacman/Launcher.java`, 100% line coverage..

![](/Users/alecbrunelle/Downloads/csc410_pit_comparison_1.png)

But in the same file we have marginally less mutation coverage:

![](/Users/alecbrunelle/Downloads/csc410_pit_comparison_2.png)

Mutation testing will run tests with some tests removed to see if they still pass. The red highlights here indicate that PIT ran the test suite with line 199 removed and the tests still passed. This indicates a `survived` mutation which it will not count into mutation coverage.

### 2. Run PIT with only “Conditionals Boundary Mutator”, “Increments Mutator”, and “Math Mutator”, respectively. Compare the results and explain.

The mutators specified are a subset of the default mutators used thus less mutators are used when running the test suite. 

One example here is shown.

As you can see this is the summary of mutation coverage results for `nl.tudelft.jpacman.board`.

![](/Users/alecbrunelle/Downloads/csc410_pit_mutations_config_2.png)

This is the same summary but with the specified mutators.

![](/Users/alecbrunelle/Downloads/csc410_pit_mutation_config.png)

As you can see, less classes were covered with mutators. This is because the classes which were excluded do not contain code which can pertain to the specific mutators chosen.

A more specific example is shown when looking at a class which did not get covered in our second PIT test coverage (the one with mutators specified).

![](/Users/alecbrunelle/Downloads/csc410_pit_mutator_example_1.png)

The rest of the file shows only green lines as well. This file was not covered in the test run where we specified mutators because the file does not contain lines with:

* Conditional boundaries: `<, <=, >, >=`
* Increments: `++, --`
* Mathematical operators: `+, -, ...`

### 3. Add one Junit test case to an appropriate package (e.g., in “ src/test/java/... ”) so that with it more mutants can be killed using default PIT configurations (i.e., the mutation score increases). Include your test case in the report and explain.

Found in `/jpacman-framework/src/main/java/nl/tudelft/jpacman/level/Level.java` starting on line 274.

```java
/**
 * Returns <code>true</code> iff at least one of the players in this level
 * is alive.
 * 
 * @return <code>true</code> if at least one of the registered players is
 *         alive.
 */
public boolean isAnyPlayerAlive() {
	for (Player p : players) {
		if (p.isAlive()) {
			return true;
		}
	}
	return false;
}
```

Before:

![](/Users/alecbrunelle/Downloads/csc410_level_before.png)

PIT reported this for the return line for `isAnyPlayerAlive`:

`1.1 Location : isAnyPlayerAlive
Killed by : none replaced return of integer sized value with (x == 0 ? 1 : 0) → SURVIVED`

This simply means that PIT tried running the test suite with returning True instead of False and they all succeeded. This means that the return value is not being asserted anywhere in the test suite.

After:

![](/Users/alecbrunelle/Downloads/csc410_level_after.png)

Test added:

```java
/**
 * Verifies if no players are in the game, no one is alive.
 */
@Test
@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public void isAnyPlayerAlive() {
	assertFalse(level.isAnyPlayerAlive());
}
```

With this test, the return value of `isAnyPlayerAlive` is checked, thus the mutation mentioned previously will be killed.

## Part 3: Extend Test Suite with Symbolic Execution


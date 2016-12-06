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

Code which needs to appear for the mutator to be applied to the test related test suite.

* Increments: `++, --`
* Mathematical operators: `+, -, ...`
* Conditional boundaries: `<, <=, >, >=`

As you can see this is the summary of mutation coverage results for `nl.tudelft.jpacman.board` using all mutators.

![](/Users/alecbrunelle/Downloads/csc410_pit_mutations_config_2.png)

#### Increments Mutator

This is the same summary but with the `Increments Mutator`.

![](/Users/alecbrunelle/Downloads/csc410_mutator_increments.png)

As you can see here, the mutation coverage technically rises! The reason for this is that code which is excluded from the report (`Direction.java` for example) did not include increments. PIT was smart enough to not run those tests as mutations with this setting does not apply to it.

#### Math Mutator

This is the same summary but with the `Math Mutator`.

![](/Users/alecbrunelle/Downloads/csc410_mutator_math.png)

As you can see here, the mutation coverage technically rises! The reason for this is that code which is excluded from the report (`Direction.java` for example) did not include addition, subtracting, modulus, etc. PIT was smart enough to not run those tests as mutations with this setting does not apply to it.

#### Condition Boundaries

This is the same summary but with the `Math Mutator`.

![](/Users/alecbrunelle/Downloads/csc410_mutator_conditionals.png)

As you can see here, the mutation coverage technically rises! The reason for this is that code which is excluded from the report (`Direction.java` for example) did not include conditional operators such as `>` and `<=`, etc. PIT was smart enough to not run those tests as mutations with this setting does not apply to it.

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

#### 1.

##### Part 1: Note that in order to apply SPF on a non-static method, you need to write a driver method ‘ static void main(String[]) ’ which instantiates the target class and calls the target methods.

```java
// added to Board.java
static void main(String[] args){
	Square x0y0 = new BasicSquare();
	Square x0y1 = new BasicSquare();
	Square x0y2 = new BasicSquare();
	Square x1y0 = new BasicSquare();
	Square x1y1 = new BasicSquare();
	Square x1y2 = new BasicSquare();
	Unit occupant = new BasicUnit();
	x0y0.put(occupant);
	Unit occupant1 = new BasicUnit();
	x0y1.put(occupant1);
	Unit occupant2 = new BasicUnit();
	x0y2.put(occupant2);
	Unit occupant3 = new BasicUnit();
	x1y0.put(occupant3);
	Unit occupant4 = new BasicUnit();
	x1y1.put(occupant4);
	Unit occupant5 = new BasicUnit();
	x1y2.put(occupant5);
	Square[][] grid = new Square[2][3];
	grid[0][0] = x0y0;
	grid[0][1] = x0y1;
	grid[0][2] = x0y2;
	grid[1][0] = x1y0;
	grid[1][1] = x1y1;
	grid[1][2] = x1y2;
	Board board = new Board(grid);
	board.withinBorders(1, 2);
}
```

When running `java -jar /Users/alecbrunelle/Github/jpf-core/build/RunJPF.jar +shell.port=4242 /Users/alecbrunelle/School/CSC410-A2/q3part1.jpf `...

```
Executing command: java -jar /Users/alecbrunelle/Github/jpf-core/build/RunJPF.jar +shell.port=4242 /Users/alecbrunelle/School/CSC410-A2/q3part1.jpf 
Running Symbolic PathFinder ...
symbolic.dp=choco
symbolic.string_dp_timeout_ms=0
symbolic.string_dp=none
symbolic.choco_time_bound=30000
symbolic.max_pc_length=2147483647
symbolic.max_pc_msec=0
symbolic.min_int=-1000000
symbolic.max_int=1000000
symbolic.min_double=-8.0
symbolic.max_double=7.0
JavaPathfinder core system v8.0 (rev ${version}) - (C) 2005-2014 United States Government. All rights reserved.


====================================================== system under test
nl.tudelft.jpacman.board.Board.main()

====================================================== search started: 01/12/16 2:12 PM

====================================================== Method Summaries
Inputs: x_1_SYMINT,y_2_SYMINT

nl.tudelft.jpacman.board.Board.withinBorders(0,0)  --> Return Value: 1
nl.tudelft.jpacman.board.Board.withinBorders(0,3)  --> Return Value: 0
nl.tudelft.jpacman.board.Board.withinBorders(0,-1000000)  --> Return Value: 0
nl.tudelft.jpacman.board.Board.withinBorders(2,-2147483648(don't care))  --> Return Value: 0
nl.tudelft.jpacman.board.Board.withinBorders(-1000000,-2147483648(don't care))  --> Return Value: 0

====================================================== Method Summaries (HTML)
<h1>Test Cases Generated by Symbolic JavaPath Finder for nl.tudelft.jpacman.board.Board.withinBorders (Path Coverage) </h1>
<table border=1>
<tr><td>x_1_SYMINT</td><td>y_2_SYMINT</td><td>RETURN</td></tr>
<tr><td>0</td><td>0</td><td>Return Value: 1</td></tr>
<tr><td>0</td><td>3</td><td>Return Value: 0</td></tr>
<tr><td>0</td><td>-1000000</td><td>Return Value: 0</td></tr>
<tr><td>2</td><td>-2147483648(don't care)</td><td>Return Value: 0</td></tr>
<tr><td>-1000000</td><td>-2147483648(don't care)</td><td>Return Value: 0</td></tr>
</table>

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:00:00
states:             new=9,visited=0,backtracked=9,end=5
search:             maxDepth=5,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=4
heap:               new=484,released=283,maxLive=476,gcCycles=8
instructions:       5296
max memory:         123MB
loaded code:        classes=77,methods=1681

====================================================== search finished: 01/12/16 2:12 PM
```

##### Part 2: Write a fully functional JUnit test class ‘ JPFBoardTest ’ for the test cases generated in a new file ‘/src/test/java/nl/tudelft/jpacman/board/JPFBoardTest.java ’. The test class should have a test fixture (i.e., @Before) and several test cases (i.e., @Test). Include your test class in the report as well.

Test class `/jpacman-framework/src/test/java/nl/tudelft/jpacman/board/JPFBoardTest.java`.

```java
package nl.tudelft.jpacman.board;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

/*
 * Test suite for JPF related Board class tests.
 */
public class JPFBoardTest {
	private Board board;
	
	private final Square x0y0 = mock(Square.class);
	private final Square x0y1 = mock(Square.class);
	private final Square x0y2 = mock(Square.class);
	private final Square x1y0 = mock(Square.class);
	private final Square x1y1 = mock(Square.class);
	private final Square x1y2 = mock(Square.class);
	
	private static final int MAX_WIDTH = 2;
	private static final int MAX_HEIGHT = 3;
	
	/**
	 * Setup a board that can be used for testing.
	 */
	@Before
	public void setUp() {
		Square[][] grid = new Square[MAX_WIDTH][MAX_HEIGHT];
		grid[0][0] = x0y0;
		grid[0][1] = x0y1;
		grid[0][2] = x0y2;
		grid[1][0] = x1y0;
		grid[1][1] = x1y1;
		grid[1][2] = x1y2;
		board = new Board(grid);
	}
	
	/**
	 * Verifies that given a position on the board, returns True.
	 */
	@Test
	public void verifyWithinBorders() {
		assertTrue(board.withinBorders(0, 0));
	}
	
	/**
	 * Verifies that given a position not on the board, returns False.
	 */
	@Test
	public void verifyWithinBordersOutsideBounds() {
		assertFalse(board.withinBorders(0, 3));
	}
	
	/**
	 * Verifies that given a position not on the board, returns False.
	 */
	@Test
	public void verifyWithinBordersOutsideBounds2() {
		assertFalse(board.withinBorders(0, -1000000));
	}
	
	/**
	 * Verifies that given a position not on the board, returns False.
	 */
	@Test
	public void verifyWithinBordersOutsideBounds3() {
		assertFalse(board.withinBorders(2, -2147483648));
	}
	
	/**
	 * Verifies that given a position not on the board, returns False.
	 */
	@Test
	public void verifyWithinBordersOutsideBounds4() {
		assertFalse(board.withinBorders(-1000000, -2147483648));
	}
}
```

#### 2

SPF did not generate test cases to cover all boundary values. `x` and `y` are bound by integers. This means valid values are found within a range of integers. Tests were generated for all `on` and `in` cases but not `off`. The table below explains which ones were there and which ones were missing.

Valid range for `x`: x >= 0, x < 2

Valid range for `y`: y >= 0, y < 3

|   Type| Range Value| Variable| Test case| Generated from SPF|
|---|---|---|---|---|
|   on| 0| x| `withinBorders(0,-1000000)`| Yes|
|   on| 2| x| `withinBorders(2,-2147483648(don't care))`| Yes|
| on| 0| y| `withinBorders(0,0)`| Yes|
| on| 3| y| `withinBorders(0,3)`| Yes|
| off| 1 or 3| x| see below| No|
| off| -1 or 1| x| see below| No|
| off| 2 or 4| y| see below| No|
| off| -1 or 1| y|  see below| No|
| in| 0 or 1| x and y| `withinBorders(0,0)`| Yes|


```java
/**
 * Verifies that given a position not on the board, returns False.
 */
@Test
public void verifyWithinBordersOffXUpperBound() {
	assertFalse(board.withinBorders(3, 1));
}
	
/**
 * Verifies that given a position not on the board, returns False.
 */
@Test
public void verifyWithinBordersOffXLowerBound() {
	assertFalse(board.withinBorders(-1, 1));
}
	
/**
 * Verifies that given a position not on the board, returns False.
 */
@Test
public void verifyWithinBordersOffYUpperBound() {
	assertFalse(board.withinBorders(0, 4));
}
	
/**
 * Verifies that given a position not on the board, returns False.
 */
@Test
public void verifyWithinBordersOffYLowerBound() {
	assertFalse(board.withinBorders(0, -1));
}
```

### Part 4

#### 1:

A couple of strategies learned during lectures which can be used to make a good test suite:

##### Black Box

- Manual Testing
	- All test scenarios logged

##### White Box

- Code Coverage (Jacoco)
	- Line Coverage 	
	- Branch Coverage
- Mutation Coverage (PIT)
- Symbolic Coverage (SPF)

#### Code Coverage

Code coverage was done iteratvly through the 


#### 2:



#### 3: What features have you tested? What approaches have you used to improve your tests? The marks will be given based on both your understanding as well as applications of different test strategies and the quality of your implementation and tests.

A Standard Organization of an Analysis and Test Plan
Analysis and test items:
The items to be tested or analyzed. The description of each item indicates version and installation
procedures that may be required.
Features to be tested:
The features considered in the plan.
Features not to be tested:
Features not considered in the current plan.
Approach:
The overall analysis and test approach, sufficiently detailed to permit identification of the major
test and analysis tasks and estimation of time and resources.
Pass/Fail criteria:
Rules that determine the status of an artifact subjected to analysis and test.
Suspension and resumption criteria:
Conditions to trigger suspension of test and analysis activities (e.g., an excessive failure rate)
and conditions for restarting or resuming an activity.
Risks and contingencies:
Risks foreseen when designing the plan and a contingency plan for each of the identified
risks.
Deliverables:
A list all A&T artifacts and documents that must be produced.
Task and Schedule:
A complete description of analysis and test tasks, relations among them, and relations between
A&T and development tasks, with resource allocation and constraints. A task schedule
usually includes GANTT and PERT diagrams.
Staff and responsibilities:
Staff required for performing analysis and test activities, the required skills, and the allocation
of responsibilities among groups and individuals. Allocation of resources to tasks is described in
the schedule.
Environmental needs:
Hardware and software required to perform analysis or testing activities.
	
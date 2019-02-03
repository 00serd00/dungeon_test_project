package com.fuzzybears;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class DungeonTest {

    private static final Dungeon fullOpenEntrance =
            new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                    "+######",
                    "+######",
                    "+######",
                    "+######"}));

    private static final Dungeon fullOpenExit =
            new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                    "######+",
                    "######+",
                    "######+",
                    "######+"}));

    private static final Dungeon topOneOpenedEntrance =
            new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                    "+######",
                    "#######",
                    "#######",
                    "#######"}));

    private static final Dungeon topOneOpenedExit =
            new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                    "######+",
                    "#######",
                    "#######",
                    "#######"}));

    private static final Dungeon bottomOneOpenedEntrance =
            new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                    "#######",
                    "#######",
                    "#######",
                    "+######"}));

    private static final Dungeon bottomOneOpenedExit =
            new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                    "#######",
                    "#######",
                    "#######",
                    "######+"}));

    @RunWith(Parameterized.class)
    public static class MatchTests {
        @Parameterized.Parameters(name = "{index}: {3}")
        public static Collection<Object[]> matchData() {
            return Arrays.asList(new Object[][]{
                    {fullOpenExit, fullOpenEntrance, true, "all exits match all entrances"},
                    {fullOpenEntrance, fullOpenExit, false, "none exits match to none entrances"},
                    {bottomOneOpenedExit, bottomOneOpenedEntrance, true, "only one exit match one entrance"},
                    {bottomOneOpenedExit, topOneOpenedEntrance, false, "has non matching exit and entrance"},
                    });
        }

        private final Dungeon leftDungeon;
        private final Dungeon rightDungeon;
        private final boolean expected;

        public MatchTests(Dungeon leftDungeon,
                          Dungeon rightDungeon,
                          boolean expected,
                          String message) {
            this.leftDungeon = leftDungeon;
            this.rightDungeon = rightDungeon;
            this.expected = expected;
        }

        @Test
        public void matchTest() {
            assertEquals(leftDungeon.match(rightDungeon), expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class HasExitTests {
        private final Dungeon dungeon;
        private final boolean expected;

        @Parameterized.Parameters(name = "{index}: {2}")
        public static Collection<Object[]> matchData() {
            return Arrays.asList(new Object[][]{
                    {fullOpenExit, true, "all exits are open"},
                    {fullOpenEntrance, false, "none of the exits are open"},
                    {bottomOneOpenedExit, true, "one exit is open"},
                    {topOneOpenedEntrance, false, "none of the exits are open but opened one entrance"}
            });
        }

        public HasExitTests(Dungeon dungeon, boolean expected, String message) {
            this.dungeon = dungeon;
            this.expected = expected;
        }

        @Test
        public void hasExit() {
            assertEquals(dungeon.hasExit(), expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class HasEntranceTests {
        private final Dungeon dungeon;
        private final boolean expected;

        @Parameterized.Parameters(name = "{index}: {2}")
        public static Collection<Object[]> matchData() {
            return Arrays.asList(new Object[][]{
                    {fullOpenEntrance, true, "all entrances are open"},
                    {fullOpenExit, false, "none of entrances are open"},
                    {bottomOneOpenedEntrance, true, "one entrances is open"},
                    {topOneOpenedExit, false, "none of the entrances are open but opened one exit"}
            });
        }

        public HasEntranceTests(Dungeon dungeon, boolean expected, String message) {
            this.dungeon = dungeon;
            this.expected = expected;
        }

        @Test
        public void hasEntrance() {
            assertEquals(dungeon.hasEntrance(), expected);
        }
    }
}
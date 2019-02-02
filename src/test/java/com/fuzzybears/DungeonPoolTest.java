package com.fuzzybears;

import junit.framework.AssertionFailedError;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class DungeonPoolTest {


    @Test
    public void DuplicatedDungeonTest() throws Exception {
        //Arrange
        File dungeonFile = new File(App.class.getResource("/duplicates.txt").getFile());
        List<Dungeon> testData = DungeonTestUtils.loadFromFile(dungeonFile);
        //Act
        DungeonPool dungeonPool = new DungeonPool(testData);
        //Assert
        assertThat(dungeonPool.getContents().size(), CoreMatchers.equalTo(2));
        assertTrue(dungeonPool.getContents().containsAll(testData));
    }

    @Test
    public void NotEnoughRoomsTest() throws Exception {
        //Arrange
        File dungeonFile = new File(App.class.getResource("/non_connected.txt").getFile());
        List<Dungeon> testData = DungeonTestUtils.loadFromFile(dungeonFile);
        //Act
        exceptionCheck(() -> new DungeonPool(testData),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.INPUT_CONTAINS_NON_CONNECTED_ROOMS);
    }

    @Test
    public void ClosedRoomsTest() throws Exception {
        //Arrange

        List<Dungeon> testData = Arrays.asList(new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                "#######",
                "#######",
                "#######",
                "#######"
        })), new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                "+#####+",
                "+++++++",
                "+#####+",
                "+#####+"
        })));
        //Act
        exceptionCheck(() -> new DungeonPool(testData),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.ROOM_MUST_HAVE_ENTRANCE_AND_EXIT);
    }

    @Test
    public void RoomWithoutExitTest() throws Exception {
        //Arrange

        List<Dungeon> testData = Arrays.asList(new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                "+######",
                "++++++#",
                "+######",
                "+######"
        })), new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                "+#####+",
                "+++++++",
                "+#####+",
                "+#####+"
        })));
        //Act
        exceptionCheck(() -> new DungeonPool(testData),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.ROOM_MUST_HAVE_ENTRANCE_AND_EXIT);
    }

    @Test
    public void RoomWithoutEntranceTest() throws Exception {
        //Arrange

        List<Dungeon> testData = Arrays.asList(new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                "######+",
                "#++++++",
                "######+",
                "######+"
        })), new Dungeon(DungeonTestUtils.decodeDungeon(new String[]{
                "+#####+",
                "+++++++",
                "+#####+",
                "+#####+"
        })));
        //Act
        exceptionCheck(() -> new DungeonPool(testData),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.ROOM_MUST_HAVE_ENTRANCE_AND_EXIT);
    }


    @Test
    public void CreateWithNullInInputSequence() {
        //Arrange
        List<Dungeon> testData = new ArrayList<>();
        testData.add(null);
        testData.add(null);
        //Act
        exceptionCheck(() -> new DungeonPool(testData),
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.INPUT_CONTAINS_NULL_ROOMS);
    }

    @Test
    public void CreatePoolWithoutDungeons() {
        //Act
        exceptionCheck(() -> new DungeonPool(null),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.DUNGEON_LIST_IS_MANDATORY);
    }

    @Test
    public void CreatePoolWithEmptyListOfDungeons() {
        //Act
        exceptionCheck(() -> new DungeonPool(Collections.emptyList()),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.NOT_ENOUGH_ROOMS);
    }

    @Test
    public void createXSequence() throws Exception {
        //Arrange
        File dungeonFile = new File(App.class.getResource("/dungeons.txt").getFile());
        List<Dungeon> testData = DungeonTestUtils.loadFromFile(dungeonFile);
        int sequenceSize = 50;
        DungeonPool pool = new DungeonPool(testData);
        //Act
        List<Dungeon> result = pool.createXSequence(sequenceSize);
        //Assert
        assertThat(result.size(), CoreMatchers.equalTo(sequenceSize));

        for (int i = 0; i < sequenceSize - 1; i++) {
            Dungeon current = result.get(i);
            Dungeon next = result.get(i + 1);

            assertNotEquals(current, next);

            assertTrue("Rooms does not match", IntStream.range(0, Dungeon.HEIGHT)
                                                        .filter(index -> current.getExit()[index] == Dungeon.Block.AIR)
                                                        .filter(index -> next.getEntrance()[index] == Dungeon.Block.AIR)
                                                        .findAny().isPresent());
        }
    }

    @Test
    public void createXSequenceOfNegativeLength() throws Exception {
        File dungeonFile = new File(App.class.getResource("/dungeons.txt").getFile());
        List<Dungeon> testData = DungeonTestUtils.loadFromFile(dungeonFile);
        DungeonPool pool = new DungeonPool(testData);
        //Act
        exceptionCheck(() -> pool.createXSequence(0),
                       //Assert
                       RuntimeException.class,
                       DungeonPool.ErrorMessages.SEQ_LENGTH_MUST_BE_POSITIVE);
    }


    public static void exceptionCheck(Callable statement,
                                      Class<? extends Exception> expectedException,
                                      String expectedMessage) {
        try {
            statement.call();
        } catch (Exception ex) {
            assertThat(ex, CoreMatchers.instanceOf(expectedException));
            assertThat(ex.getMessage(), CoreMatchers.equalTo(expectedMessage));
            return;
        }
        throw new AssertionFailedError("No exception has been thrown.");
    }
}
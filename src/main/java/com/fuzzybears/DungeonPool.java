package com.fuzzybears;

import java.util.*;
import java.util.stream.Collectors;

public class DungeonPool {
    public final List<Node> dungeonNodes;
    public static final Random random = new Random();

    public DungeonPool(List<Dungeon> inputDungeons) {
        Guard.checkArgumentExists(inputDungeons, ErrorMessages.DUNGEON_LIST_IS_MANDATORY);
        Guard.checkArgumentValid(inputDungeons.size() > 1, ErrorMessages.NOT_ENOUGH_ROOMS);
        dungeonNodes = new ArrayList<>();
        for (Dungeon dungeon : inputDungeons) {
            Guard.checkArgumentExists(dungeon, ErrorMessages.INPUT_CONTAINS_NULL_ROOMS);
            Guard.checkArgumentValid(dungeon.hasEntrance() &&
                                     dungeon.hasExit(),
                                     ErrorMessages.ROOM_MUST_HAVE_ENTRANCE_AND_EXIT);
            if (dungeonNodes.stream().noneMatch(item -> Objects.equals(item.dungeon, dungeon))) {
                Node newNode = new Node(dungeon, new ArrayList<>());
                for (Node dungeonNode : dungeonNodes) {
                    if (dungeon.match(dungeonNode.dungeon)) {
                        if (newNode.exitsTo.stream()
                                           .noneMatch(item -> Objects.equals(item.dungeon, dungeonNode.dungeon))) {
                            newNode.exitsTo.add(dungeonNode);
                        }
                    }
                    if (dungeonNode.dungeon.match(dungeon)) {
                        if (dungeonNode.exitsTo.stream()
                                               .noneMatch(item -> Objects.equals(item.dungeon, newNode.dungeon))) {
                            dungeonNode.exitsTo.add(newNode);
                        }
                    }
                }
                dungeonNodes.add(newNode);
            }
        }
        Guard.checkArgumentValid(dungeonNodes.stream().noneMatch(item -> item.exitsTo.size() == 0),
                                 ErrorMessages.INPUT_CONTAINS_NON_CONNECTED_ROOMS);
    }

    public List<Dungeon> createXSequence(int length) {
        Guard.checkArgumentValid(length > 0, ErrorMessages.SEQ_LENGTH_MUST_BE_POSITIVE);
        List<Dungeon> result = new ArrayList<>();
        Node prevNode = null;
        for (int i = 0; i < length; i++) {
            Node current;
            if (prevNode == null) {
                current = dungeonNodes.get(random.nextInt(dungeonNodes.size()));
            } else {
                current = prevNode.exitsTo.get(random.nextInt(prevNode.exitsTo.size()));
            }
            result.add(current.dungeon);
            prevNode = current;
        }
        return result;
    }

    public Set<Dungeon> getContents() {
        return dungeonNodes.stream().map(item -> item.dungeon).collect(Collectors.toSet());
    }

    private final class Node {
        final Dungeon dungeon;
        final List<Node> exitsTo;

        Node(Dungeon dungeon, List<Node> exitsTo) {
            this.dungeon = dungeon;
            this.exitsTo = exitsTo;
        }
    }

    public static final class ErrorMessages {
        public static final String INPUT_CONTAINS_NON_CONNECTED_ROOMS = "Input contains rooms that does't connected anywhere.";
        public static final String NOT_ENOUGH_ROOMS = "Input must be at lest 2 rooms, that can be put together in any order.";
        public static final String DUNGEON_LIST_IS_MANDATORY = "Dungeon list is mandatory.";
        public static final String SEQ_LENGTH_MUST_BE_POSITIVE = "Sequence length must be greater than 0.";
        public static final String ROOM_MUST_HAVE_ENTRANCE_AND_EXIT = "Room must have entrance and exit.";
        public static final String INPUT_CONTAINS_NULL_ROOMS = "Items in input sequence must not be null";
    }
}

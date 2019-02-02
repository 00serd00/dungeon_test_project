package com.fuzzybears;

import com.sun.media.sound.InvalidFormatException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fuzzybears.Dungeon.HEIGHT;

public class DungeonTestUtils {
    private DungeonTestUtils() {
    }

    public static void displaySequence(Collection<Dungeon> sequence, boolean withDelimiter) {
        StringBuilder lineBuilder = new StringBuilder();
        String delimiter = withDelimiter ? "|" : "";
        for (int i = 0; i < HEIGHT; i++) {
            for (Dungeon col : sequence) {
                for (int j = 0; j < Dungeon.WIDTH; j++) {
                    lineBuilder.append(col.getArea()[j][HEIGHT - i - 1] == Dungeon.Block.GROUND ? "#" : "-");
                }
                lineBuilder.append(delimiter);
            }
            lineBuilder.append("\n");
        }
        lineBuilder.append("\n");
        System.out.print(lineBuilder.toString());
    }

    public static Dungeon.Block[][] decodeDungeon(String[] rows) {
        Dungeon.Block[][] area = new Dungeon.Block[Dungeon.WIDTH][HEIGHT];
        for (int row = HEIGHT - 1; row >= 0; row--) {
            for (int column = 0; column < Dungeon.WIDTH; column++) {
                if ('#' == (rows[HEIGHT - row - 1].charAt(column))) {
                    area[column][row] = Dungeon.Block.GROUND;
                } else {
                    area[column][row] = Dungeon.Block.AIR;
                }
            }
        }
        return area;
    }

    public static List<Dungeon> loadFromFile(File dungeonFile) throws Exception {
        List<Dungeon> result = new ArrayList<>();
        if (dungeonFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(dungeonFile))) {
                int lineNumber = 0;
                String row;
                while ((row = reader.readLine()) != null) {
                    lineNumber++;
                    if (row.matches("[#+]{7}")) {
                        String[] dungeon = new String[HEIGHT];
                        dungeon[0] = row;
                        for (int i = 1; i < HEIGHT; i++) {
                            row = reader.readLine();
                            lineNumber++;
                            if (row.matches("[#+]{7}")) {
                                dungeon[i] = row;
                            } else {
                                throw new InvalidFormatException(String.format("File has invalid format, at line %d", lineNumber));
                            }
                        }
                        result.add(new Dungeon(decodeDungeon(dungeon)));
                    }
                }
            } catch (FileNotFoundException ex) {
                System.out.println("File Not found");
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Cant read file");
                ex.printStackTrace();
            }
        }
        return result;
    }
}

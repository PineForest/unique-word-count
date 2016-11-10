/*
 * Copyright © 2016  David Williams
 *
 * This file is part of the unique-word-count project.
 *
 * unique-word-count is free software: you can redistribute it and/or modify it under the terms of the
 * Lesser GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * unique-word-count is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Lesser GNU General Public
 * License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License along with unique-word-count.
 * If not, see <a href="http://www.gnu.org/licenses/">www.gnu.org/licenses/</a>.
 */
package algorithms.practice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>An interview code quiz an InfoSys client gave me.</p>
 *
 * Constraints:
 * <ul>
 *     <li>I assumed that "greater or equal" is valid for positive and negative integer values of n, but only positive
 *     integer values of the power of two integer.</li>
 *     <li>I assumed that the goal is to demonstrate the implementation of the fastest algorithm for finding the result,
 *     not to demonstrate the fastest speed to read and process large files. Due to this, the implementation is
 *     performance constrained by disk IO.</li>
 *     <li>I assumed that user friendly error messages, comments, and test code are a non-goal, though I did include a
 *     couple of these.</li>
 * </ul>
 *
 * The algorithm I used can be found at this link on <a href="https://en.wikipedia.org/wiki/Power_of_two#Fast_algorithms_to_round_any_integer_to_a_multiple_of_a_given_power_of_two">Wikipedia</a>.
 *
 * @author PineForest (see https://github.com/PineForest) 9/25/2015
 */
public class CountUniqueWordsAndAnagrams {
    private static Pattern WORD_CAPTURE_GROUP = Pattern.compile("\\b(\\S+)\\b");

    private StringBuilder contents = new StringBuilder();
    private Map<String,Integer> words = new HashMap<>();
    private Map<String,Integer> anagrams = new HashMap<>();

    public void readFile(String filepath) throws IOException {
        InputStream fis = Files.newInputStream(FileSystems.getDefault().getPath(filepath));
        InputStreamReader fisr = new InputStreamReader(fis, "UTF-8");
        while (fisr.ready()) {
            char character = (char) fisr.read();
            contents.append(character);
        }
    }

    public void generateCounts() {
        Matcher wordMatcher = WORD_CAPTURE_GROUP.matcher(contents);
        while (wordMatcher.find()) {
            String base = wordMatcher.group().toLowerCase();
            //System.out.println(base);
            Integer wordCount = words.get(base);
            words.put(base, wordCount == null ? 1 : wordCount + 1);
            String sorted = sort(base);
            //System.out.println(sorted);
            Integer anagramCount = anagrams.get(sorted);
            if (wordCount == null) {
                anagrams.put(sorted, anagramCount == null ? 0 : anagramCount + 1);
            }
        }
    }

    private String sort(String string) {
        char[] array = string.toCharArray();
        Arrays.sort(array);
        return new String(array);
    }

    public static void main( String[] args ) throws IOException {
        CountUniqueWordsAndAnagrams counter = new CountUniqueWordsAndAnagrams();
        counter.readFile(args[0]);
        counter.generateCounts();
        counter.writeCounts(System.out);
    }

    private void writeCounts(PrintStream out) {
        for (String word : words.keySet()) {
            String sorted = sort(word);
            out.println(String.format("%s: %d uses / %d anagrams", word, words.get(word), anagrams.get(sorted)));
        }
        out.println();
        out.flush();
    }
}

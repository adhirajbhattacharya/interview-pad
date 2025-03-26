package com.adhiraj.dsalgo;

import java.util.ArrayList;
import java.util.List;

public class WordSearch {

    // Main function to find words
    public static List<String> findWords(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();

        // Build a Trie from the words list
        TrieNode root = new TrieNode();
        for (String word : words) {
            addWordToTrie(word, root);
        }

        // Start DFS from each cell in the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                dfs(board, i, j, root, result);
            }
        }

        return result;
    }

    private static void addWordToTrie(String word, TrieNode root) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.word = word; // Mark the end of a word
    }

    // Helper function to perform DFS with the Trie
    private static void dfs(char[][] board, int x, int y, TrieNode node, List<String> result) {
        char c = board[x][y];
        if (c == '#' || node.children[c - 'a'] == null) {
            return; // Already visited or no matching child in the Trie
        }

        node = node.children[c - 'a'];
        if (node.word != null) {
            // Found a word
            result.add(node.word);
            node.word = null; // Avoid duplicate results
        }

        // Temporarily mark the current cell as visited
        board[x][y] = '#'; // not required as cycle not possible

        // Explore right and down directions
        if (y + 1 < board[0].length) dfs(board, x, y + 1, node, result); // Move right
        if (x + 1 < board.length) dfs(board, x + 1, y, node, result);     // Move down

        // Restore the current cell
        board[x][y] = c;
    }

    // Main function to find words
    public static List<String> findWordsUnoptimized(char[][] board, String[] words) {
        List<String> foundWords = new ArrayList<>();

        // For each word, start searching from each cell
        for (String word : words) {
            boolean wordFound = false;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (dfs(board, i, j, word, 0)) {
                        foundWords.add(word);
                        wordFound = true;
                        break; // Break the inner loop once word is found
                    }
                }
                if (wordFound) {
                    break; // Break the outer loop once word is found
                }
            }
        }

        return foundWords;
    }


    // Helper function to perform DFS
    private static boolean dfs(char[][] board, int x, int y, String word, int idx) {
        // If we have matched the entire word, return true
        if (idx == word.length()) {
            return true;
        }

        // If out of bounds or the character doesn't match, return false
        if (x >= board.length || y >= board[0].length || board[x][y] != word.charAt(idx)) {
            return false;
        }

        // Try moving right and down
        return dfs(board, x, y + 1, word, idx + 1) || dfs(board, x + 1, y, word, idx + 1);
    }

    public static void main(String[] args) {
        char[][] board = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'}
        };

        String[] words = {"abc", "abe", "adg", "bef", "gh", "gfi"};
        List<String> result = findWords(board, words);
        System.out.println(result); // Output: [abc, abe, adg, bef, gh]
    }
}

// Trie Node class
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    String word = null; // Stores the word when this node marks the end of a word
}

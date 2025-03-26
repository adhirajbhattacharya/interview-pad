package com.adhiraj.dsalgo;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        int[] nums = new int[] {1,2,3};
        System.out.println(Arrays.toString(nextGreaterElements(nums)));
    }

    public static int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        int  idx = 0;

        while (idx < 2 * n) {
            int i = idx % n;
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                int curr = stack.pop();
                res[curr] = i;
            }
            stack.push(i);
            idx++;
        }

        return res;
    }

    public static void mainWordFilter(String[] args) {
        WordFilter wf = new WordFilter(new String[] {"apple"});
        System.out.println(wf.f("a", "e"));
    }

    public static void mainAutocompleteSystem(String[] args) {
        AutocompleteSystem as = new AutocompleteSystem(new String[] {"i love you","island","iroman","i love leetcode"}, new int[] {5,3,2,2});
        List<String> res = as.input('i');
        System.out.println(res);
        res = as.input(' ');
        System.out.println(res);
        res = as.input('a');
        System.out.println(res);
        res = as.input('#');
        System.out.println(res);
    }

    public static void mainDjikstras(String[] args) {
        EdgeWeightedDigraph ewg = new EdgeWeightedDigraph(4);
        ewg.addEdge(new WeightedEdge(0, 3, 2));
        ewg.addEdge(new WeightedEdge(0, 1, 4));
        ewg.addEdge(new WeightedEdge(1, 2, 6));
        ewg.addEdge(new WeightedEdge(2, 3, -9));

        DjikstrasShortestPath dsp = new DjikstrasShortestPath(ewg, 0);
    }
}

class WordFilter {

    Trie prefixTrie = new Trie();
    Trie suffixTrie = new Trie();
    Map<String, Integer> wordIndexMap = new HashMap<>();
    static final List<String> EMPTY = new ArrayList<>();

    public WordFilter(String[] words) {
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (wordIndexMap.containsKey(word)) {
                wordIndexMap.put(word, i);
                continue;
            }
            prefixTrie.add(word, word);
            suffixTrie.add(reverseWord(word), word);
            wordIndexMap.put(word, i);
        }
    }

    public int f(String pref, String suff) {
        Set<String> prefixWords = prefixTrie.search(pref);
        Set<String> suffixWords = suffixTrie.search(reverseWord(suff));

        if (prefixWords.isEmpty() || suffixWords.isEmpty()) return -1;

        List<String> commonWords = findMaxCommonIdx(prefixWords, suffixWords);
        int max = -1;
        for (String word : commonWords) {
            max = Math.max(max, wordIndexMap.get(word));
        }
        return max;
    }

    private List<String> findMaxCommonIdx(Set<String> prefixWords, Set<String> suffixWords) {
        List<String> commonWords = new ArrayList<>();
        for (String word : prefixWords) {
            if (suffixWords.contains(word)) commonWords.add(word);
        }
        return commonWords;
    }

    private String reverseWord(String word) {
        return new StringBuilder(word).reverse().toString();
    }
}


class Trie {
    static Set<String> EMPTY_LIST = new HashSet<>();
    Trie[] children = new Trie[26];
    Set<String> words = new HashSet<>();

    void add(String input, String actualWord) {
        Trie node = this;
        char[] arr = input.toCharArray();
        for (char c : arr) {
            int idx = c - 'a';
            if (node.children[idx] == null) node.children[idx] = new Trie();
            node = node.children[idx];
            node.words.add(actualWord);
        }
    }

    Set<String> search(String match) {
        Trie node = this;
        char[] arr = match.toCharArray();
        for (char c : arr) {
            int idx = c - 'a';
            if (node.children[idx] == null) return EMPTY_LIST;
            node = node.children[idx];
        }
        return node.words;
    }
}

class DjikstrasShortestPath {
    Queue<PqEle> pq;
    int[] edgeFrom;
    double[] dist;

    DjikstrasShortestPath(EdgeWeightedDigraph ewg, int source) {
        this.pq = new PriorityQueue<>((a, b) -> Double.compare(a.d, b.d));
        this.edgeFrom = new int[ewg.v];
        this.dist = new double[ewg.v];
        for (int i = 0; i < ewg.v; i++) {
            edgeFrom[i] = source;
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[source] = 0D;
        pq.offer(new PqEle(source, 0D));

        while (!pq.isEmpty()) {
            PqEle curr = pq.poll();
            for (WeightedEdge e : ewg.adj[curr.v]){
                relaxEdge(e);
            }
        }
    }

    private void relaxEdge(WeightedEdge e) {
        int from = e.from;
        int to = e.to;

        if (dist[to] > dist[from] + e.weight) {
            edgeFrom[to] = from;
            dist[to] = dist[from] + e.weight;
            PqEle curr = new PqEle(to, dist[to]);
            pq.remove(curr);
            pq.offer(curr);
        }
    }
}

class PqEle {
    int v;
    double d;
    PqEle(int v, double d) {
        this.v = v;
        this.d = d;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof PqEle)) return false;
        return this.v == ((PqEle) obj).v;
    }
}

class WeightedEdge {
    int from;
    int to;
    int weight;

    public WeightedEdge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

class EdgeWeightedDigraph {
    int v;
    List<WeightedEdge>[] adj;
    EdgeWeightedDigraph(int v) {
        this.v = v;
        this.adj = (List<WeightedEdge>[]) new List[v];
        for (int i = 0; i < v; i++) this.adj[i] = new ArrayList<>();
    }

    void addEdge(WeightedEdge e) {
        this.adj[e.from].add(e);
    }
}

class AutocompleteSystem {

    TrieNode2 root = new TrieNode2();
    StringBuilder curr = new StringBuilder();
    static final List<String> EMPTY = new ArrayList<>();

    public AutocompleteSystem(String[] sentences, int[] times) {
        for (int i = 0; i < sentences.length; i++) {
            addToTrie(sentences[i], times[i]);
        }
    }

    private void addToTrie(String sentence, int count) {
        char[] arr = sentence.toCharArray();
        TrieNode2 node = root;
        for (char c : arr) {
            int idx = c == ' ' ? 26 : c - 'a';
            if (node.children[idx] == null) node.children[idx] = new TrieNode2();
            node = node.children[idx];
        }
        node.val = sentence;
        node.count = node.count + count;
    }

    public List<String> input(char c) {
        if (c == '#') {
            addToTrie(curr.toString(), 1);
            curr.setLength(0);
            return EMPTY;
        }
        curr.append(c);
        return getTop(3);
    }

    private List<String> getTop(int cnt) {
        TrieNode2 node = root;
        char[] arr = curr.toString().toCharArray();
        for (char c : arr) {
            int idx = c == ' ' ? 26 : c - 'a';
            if (node.children[idx] == null) return EMPTY;
            node = node.children[idx];
        }
        Queue<Pair> pq = new PriorityQueue<>(AutocompleteSystem::compare);
        dfs(node, pq);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            if (!pq.isEmpty()) result.add(pq.poll().sentence);
        }
        return result;
    }

    private void dfs(TrieNode2 node, Queue<Pair> pq) {
        if (node.val != null) pq.offer(new Pair(node.count, node.val));
        for (TrieNode2 t : node.children) {
            if (t == null) continue;
            dfs(t, pq);
        }
    }

    private static int compare(Pair a, Pair b) {
        return a.count == b.count ? a.sentence.compareTo(b.sentence) : b.count - a.count;
    }
}

class Pair {
    int count;
    String sentence;
    Pair (int count, String sentence) {
        this.count = count;
        this.sentence = sentence;
    }
}

class TrieNode2 {
    TrieNode2[] children;
    String val;
    int count;

    TrieNode2() {
        children = new TrieNode2[27];
        val = null;
        count = 0;
    }
}
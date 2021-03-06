package leetcode.medium;

import java.util.*;

/**
 * Created by lanxiao on 13/05/15.
 */
public class Q210 {
    /*
    LeetCode Q210:
        https://leetcode.com/problems/course-schedule-ii/
    Thinking process:
        This question asks for an order in which prerequisite courses must be taken first. This prerequisite relationship
        reminds one of directed graphs. Then, the problem reduces to find a topological sort order of the courses, which
        would be a DAG if it has a valid order.

        The first step is to transform it into a directed graph. Since it is likely to be sparse,we use adjacency list
        graph data structure. 1 -> 2 means 1 must be taken before 2.

        How can we obtain a topological sort order of a DAG?

        We observe that if a node has incoming edges, it has prerequisites. Therefore, the first few in the order must
        be those with no prerequisites, i.e. no incoming edges. Any non-empty DAG must have at least one node without
        incoming links. You can draw a small graph to convince yourself. If we visit these few and remove all edges
        attached to them, we are left with a smaller DAG, which is the same problem. This will then give our BFS solution.

        Another way to think about it is the last few in the order must be those which are not prerequisites of other
        courses. Thinking it recursively means if one node has unvisited child node, you should visit them first before
        you put this node down in the final order array. This sounds like the post-order of a DFS. Since we are putting
        nodes down in the reverse order, we should reverse it back to correct ordering.
    */

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] incLinkCounts = new int[numCourses];
        List<List<Integer>> adjs = new ArrayList<>(numCourses);
        initialiseGraph(incLinkCounts, adjs, prerequisites);
        //return solveByBFS(incLinkCounts, adjs);
        return solveByDFS(adjs);
    }

    private void initialiseGraph(int[] incLinkCounts, List<List<Integer>> adjs, int[][] prerequisites) {
        int n = incLinkCounts.length;
        while (n-- > 0) adjs.add(new ArrayList<Integer>());
        for (int[] edge : prerequisites) {
            incLinkCounts[edge[0]]++;
            adjs.get(edge[1]).add(edge[0]);
        }
    }

    private int[] solveByBFS(int[] incLinkCounts, List<List<Integer>> adjs) {
        int[] order = new int[incLinkCounts.length];
        Queue<Integer> toVisit = new ArrayDeque<>();
        for (int i = 0; i < incLinkCounts.length; i++) {
            if (incLinkCounts[i] == 0) toVisit.offer(i);
        }
        int visited = 0;
        while (!toVisit.isEmpty()) {
            int from = toVisit.poll();
            order[visited++] = from;
            for (int to : adjs.get(from)) {
                incLinkCounts[to]--;
                if (incLinkCounts[to] == 0) toVisit.offer(to);
            }
        }
        return visited == incLinkCounts.length ? order : new int[0];
    }

    private int[] solveByDFS(List<List<Integer>> adjs) {
        BitSet hasCycle = new BitSet(1);
        BitSet visited = new BitSet(adjs.size());
        BitSet onStack = new BitSet(adjs.size());
        Deque<Integer> order = new ArrayDeque<>();
        for (int i = adjs.size() - 1; i >= 0; i--) {
            if (visited.get(i) == false && hasOrder(i, adjs, visited, onStack, order) == false) return new int[0];
        }
        int[] orderArray = new int[adjs.size()];
        for (int i = 0; !order.isEmpty(); i++) orderArray[i] = order.pop();
        return orderArray;
    }

    private boolean hasOrder(int from, List<List<Integer>> adjs, BitSet visited, BitSet onStack, Deque<Integer> order) {
        visited.set(from);
        onStack.set(from);
        for (int to : adjs.get(from)) {
            if (visited.get(to) == false) {
                if (hasOrder(to, adjs, visited, onStack, order) == false) return false;
            } else if (onStack.get(to) == true) {
                return false;
            }
        }
        onStack.clear(from);
        order.push(from);
        return true;
    }

    public static void main(String[] args) {
        int n = 10;
        int[][] edges = new int[][]{{5, 8}, {3, 5}, {1, 9}, {4, 5}, {0, 2}, {1, 9}, {7, 8}, {4, 9}};
        Q210 q210 = new Q210();
        for (int i : q210.findOrder(n, edges)) System.out.println(i);
    }
}

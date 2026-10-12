
package dsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class OrganizationGraph {

    // Each employee ID maps to a list of direct reporting employees
    private HashMap<Integer, ArrayList<Integer>> adjacencyList;

    public OrganizationGraph() {
        adjacencyList = new HashMap<>();
    }

    // Add an employee as a vertex in the graph
    public void addEmployee(int employeeId) {

        if (!adjacencyList.containsKey(employeeId)) {
            adjacencyList.put(employeeId, new ArrayList<>());
        }
    }

    // Add a directed edge: supervisor -> employee
    public void addReportingRelation(int supervisorId, int employeeId) {

        addEmployee(supervisorId);
        addEmployee(employeeId);

        ArrayList<Integer> reports = adjacencyList.get(supervisorId);

        if (!reports.contains(employeeId)) {
            reports.add(employeeId);
        }
    }

    // Breadth-First Search using a queue
    public int[] bfs(int startId) {

        if (!adjacencyList.containsKey(startId)) {
            return new int[0];
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        ArrayList<Integer> result = new ArrayList<>();

        queue.add(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {

            int current = queue.remove();
            result.add(current);

            for (int employeeId : adjacencyList.get(current)) {

                if (!visited.contains(employeeId)) {
                    visited.add(employeeId);
                    queue.add(employeeId);
                }
            }
        }

        return convertToArray(result);
    }

    // Depth-First Search using recursion
    public int[] dfs(int startId) {

        if (!adjacencyList.containsKey(startId)) {
            return new int[0];
        }

        Set<Integer> visited = new HashSet<>();
        ArrayList<Integer> result = new ArrayList<>();

        dfsRecursive(startId, visited, result);

        return convertToArray(result);
    }

    private void dfsRecursive(int current,
            Set<Integer> visited,
            ArrayList<Integer> result) {

        visited.add(current);
        result.add(current);

        for (int employeeId : adjacencyList.get(current)) {

            if (!visited.contains(employeeId)) {
                dfsRecursive(employeeId, visited, result);
            }
        }
    }

    // Display the reporting hierarchy from the root employee
    public void displayHierarchy() {

        System.out.println("\n========== ORGANIZATION HIERARCHY ==========");

        // Employee 1001 is the root in our predefined dataset
        displayRecursive(1001, 0, new HashSet<>());

        System.out.println("============================================");
    }

    private void displayRecursive(int employeeId, int level,
            Set<Integer> visited) {

        if (!adjacencyList.containsKey(employeeId) ||
                visited.contains(employeeId)) {
            return;
        }

        visited.add(employeeId);

        // Indentation represents the reporting level
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }

        System.out.println("|-- Employee " + employeeId);

        for (int childId : adjacencyList.get(employeeId)) {
            displayRecursive(childId, level + 1, visited);
        }
    }

    // Convert the traversal result into an integer array
    private int[] convertToArray(ArrayList<Integer> list) {

        int[] result = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }
}

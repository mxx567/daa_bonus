package datastructures;

import java.util.Random;

public class Tests {

    private static final int REPETITIONS = 5;
    private static volatile int result;

    public static void run() {
        int[] sizes = {100, 1000, 10000};


        int[] warmup = generateData(10000);
        for (int i = 0; i < 20; i++) {
            testBST(warmup);
            testHashTable(warmup);
        }

        System.out.printf(
                "%-8s %-12s %12s %12s %12s%n",
                "n", "Structure", "Insert (ms)", "Search (ms)", "Delete (ms)"
        );

        for (int n : sizes) {
            int[] data = generateData(n);

            long[] bstTotals = new long[3];
            long[] hashTotals = new long[3];

            for (int run = 0; run < REPETITIONS; run++) {
                if (run % 2 == 0) {
                    addTimes(bstTotals, testBST(data));
                    addTimes(hashTotals, testHashTable(data));
                } else {
                    addTimes(hashTotals, testHashTable(data));
                    addTimes(bstTotals, testBST(data));
                }
            }

            printResults(n, "BST", bstTotals);
            printResults(n, "Hash table", hashTotals);
        }
    }

    private static long[] testBST(int[] data) {
        BinarySearchTree tree = new BinarySearchTree();

        long start = System.nanoTime();

        for (int value : data) {
            tree.insert(value);
        }

        long insertTime = System.nanoTime() - start;

        int found = 0;
        start = System.nanoTime();

        for (int value : data) {
            if (tree.contains(value)) {
                found++;
            }
        }

        long searchTime = System.nanoTime() - start;
        result = found;

        check(found == data.length, "BST: inserted values missing");
        check(!tree.contains(-1), "BST: found an absent value");

        start = System.nanoTime();

        for (int value : data) {
            tree.delete(value);
        }

        long deleteTime = System.nanoTime() - start;

        check(tree.isEmpty(), "BST: not empty after deletion");

        return new long[]{insertTime, searchTime, deleteTime};
    }

    private static long[] testHashTable(int[] data) {
        HashTable<Integer, Integer> table = new HashTable<>();

        Integer[] keys = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            keys[i] = data[i];
        }

        long start = System.nanoTime();

        for (Integer key : keys) {
            table.put(key, key);
        }

        long insertTime = System.nanoTime() - start;

        int found = 0;
        start = System.nanoTime();

        for (Integer key : keys) {
            if (table.containsKey(key)) {
                found++;
            }
        }

        long searchTime = System.nanoTime() - start;
        result = found;

        check(found == data.length, "Hash table: inserted keys missing");
        check(table.size() == data.length, "Hash table: incorrect size");
        check(!table.containsKey(-1), "Hash table: found an absent key");

        for (Integer key : keys) {
            check(key.equals(table.get(key)),
                    "Hash table: incorrect stored value");
        }


        start = System.nanoTime();

        for (Integer key : keys) {
            table.remove(key);
        }

        long deleteTime = System.nanoTime() - start;

        check(table.isEmpty(), "Hash table: not empty after deletion");

        return new long[]{insertTime, searchTime, deleteTime};
    }

    private static int[] generateData(int n) {
        int[] data = new int[n];
        Random random = new Random(63);

        for (int i = 0; i < n; i++) {
            data[i] = i * 1000 + random.nextInt(1000);
        }

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }

        return data;
    }

    private static void addTimes(long[] totals, long[] times) {
        for (int i = 0; i < totals.length; i++) {
            totals[i] += times[i];
        }
    }

    private static void printResults(
            int n, String structure, long[] totals) {

        double divisor = REPETITIONS * 1_000_000.0;

        System.out.printf(
                java.util.Locale.US,
                "%-8d %-12s %12.4f %12.4f %12.4f%n",
                n,
                structure,
                totals[0] / divisor,
                totals[1] / divisor,
                totals[2] / divisor
        );
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
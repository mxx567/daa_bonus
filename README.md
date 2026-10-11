# Binary Search Tree and Hash Table

Java implementations of an unbalanced Binary Search Tree (BST) and a hash table using separate chaining.

## Operations

- **BST:** Insertion and searching compare values and follow left or right branches. Deletion handles nodes with zero, one, or two children.
- **Hash table:** Operations calculate a bucket index and scan its linked list when collisions occur. The table resizes when its load factor exceeds 0.75.

## Time Complexity

| Structure | Operation | Best | Expected* | Worst |
|---|---|---|---|---|
| BST | Insert | Θ(1) | O(log n) | Θ(n) |
| BST | Search | Θ(1) | O(log n) | Θ(n) |
| BST | Delete | Θ(1) | O(log n) | Θ(n) |
| Hash table | Insert | Θ(1) | O(1), amortized | Θ(n) |
| Hash table | Search | Θ(1) | O(1) | Θ(n) |
| Hash table | Delete | Θ(1) | O(1) | Θ(n) |

*Expected performance assumes random insertion order for the BST and well-distributed hashes for the hash table.

The BST’s worst case occurs when it becomes a chain. The hash table’s worst case occurs with many collisions; insertion also takes linear time during resizing.

## Testing

Both structures were tested with 100, 1,000, and 10,000 unique keys.

- Same shuffled data for both structures, using `Random(63)`.
- Five repetitions after JVM warm-up.
- Timing with `System.nanoTime()`.
- Input generation and correctness checks excluded from timing.
- Checks verify successful searches and empty structures after deletion.

Run the tests from `Main.java`:

```java
public class Main {
    public static void main(String[] args) {
        Tests.run();
    }
}
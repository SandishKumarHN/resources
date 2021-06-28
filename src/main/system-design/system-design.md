### Sort 1TB file ###

This problem can be reduced to a simpler problem. This problem was designed to force you to an approach. Here it is:

- Pick up chunks =~ 1GB, sort & store them as separate sorted files.
- You end up with 1000 1GB sorted files on the file system.
- Now, its simply a problem of merging k-sorted arrays into a new array.
- Merging k-sorted arrays need you to maintain a min-heap (Priority Queue) with k elements at a time.
i.e. k = 1000 (files) in our case. ( 1GB ram can store 1000 numbers )

Therefore, keep poping elements from your priority queue and save to disk.
You will have a new file, sorted of size 1TB.
Refer: http://www.geeksforgeeks.org/merge-k-sorted-arrays/

Update
PS: Can be done on a single machine with 1 GB RAM with a better data structure
Merge can be done in less than O(N) space with Priority Queue i.e. O(K) space i.e. the heart of the problem.

### 
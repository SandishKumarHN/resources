### Graph Patterns:

#### Union Find:
  https://www.youtube.com/watch?v=4avlWhSdM2I
     
  1)  https://leetcode.com/problems/friend-circles/
  2)  https://leetcode.com/problems/redundant-connection/
  3)  https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/
  4)  https://leetcode.com/problems/number-of-operations-to-make-network-connected/
  5)  https://leetcode.com/problems/satisfiability-of-equality-equations/
  6)  https://leetcode.com/problems/accounts-merge/
  7)  https://leetcode.com/problems/connecting-cities-with-minimum-cost/
  
#### DFS:
  ##### DFS From Boundery
  8)  https://leetcode.com/problems/surrounded-regions/
  9)  https://leetcode.com/problems/number-of-enclaves/
  ##### Shortest time:
  10) https://leetcode.com/problems/time-needed-to-inform-all-employees/
  ##### Islands Variants:
  11) https://leetcode.com/problems/number-of-closed-islands/
  12) https://leetcode.com/problems/number-of-islands/
  13) https://leetcode.com/problems/keys-and-rooms/
  14) https://leetcode.com/problems/max-area-of-island/
  15) https://leetcode.com/problems/flood-fill/
  16) https://leetcode.com/problems/coloring-a-border/
  ##### Hash/DFS:
  17) https://leetcode.com/problems/employee-importance/
  18) https://leetcode.com/problems/find-the-town-judge/
  ##### Cycle Find:
  19) https://leetcode.com/problems/find-eventual-safe-states/
  
#### BFS:
  ##### BFS for shortest path:
  20) https://leetcode.com/problems/01-matrix/
  21) https://leetcode.com/problems/as-far-from-land-as-possible/
  22) https://leetcode.com/problems/rotting-oranges/
  23) https://leetcode.com/problems/shortest-path-in-binary-matrix/

#### Graph coloring:
  24) https://leetcode.com/problems/possible-bipartition/
  25) https://leetcode.com/problems/is-graph-bipartite/
  
#### Topological Sort:
  26) https://leetcode.com/problems/course-schedule-i/
  27) https://leetcode.com/problems/course-schedule-ii/
  28) https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
  29) https://leetcode.com/problems/alien-dictionary/
  30) https://leetcode.com/problems/clone-graph/
  31) https://leetcode.com/problems/minimum-height-trees/submissions/
  32) https://leetcode.com/problems/pacific-atlantic-water-flow/
  33) https://leetcode.com/problems/longest-consecutive-sequence/
  34) https://leetcode.com/problems/graph-valid-tree/
  35) https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/
  36) https://leetcode.com/problems/sequence-reconstruction/

#### Shortest Path:
  37) https://leetcode.com/problems/network-delay-time/
  38) https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
  39) https://leetcode.com/problems/cheapest-flights-within-k-stops/
  40) https://leetcode.com/problems/path-with-maximum-probability/
  41) https://leetcode.com/problems/the-maze-ii/
  42) https://leetcode.com/problems/the-maze-iii/
  43) https://leetcode.com/problems/path-with-minimum-effort/
  44) https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
  45) https://leetcode.com/problems/critical-connections-in-a-network/
  46) https://leetcode.com/problems/minimize-malware-spread/
  47) https://leetcode.com/problems/minimize-malware-spread-ii/
  48) https://leetcode.com/problems/minimum-number-of-days-to-disconnect-island/ 
  
#### 1: Union Find:
  1) https://www.youtube.com/watch?v=4avlWhSdM2I
  
 ```
     class UnionFind {
         int[] parents;
         int[] size;
         int numComponents = 0;
         
         public UnionFind(int n) {
             this.parents = new int[n];
             this.size = new int[n];
             this.numComponents = n;
             
             for(int i = 0; i < this.parents.length; i++) {
                 this.parents[i] = i;
                 this.size[i] = 1;
             }
         }
         
         public int find(int cur) {
             // Find root of the component or set
             int root = cur;
             while(root != this.parents[root]) {
                 root = this.parents[root];
             }
             
             // compress the path leading to the root, Path Compression
             // it gives constant time
             while(cur != root) {
                 int preParent = this.parents[cur];
                 this.parents[cur] = root;
                 cur = preParent;
             }
             return root;
         }
         
         public int findComponentsSize(int cur) {
             int parent = find(cur);
             return this.size[parent];
         }
         
         public boolean connected(int node1, int node2) {
             return find(node1) == find(node2);
         }
         
         // unify the compenets
         public void union(int node1, int node2) {
             int node1Parent = find(node1);
             int node2Parent = find(node2);
             
             // alredy both are in same group and set
             if(node1Parent == node2Parent) return;
             
             //merge two components/set in the same group
             // merge smaller component/set into smaller group.
             if(this.size[node1Parent] > this.size[node2Parent]) {
                 this.parents[node2Parent] = node1Parent;
                 this.size[node1Parent] += this.size[node2Parent];
             } else {
                 this.parents[node1Parent] = node2Parent;
                 this.size[node2Parent] += this.size[node1Parent];
             }
             this.numComponents--;
         }
     }
 ```
 2) https://leetcode.com/problems/number-of-provinces/
 ```
     public int findCircleNum(int[][] isConnected) {
         int n = isConnected.length;
         UnionFind uf = new UnionFind(n);
         for(int i = 0; i < n; i++) {
             for(int j = 0; j < n; j++) {
                 if(i == j || isConnected[i][j] == 0) continue;
                 uf.union(i, j);
             }
         }
         return uf.numComponents;
     }
 ```
 3) https://leetcode.com/problems/redundant-connection/
 ```
     public int[] findRedundantConnection(int[][] edges) {
         UnionFind uf = new UnionFind(edges.length + 1);
         for(int[] edge : edges) {
             if(!uf.union(edge[0], edge[1])) return edge;
         }
         return new int[0];
     }
 ```
 4) https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/
 
 ```
     public int removeStones(int[][] stones) {
         int n = stones.length;
         UnionFind uf = new UnionFind(n);
         for(int i = 0; i < n; i++) {
             for(int j = i + 1; j < n; j++) {
                 // stone with same row or column. group them into one
                 if(isConncted(stones[i], stones[j]))
                     uf.union(i, j);
             }
         }
         return n - uf.numComponents;
     }
     
     public boolean isConncted(int[] node1, int[] node2) {
         return node1[0] == node2[0] || node1[1] == node2[1];
     }
 ```
 5) https://leetcode.com/problems/number-of-operations-to-make-network-connected/
 ```
     public int makeConnected(int n, int[][] connections) {
         if(connections.length < n - 1) return -1;
         
         UnionFind uf = new UnionFind(n);
         for(int[] con : connections) {
                 uf.union(con[0], con[1]);
         }
         return uf.numComponents - 1;
     }
 ```
6) https://leetcode.com/problems/satisfiability-of-equality-equations/

```
    public boolean equationsPossible(String[] equations) {
        UnionFind uf = new UnionFind(26);
        for(String s : equations) {
            if(s.charAt(1) == '=') {
                uf.union(s.charAt(0) - 'a', s.charAt(3) - 'a');
            }
        }
        
        for(String s : equations) {
            if(s.charAt(1) == '!') {
                if(uf.connected(s.charAt(0) - 'a', s.charAt(3) - 'a')) {
                    return false;
                }
            }
        }
        return true;
    }
``` 
7) https://leetcode.com/problems/accounts-merge/
```
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, String> emailToName = new HashMap<>();
        Map<String, Integer> emailToId = new HashMap<>();
        UnionFind uf = new UnionFind(10001);
        
        int id = 0;
        for(List<String> account : accounts) {
            String name = "";
            for(String mail: account) {
                if(name == "") {
                    name = mail;
                    continue;
                }
                emailToName.put(mail, name);
                if(!emailToId.containsKey(mail)) {
                    emailToId.put(mail, id++);
                }
                // union them
                uf.union(emailToId.get(account.get(1)), emailToId.get(mail));
            }
        }
        
        Map<Integer, List<String>> ans = new HashMap<>();
        for(String mail: emailToName.keySet()) {
            // find the root of mail by index
            int index = uf.find(emailToId.get(mail));
            ans.computeIfAbsent(index, x -> new ArrayList<>()).add(mail);
        }
        
        for(List<String> componenet: ans.values()) {
            Collections.sort(componenet);
            // add Name back
            componenet.add(0, emailToName.get(componenet.get(0)));
        }
        return new ArrayList(ans.values());
    }
```
8) https://leetcode.com/problems/connecting-cities-with-minimum-cost/
```
    public int minimumCost(int n, int[][] connections) {
        UnionFind uf = new UnionFind(n + 1);
        Arrays.sort(connections, (a, b) -> (a[2] - b[2]));
        int cost = 0;
        int total = 0;
        for(int[] con : connections) {
            if(uf.connected(con[0], con[1])) continue;
            uf.union(con[0], con[1]);
            cost += con[2];
            total++;
        }
        
        return total == n - 1 ? cost : -1;
    }
``` 

#### DFS- DFS from boundary:
9) https://leetcode.com/problems/surrounded-regions/
```
    public void solve(char[][] board) {
        int rowLen = board.length;
        int colLen = board[0].length;
        for(int i = 0; i < rowLen; i++) {
            if(board[i][0] == 'O') {
                dfs(board, i, 0);
            }
            if(board[i][colLen - 1] == 'O') {
                dfs(board, i, colLen - 1);
            }
        }
        
        for(int i = 0; i < colLen; i++) {
            if(board[0][i] == 'O') {
                dfs(board, 0, i);
            }
            
           if(board[rowLen - 1][i] == 'O') {
                dfs(board, rowLen - 1, i);
            }
        }
        
        for(int i = 0; i < rowLen; i++) {
            for(int j = 0; j < colLen; j++) {
                if(board[i][j] == 'O') {
                   board[i][j] = 'X' ;
                }
                if(board[i][j] == 'E') {
                   board[i][j] = 'O' ;
                }
            }
        }
    }
    
    public void dfs(char[][] board, int row, int col) {
        if(row < 0 || row >= board.length) return;
        if(col < 0 || col >= board[0].length) return;
        if(board[row][col] == 'X' || board[row][col] == 'E') return;
        board[row][col] = 'E';
        dfs(board, row, col + 1);
        dfs(board, row, col - 1);
        dfs(board, row + 1, col);
        dfs(board, row - 1, col);
    }
```
10) https://leetcode.com/problems/number-of-enclaves/
```
Same as above but return the count
```
### DFS Shortest time:
11) https://leetcode.com/problems/time-needed-to-inform-all-employees/
```
    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        Map<Integer, List<Integer>> subOrdinates = new HashMap<>();
        for(int i = 0; i < manager.length; i++) {
            int head = manager[i];
            if(!subOrdinates.containsKey(head)) {
                subOrdinates.put(head, new ArrayList<>());
            }
            subOrdinates.get(head).add(i);
        }
        return dfs(subOrdinates, informTime, headID);
    }
    
    public int dfs(Map<Integer, List<Integer>> subOrdinates, int[] informTime, int headId) {
        int max = 0;
        if(!subOrdinates.containsKey(headId)) {
            return max;
        }
        for(int i = 0; i < subOrdinates.get(headId).size(); i++) {
            max = Math.max(max, dfs(subOrdinates, informTime, subOrdinates.get(headId).get(i)));
        }
        return max + informTime[headId];
    }
```
### DFS Islands Variants:
12) https://leetcode.com/problems/number-of-closed-islands/
```
boolean flag = true;
    public int closedIsland(int[][] grid) {
        int res = 0;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 0) {
                    dfs(grid, i, j);
                    
                    //check if 0 isn't border/connected to border
                    if(flag)
                        res++;
                    flag = true;
                }
            }
        }
        return res;
    }
    
    public void dfs(int[][] grid, int row, int col) {
        if(row < 0 || row >= grid.length) return;
        if(col < 0 || col >= grid[0].length) return;
        if(grid[row][col] == 1) return;
        
        //If other 0's are connected to border then dont increase ans
        if(row == 0 || col == 0 || row == grid.length - 1 || col == grid[0].length - 1 & grid[row][col] == 0)
            flag = false;
        
        
        grid[row][col] = 1;
        
        dfs(grid, row, col + 1);
        dfs(grid, row, col - 1);
        dfs(grid, row - 1, col);
        dfs(grid, row + 1, col);
    }
```

13) https://leetcode.com/problems/number-of-islands/
```
    public int numIslands(char[][] grid) {
        int numIslands = 0;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == '0') continue;
                dfs(grid, visited, i, j);
                numIslands++;
            }
        }
        return numIslands;
    }
    
    public void dfs(char[][] grid, boolean[][] visited, int row, int col) {
        if(row < 0 || row >= grid.length ) return;
        if(col < 0 || col >= grid[0].length) return;
        if(grid[row][col] == '0') return;
        
        grid[row][col] = '0';
        dfs(grid, visited, row, col + 1);
        dfs(grid, visited, row, col - 1);
        dfs(grid, visited, row - 1, col);
        dfs(grid, visited, row + 1, col);
    }
```
14) https://leetcode.com/problems/max-area-of-island/
```
    boolean[][] seen;
    public int maxAreaOfIsland(int[][] grid) {
        int maxAreaOfIsland = 0;
        seen = new boolean[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 0) continue;
                maxAreaOfIsland = Math.max(maxAreaOfIsland, dfs(grid, i, j));
            }
        }
        return maxAreaOfIsland;
    }
    
    public int dfs(int[][] grid, int row, int col) {
        if(row < 0 || row >= grid.length ) return 0;
        if(col < 0 || col >= grid[0].length) return 0;
        if(grid[row][col] == 0 || seen[row][col]) return 0;
        
        seen[row][col] = true;
        return (dfs(grid, row, col + 1) + dfs(grid, row, col - 1) + dfs(grid, row - 1, col) + dfs(grid, row + 1, col) + 1);
    }
```
15) https://leetcode.com/problems/keys-and-rooms/
```
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int[] visited = new int[rooms.size()];
        visited[0] = 0;
        int count = 0;
        
        for(int i = 0; i < rooms.size(); i++) {
            if(visited[i] == 0) {
                dfs(i, rooms, visited);
                count++;
            }
        }
        return count == 1 ? true : false;
    }
    
    public void dfs(int roomNum, List<List<Integer>> rooms, int[] visited) {
        visited[roomNum] = 1;
        for(int num : rooms.get(roomNum)) {
            if(visited[num] == 0) {
                dfs(num, rooms, visited);
            }
        }
    }
```
16) https://leetcode.com/problems/flood-fill/
```
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int color = image[sr][sc];
        if(color != newColor) {
            dfs(image, sr, sc, color, newColor);
        }
        return image;
    }
    
    public void dfs(int[][] image, int row, int col, int color, int newColor) {
        if(row < 0 || row >= image.length) return;
        if(col < 0 || col >= image[0].length) return;
        if(image[row][col] != color) return;
        
        image[row][col] = newColor;
        
        dfs(image, row, col + 1, color, newColor);
        dfs(image, row, col - 1, color, newColor);
        dfs(image, row + 1 , col, color, newColor);
        dfs(image, row - 1, col, color, newColor);
    }
```

17) https://leetcode.com/problems/making-a-large-island/
```
public int largestIsland(int[][] grid) {
        Map<Integer, Integer> dict = new HashMap<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        
        int count  = 1;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 1 && !visited[i][j]) {
                    dict.put(count, 0);
                    dfs(dict, grid, visited, i, j, count++);
                }
            }
        }
        
        int max = 0;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                Set<Integer> seen = new HashSet<>();
                if(grid[i][j] == 0) {
                    int[][] dirs = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                    for(int[] dir : dirs) {
                        int nRow = i + dir[0];
                        int nCol = j + dir[1];
                        
                        if(nRow < 0 || nRow >= grid.length) continue;
                        if(nCol < 0 || nCol >= grid[0].length) continue;
                        if(grid[nRow][nCol] == 0) continue;
                        
                        seen.add(grid[nRow][nCol]);                        
                    }
                    int first = 0;
                    for(int num : seen){
                        first += dict.get(num);
                    }
                    max = Math.max(max, first + 1);
                }
            }
        }
        return max == 0? grid.length * grid.length : max;
        
    }
    
    public void dfs(Map<Integer, Integer> dict, int[][] grid, boolean[][] visited, int row, int col, int count) {
        if(row < 0 || row >= grid.length) return;
        if(col < 0 || col >= grid[0].length) return;
        if(grid[row][col] == 0 || visited[row][col]) return;
        
        grid[row][col] = count;
        dict.put(count, dict.get(count) + 1);
        visited[row][col] = true;
        
        dfs(dict, grid, visited, row, col + 1, count);
        dfs(dict, grid, visited, row, col - 1, count);
        dfs(dict, grid, visited, row + 1, col, count);
        dfs(dict, grid, visited, row - 1, col, count);
    }
```

### DFS: Cycle Find:
18) https://leetcode.com/problems/find-eventual-safe-states/

```
public List<Integer> eventualSafeNodes(int[][] graph) {
        Set<Integer> visiting = new HashSet<>();
        Set<Integer> visited = new HashSet<>();
        List<Integer> result = new ArrayList<>();
        
        for(int i = 0; i < graph.length; i++){
            if(!hasCycle(graph, visiting, visited, i)) {
                result.add(i);
            }
        }
        return result;
    }
    
    public boolean hasCycle(int[][] graph, Set<Integer> visiting, Set<Integer> visited, int index) {
        if(visiting.contains(index)) {
            return true;
        }
        if(visited.contains(index)) {
            return false;
        }
        
        visiting.add(index);
        
        for(int  edge: graph[index]) {
            if(hasCycle(graph, visiting, visited, edge)) {
                return true;
            }
        }
        visiting.remove(index);
        visited.add(index);
        return false;
    }
```
### BFS:

19) https://leetcode.com/problems/01-matrix/

```
    public int[][] updateMatrix(int[][] mat) {
        Queue<int[]> queue = new LinkedList<>();
        for(int i = 0; i < mat.length; i++) {
            for(int j = 0; j < mat[0].length; j++) {
                if(mat[i][j] == 0) {
                    queue.add(new int[]{i, j});
                } else {
                    mat[i][j] = -1;
                }
            }
        }
        
        int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        
        int count = 0;
        while(!queue.isEmpty()) {
            int size = queue.size();
            count++;
            for(int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                
                for(int[] dir : dirs) {
                    int nRow = cur[0] + dir[0];
                    int nCol = cur[1] + dir[1];
                    if(nRow >= 0 && nRow < mat.length &&
                      nCol >= 0 && nCol < mat[0].length) {
                        if(mat[nRow][nCol] == -1) {
                            mat[nRow][nCol] = count;
                            queue.add(new int[]{nRow, nCol});
                        }
                    }
                }
            }
        }
        return mat;
    }
```
20) https://leetcode.com/problems/as-far-from-land-as-possible/

```
    public int maxDistance(int[][] mat) {
         Queue<int[]> queue = new LinkedList<>();
        for(int i = 0; i < mat.length; i++) {
            for(int j = 0; j < mat[0].length; j++) {
                if(mat[i][j] == 1) {
                    queue.add(new int[]{i, j});
                } else {
                    mat[i][j] = -1;
                }
            }
        }
        
        int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        
        int count = 0;
        int max = -1;
        while(!queue.isEmpty()) {
            int size = queue.size();
            count++;
            for(int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                
                for(int[] dir : dirs) {
                    int nRow = cur[0] + dir[0];
                    int nCol = cur[1] + dir[1];
                    if(nRow >= 0 && nRow < mat.length &&
                      nCol >= 0 && nCol < mat[0].length) {
                        if(mat[nRow][nCol] == -1) {
                            mat[nRow][nCol] = count;
                            queue.add(new int[]{nRow, nCol});
                            max  = Math.max(max, count);
                        }
                    }
                }
            }
        }
        return max;
    }
```

21) https://leetcode.com/problems/rotting-oranges/
```
        Queue<int[]> queue = new LinkedList<>();
        int freshOranges = 0;
        for(int i = 0; i < mat.length; i++) {
            for(int j = 0; j < mat[0].length; j++) {
                if(mat[i][j] == 2) {
                    queue.add(new int[]{i, j});
                } else if(mat[i][j] == 1) {
                    freshOranges++;
                }
            }
        }
        
        if(freshOranges == 0) return 0;
        
        int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        
        int count = -1;
        while(!queue.isEmpty()) {
            int size = queue.size();
            count++;
            for(int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                
                for(int[] dir : dirs) {
                    int nRow = cur[0] + dir[0];
                    int nCol = cur[1] + dir[1];
                    
                    if(nRow < 0 || nCol < 0 || nRow >= mat.length || nCol >= mat[0].length) continue;
                    if(mat[nRow][nCol] == 0 || mat[nRow][nCol] == 2) continue;
                    
                    mat[nRow][nCol] = 2;
                    
                    queue.add(new int[]{nRow, nCol});
                    
                    freshOranges--;
    
                }
            }
        }
        return freshOranges == 0 ? count : -1 ;
```
22) https://leetcode.com/problems/shortest-path-in-binary-matrix/
```
    public int shortestPathBinaryMatrix(int[][] mat) {
        if (mat[0][0] != 0 || mat[mat.length - 1][mat[0].length - 1] != 0) {
            return -1;
        }
        
        Queue<int[]> queue = new ArrayDeque<>();
        mat[0][0] = 1;
        queue.add(new int[]{0, 0});
        
        // all 8 directons.
        int[][] dirs = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        
        while(!queue.isEmpty()) {
            
            int[] cur = queue.poll();
            int row = cur[0];
            int col = cur[1];
            int count = mat[row][col];

            if(row == mat.length - 1 && col == mat[0].length - 1) return count;

            for(int[] dir : dirs) {
                int nRow = row + dir[0];
                int nCol = col + dir[1];
                if(nRow < 0 || nCol < 0 || nRow >= mat.length || nCol >= mat[0].length) continue;
                if(mat[nRow][nCol] != 0) continue;

                queue.add(new int[]{nRow, nCol});
                mat[nRow][nCol] = count + 1;
            }
        }
        return -1;
    }
```
### Graph coloring/Bipartition:
23) https://leetcode.com/problems/possible-bipartition/

```
    public boolean possibleBipartition(int n, int[][] dislikes) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for(int i = 0; i <= n; i++) graph.putIfAbsent(i, new HashSet<>());
        for(int[] dLike : dislikes) {
            graph.get(dLike[0]).add(dLike[1]);
            graph.get(dLike[1]).add(dLike[0]);
        }
        
        int[] colors = new int[n + 1];
        
        for(int i = 1;  i <= n  ; i++) {
            if(colors[i] == 0) {
                colors[i] = 1;
                
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                
                while(!queue.isEmpty()) {
                    int cur = queue.poll();
                    for(int edge : graph.get(cur)) {
                        if(colors[edge] == 0) {
                            colors[edge] =  colors[cur] == 1 ? 2 : 1;
                            queue.add(edge);
                        } else {
                           if(colors[edge] == colors[cur]) return false;
                        }
                    }
                }
            }
        }
        return true;
    }
```
24) https://leetcode.com/problems/is-graph-bipartite/
```
    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] colors = new int[n];
        
        for(int i = 0;  i < n  ; i++) {
            if(colors[i] == 0) {
                colors[i] = 1;
                
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                
                while(!queue.isEmpty()) {
                    int cur = queue.poll();
                    for(int edge : graph[cur]) {
                        if(colors[edge] == 0) {
                            colors[edge] =  colors[cur] == 1 ? 2 : 1;
                            queue.add(edge);
                        } else {
                           if(colors[edge] == colors[cur]) return false;
                        }
                    }
                }
            }
        }
        return true;
    }
```
### Topological Sorting
25) https://leetcode.com/problems/course-schedule/

```
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int[] indegree = new int[numCourses];
        for(int[] pre : prerequisites) {
            indegree[pre[0]]++;
        }
        
        Set<Integer> zeroDegree = new HashSet<>();
        for( int i = 0; i < numCourses; i++) {
            if(indegree[i] == 0) {
                zeroDegree.add(i);
            }
        }
        
        while(!zeroDegree.isEmpty()) {
            Iterator<Integer> it = zeroDegree.iterator();
            
            int course = it.next();
            zeroDegree.remove(course);
            
            for(int[] pre : prerequisites) {
                if(course == pre[1]) {
                    indegree[pre[0]]--;
                    if(indegree[pre[0]] == 0) {
                        zeroDegree.add(pre[0]);
                    }
                }
            }
        }
        
        for(int n : indegree) {
            if(n > 0) {
                return false;
            }
        }
        return true;
    }
```
26) https://leetcode.com/problems/course-schedule-ii/
```
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] inDegree = new int[numCourses];
        for(int[] pre : prerequisites) {
            inDegree[pre[0]]++;
        }
        
        Set<Integer> zeroDegree = new HashSet<>();
        for(int i = 0; i< numCourses; i++) {
            if(inDegree[i] == 0) {
                zeroDegree.add(i);
            }
        }
        
        if(zeroDegree.isEmpty()) {
            return new int[0];
        }
        
        int[] res = new int[numCourses];
        int i = 0;
        while(!zeroDegree.isEmpty()) {
            Iterator<Integer> iterator = zeroDegree.iterator();
            int course = iterator.next();
            zeroDegree.remove(course);
            res[i++] = course;
            for(int[] pre : prerequisites) {
                if(course == pre[1]) {
                    inDegree[pre[0]]--;
                    if(inDegree[pre[0]] == 0) {
                        zeroDegree.add(pre[0]);
                    }
                }
            }
        }
        
        for(int pre : inDegree) {
            if(pre != 0) 
                return new int[0];
        }
        return res;
    }
```
### Dijistra 
22) https://leetcode.com/problems/network-delay-time/
```
 Dijistra
 Time: O(Nlog(N) + E), Space complexity: O(N + E)
 public int networkDelayTime(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for(int[] time: times) {
            if(!graph.containsKey(time[0])) {
                graph.put(time[0], new ArrayList<>());
            }
            graph.get(time[0]).add(new int[]{time[1], time[2]});
        }
        
        
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        queue.add(new int[]{0, k});
        
        Map<Integer, Integer> distMap = new HashMap<>();
        
        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            int dist = cur[0];
            int src = cur[1];
            
            if(distMap.containsKey(src)) continue;
                
            distMap.put(src, dist);
            
            if(graph.containsKey(src)) {
                for(int[] dest : graph.get(src)) {
                    if(!distMap.containsKey(dest[0])) {
                        queue.add(new int[]{dist + dest[1], dest[0]});
                    }
                }
            }
        }
        
        if(distMap.size() != n) return -1;
        int ans = 0;
        for(int num : distMap.values()) {
            ans = Math.max(ans, num);
        }
        return ans;
    }
    
    Bellman-Ford algorithm Time complexity: O(N*E), Space complexity: O(N)
    public int networkDelayTime(int[][] times, int n, int k) {
        double[] distTo = new double[n];
        Arrays.fill(distTo, Double.MAX_VALUE);
        distTo[k - 1] = 0;
        
        for(int i = 1; i < n ; i++) {
            for(int[] edge : times) {
                int src = edge[0] - 1, dest = edge[1] - 1, distance = edge[2];
                distTo[dest] = Math.min(distTo[dest], distTo[src] + distance);
            }
        }
        
        double res = Double.MIN_VALUE;
        for(double dis: distTo) {
            res = Math.max(res, dis);
        }
        return res == Double.MAX_VALUE ? -1 : (int) res;
    }
    
```
23) https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/

```
public int findTheCity(int n, int[][] edges, int distanceThreshold) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for(int i = 0; i < n; i++) graph.put(i, new ArrayList<>());
    
        for(int[] edge : edges) {
          int src = edge[0];
          int dest = edge[1];
          int cost = edge[2];

          graph.get(src).add(new int[]{dest, cost});
          graph.get(dest).add(new int[]{src, cost});
        }
        
        int resultSize = Integer.MAX_VALUE;
        int resultCity = 0;
        for(int i = 0; i < n; i++) {
            int size = dijistra(graph, i, n, distanceThreshold);
            if(size <= resultSize) {
                resultSize = size;
                resultCity = i;
            }
        }
        
        return resultCity;
    }
    
    public int dijistra(Map<Integer, List<int[]>> graph, int start, int n, int distanceThreshold) {
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> a[1]-b[1]);
        queue.add(new int[]{start, 0});
        
        int[] distanceTo = new int[n];
        Arrays.fill(distanceTo, Integer.MAX_VALUE);
        distanceTo[start] = 0;
        
        
        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            int src = cur[0];
            
            for(int[] edge : graph.get(src)) {
                
                int dest = edge[0], distance = edge[1];
                int totalCost = distanceTo[src] + distance;
                
                if(distanceTo[dest] > totalCost) {
                    distanceTo[dest] = totalCost;
                    queue.add(new int[]{dest, totalCost});
                }
            }
        }
        
        int size = 0;
        for(int i = 0; i < n; i++) {
            if(distanceTo[i] <= distanceThreshold) size++;
        }
        
        return size;
    }
```
24) https://leetcode.com/problems/cheapest-flights-within-k-stops/

```
 public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        
        int[][] graph = new int[n][n];
        for(int[] f : flights) {
            graph[f[0]][f[1]] = f[2];
        }
        
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> a[0] - b[0]);        
        queue.add(new int[]{0, src, 0});
        int[] distances = new int[n];
        int[] currentStops = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(currentStops, Integer.MAX_VALUE);
        
        
        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            
            int price  = cur[0];
            int place = cur[1];
            int stops = cur[2];
            
            if(place == dst) return price;
            if(stops == k + 1) continue;
            
            for(int i = 0; i < n; i++) {
                if(graph[place][i] > 0) {
                    int totalDistance = price + graph[place][i];
                    if(totalDistance < distances[i] ) {
                        queue.add(new int[]{totalDistance, i, stops + 1});
                        distances[i] = totalDistance;
                    } else if(stops < currentStops[i]){
                        queue.add(new int[]{totalDistance, i, stops + 1});
                    }
                    currentStops[i] = stops;       
                }
            }
        }
        return -1;
    }
```





 
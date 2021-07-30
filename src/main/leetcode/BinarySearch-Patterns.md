### Binary Search patterns:

#### Two Halves
  1) https://leetcode.com/problems/find-the-duplicate-number/
  2) https://leetcode.com/problems/kth-smallest-element-in-a-bst/ 

#### Search:
  3) https://leetcode.com/problems/find-peak-element/
  4) https://leetcode.com/problems/guess-number-higher-or-lower/
  5) https://leetcode.com/problems/h-index/
  6) https://leetcode.com/problems/h-index-ii/
  7) https://leetcode.com/problems/search-a-2d-matrix/
  8) https://leetcode.com/problems/search-a-2d-matrix-ii/ 
  9) https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
 
####  Search in Rotated Sorted Array:
  10) https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
  11) https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/
  12) https://leetcode.com/problems/median-of-two-sorted-arrays/
  13) https://leetcode.com/problems/search-in-rotated-sorted-array/
  14) https://leetcode.com/problems/search-in-rotated-sorted-array-ii/

#### Range questions:
  15) https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
  16) https://leetcode.com/problems/subarray-sum-equals-k/
  
#### less than 2:
  17) 
#### greater than 2: 
  18) https://leetcode.com/problems/count-complete-tree-nodes/
  19) https://leetcode.com/problems/first-bad-version/
  20) https://leetcode.com/problems/minimum-size-subarray-sum/
  21) https://leetcode.com/problems/search-insert-position/
  22) https://leetcode.com/problems/median-of-two-sorted-arrays/
  
####  
  23) https://leetcode.com/discuss/interview-question/351782/Google-or-Phone-Screen-or-Kth-Largest-Element-of-Two-Sorted-Arrays
  24) https://leetcode.com/problems/count-of-smaller-numbers-after-self/ 

### Two Halves:
https://leetcode.com/problems/find-the-duplicate-number/
```
    public int findDuplicate(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while(left < right) {
            int mid = left + (right - left) / 2;
            int count = 0;
            for(int num : nums) {
                if(num <= mid) {
                  count++;  
                }
            }
            
            if(count > mid) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
```
https://leetcode.com/problems/kth-smallest-element-in-a-bst/
```
    public int kthSmallest(TreeNode root, int k) {
        int n = getCount(root.left);
        
        if(n + 1 == k) return root.val;
        
        if(n + 1 < k) {
            return kthSmallest(root.right, k - n - 1);
        } else {
            return kthSmallest(root.left, k);
        }
    }
    
    public int getCount(TreeNode root) {
        if(root == null) return 0;
        return getCount(root.left) + getCount(root.right) + 1;
    }
```

### search
https://leetcode.com/problems/find-peak-element/

```
    public int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while(left < right) {
            int mid = left + (right - left) / 2;
            if((mid < right && nums[mid] > nums[mid + 1]) && ( mid > 0 && nums[mid] > nums[mid - 1])) {
                return mid;
            } else if(nums[mid] > nums[mid + 1]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
```
https://leetcode.com/problems/guess-number-higher-or-lower/
```
    public int guessNumber(int n) {
        int left = 0;
        int right = n;
        
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(guess(mid) == 1) {
                left = mid + 1;
            } else if(guess(mid) == -1) {
                right = mid;
            } else {
                return mid;
            }
        }
        return left;
    }
```
https://leetcode.com/problems/h-index/ n log n 
https://leetcode.com/problems/h-index-ii/ log n 
```
    public int hIndex(int[] citations) {
        Arrays.sort(citations);
        // [0,1,3,6,5]
        int left = 0;
        int right = citations.length;
        int n = citations.length;
        
        while(left < right) {
            int mid = left + (right - left) / 2;
            
            if(citations[mid] == n - mid) {
                return n - mid;
            } else if(citations[mid] > n - mid) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return n - left;
    } 
```
https://leetcode.com/problems/search-a-2d-matrix/
```
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        if(m == 0) return false;
        int n = matrix[0].length;
        
        int left = 0, right = n * m - 1;
        int pivotIndex = 0, pivotElement = 0;
        
        while(left <= right) {
            pivotIndex = left + (right - left) / 2;
            pivotElement = matrix[pivotIndex / n][pivotIndex % n];
            if(pivotElement == target) {
                return true;
            } else {
                if(pivotElement > target) {
                        right = pivotIndex - 1;
                } else {
                    left = pivotIndex + 1;
                }
            }
        }
        return false;
    }
```
https://leetcode.com/problems/search-a-2d-matrix-ii/
```
    public boolean searchMatrix(int[][] matrix, int target) {
        if(matrix == null || matrix.length == 0) return false;
        
        int shortDim = Math.min(matrix.length, matrix[0].length);
        
        for(int i = 0; i < shortDim; i++) {
            boolean vertical = binarySearch(matrix, target, i, true);
            boolean horizantal = binarySearch(matrix, target, i, false);
            if(vertical || horizantal) return true;
        }
        return false;
    }
    public boolean binarySearch(int[][] matrix, int target, int start, boolean vertical) {
        int left = start;
        int right = vertical ? matrix[0].length - 1 : matrix.length - 1;
        
        while(left <= right) {
            int mid = (left + right) / 2;
            if(vertical) {
                if(matrix[start][mid] < target) {
                    left = mid + 1;
                } else if(matrix[start][mid] > target) {
                    right = mid - 1;
                } else {
                    return true;
                }
                
            } else {
                if(matrix[mid][start] < target) {
                    left = mid + 1;
                } else if(matrix[mid][start] > target) {
                    right = mid - 1;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
```
https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
```
     public int[] twoSum(int[] numbers, int target) {
         int left = 0;
         int right = numbers.length - 1;
         while(left < right) {
             int sum = numbers[left] + numbers[right];
             
             if( sum == target) {
                 return new int[]{left + 1, right + 1};
             }
             
             if(sum < target) {
                 left++;
             } else if(sum > target) {
                 right--;
             }
         }
         return new int[0];
     }
```
### Search in Rotated Sorted Array:

https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/

```
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(nums[mid] < nums[right]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return nums[right];
    }
```
https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/

```
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(nums[mid] < nums[right]) {
                right = mid;
            } else if(nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right--;
            }
        }
        return nums[right];
    }
```
https://leetcode.com/problems/median-of-two-sorted-arrays/
```
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = 0, n2 = 0, mid = (nums1.length + nums2.length) / 2;
        
        int cur = 0, prev = 0;
        for(int i = 0; i <= mid; i++) {
            prev = cur;
            if(n1 == nums1.length){
                cur = nums2[n2++];
            } else if(n2 == nums2.length) {
                cur = nums1[n1++];
            } else if(nums1[n1] <= nums2[n2]) {
                cur = nums1[n1++];
            } else {
                cur = nums2[n2++];
            }
        }
        if((nums1.length + nums2.length) % 2 == 1) {
            return cur / 1.0;
        }
        return (prev + cur) / 2.0;
    }
```
https://leetcode.com/problems/search-in-rotated-sorted-array/

```
public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while(left <= right) {
            int mid = left + (right - left) / 2;
            
            if(nums[mid] == target) {
                return mid;
            } else if(nums[left] <= nums[mid]) {
                if(target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if(nums[right] >= target && nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }
```
https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
```
    public boolean search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while(left <= right) {
            int mid = left + (right - left) / 2;
            
            if(nums[mid] == target) {
                return true;
            } 
            
            //"duplicate", jump
            if(nums[mid] == nums[left]){
                left++;
            } else if(nums[left] <= nums[mid]) {
                if(target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if(nums[right] >= target && nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return false;
    }
```
### range questions: 
https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
```
public int[] searchRange(int[] nums, int target) {
        int[] res = new int[]{-1, -1};
        int firstOccurance = findBound(nums, target, true);
        if(firstOccurance == -1) {
            return new int[]{-1, -1};
        }
        
        int lastOccurance = findBound(nums, target, false);
        
        return new int[]{firstOccurance, lastOccurance};
    }
    
    public int findBound(int[] nums, int target, boolean isFirst) {
        int left = 0, right = nums.length - 1;
        while(left <= right) {
            int mid = left + (right - left) / 2;
            
            if(nums[mid] == target) {
               if(isFirst) {
                   if(mid == left || nums[mid - 1] != target) {
                       return mid;
                   }
                   right = mid -1;
               } else {
                   
                   if(mid == right || nums[mid + 1] != target) {
                       return mid;
                   }
                   
                   left = mid + 1;
               }
                
            } else if(nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
```
https://leetcode.com/problems/subarray-sum-equals-k/
```
  public int subarraySum(int[] nums, int k) {
        if(nums.length == 0) return 0;
        
        Map<Integer, Integer> dict = new HashMap<>();
        dict.put(0, 1);
        int count = 0;
        int sum = 0;
        for(int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if(dict.containsKey(sum - k)) {
                count += dict.get(sum - k);
            }
            dict.put(sum, dict.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
```
### less than 2
```
    public int mySqrt(int x) {
        if(x < 1) return 0;
        
        int left = 1;
        int right = x;
        
        while(left <= right) {
            int mid = left + (right - left) / 2;
            if(x / mid == mid) return mid;
            if(x / mid > mid) {
                left = mid + 1;
            }
            if(x / mid < mid) {
                right = mid - 1;
            }
        }
        return right;
    }
```
### greater than 2:
https://leetcode.com/problems/count-complete-tree-nodes/
```
public int countDepth(TreeNode root) {
        int count = 0;
        while(root.left != null) {
            root = root.left;
            count++;
        }
        return count;
    }
    
    public boolean exists(int index, int depth, TreeNode root) {
        
        int pivot = 0;
        int left = 0, right = (int)Math.pow(2, depth) - 1;
        
        for(int i = 0; i < depth; i++) {
            pivot = left + (right - left) / 2;
            
            if(index <= pivot) {
                root = root.left;
                right = pivot;
            } else {
                root = root.right;
                left = pivot + 1;
            }
        }
        return root != null;
    }
    
    public int countNodes(TreeNode root) {
        if(root == null) return 0;
        
        int depth = countDepth(root);
        if(depth == 0) return 1;
        
        
        int left = 1, right = (int)Math.pow(2, depth) - 1;
        
        while(left <= right) {
            int mid = left + (right - left) / 2;
            if(exists(mid, depth, root)) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return (int)Math.pow(2, depth) - 1 + left;
    }
```
https://leetcode.com/problems/first-bad-version/
```
    public int firstBadVersion(int n) {
        int left = 1;
        int right = n;
        
        while(left < right) {
            int mid = left + (right - left) / 2;
            
            if(isBadVersion(mid)){
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }
```
https://leetcode.com/problems/minimum-size-subarray-sum/
```
    public int minSubArrayLen(int target, int[] nums) {
        int sum = 0;
        int left = 0;
        int right = 0;
        int min = Integer.MAX_VALUE;
        for(right = 0; right < nums.length; right++) {
            sum += nums[right];
            
            while(sum >= target) {
                min = Math.min(min, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        return min == Integer.MAX_VALUE ? 0: min;
    }
```
https://leetcode.com/problems/search-insert-position/
```
   public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        
        while(left < right) {
            int mid = left +(right - left) / 2;
            
            if(nums[mid] >= target) {
                right = mid;
            } else {
                left =  mid + 1;
            }
        }
        return right;
    }
```
https://leetcode.com/problems/median-of-two-sorted-arrays/

```
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = 0, n2 = 0, mid = (nums1.length + nums2.length) / 2;
        
        int cur = 0, prev = 0;
        for(int i = 0; i <= mid; i++) {
            prev = cur;
            if(n1 == nums1.length){
                cur = nums2[n2++];
            } else if(n2 == nums2.length) {
                cur = nums1[n1++];
            } else if(nums1[n1] <= nums2[n2]) {
                cur = nums1[n1++];
            } else {
                cur = nums2[n2++];
            }
        }
        if((nums1.length + nums2.length) % 2 == 1) {
            return cur / 1.0;
        }
        return (prev + cur) / 2.0;
    }
```
kth Smallest in two sorted arrays:

```
private E kthSmallestFast(int k, int[] A1, int[] A2) {
    // System.out.println("this is an O(log k) speed algorithm with meaningful variables name");
    int size1 = A1.length, size2 = A2.length;

    int index1 = 0, index2 = 0, step = 0;
    while (index1 + index2 < k - 1) {
        step = (k - index1 - index2) / 2;
        int step1 = index1 + step;
        int step2 = index2 + step;
        if (size1 > step1 - 1
                && (size2 <= step2 - 1 || A1[step1 - 1].compareTo(A2[step2 - 1]) < 0)) {
            index1 = step1; // commit to element at index = step1 - 1
        } else {
            index2 = step2;
        }
    }
    // the base case of (index1 + index2 == k - 1)
    if (size1 > index1 && (size2 <= index2 || A1[index1].compareTo(A2[index2]) < 0)) {
        return A1[index1];
    } else {
        return A2[index2];
    }
}
```
https://leetcode.com/problems/count-of-smaller-numbers-after-self/
```
public List<Integer> countSmaller(int[] nums) {
        
        int n = nums.length;
        int[] result = new int[n];
        int[] indices = new int[n];
        for(int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        mergeSort(indices, 0, n, result, nums);
        List<Integer>  res = new ArrayList<>();
        for(int i : result) {
            res.add(i);
        }
        
        return res;
    }
    
    public void mergeSort(int[] indices, int left, int right, int[] result, int[] nums) {
        if(right - left <= 1) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSort(indices, left, mid, result, nums);
        mergeSort(indices, mid, right, result, nums);
        merge(indices, left, right, mid, result, nums);
    }
    
    public void merge(int[] indices, int left, int right, int mid, int[] result, int[] nums) {
        int i = left;
        int j = mid;
        List<Integer> tmp = new ArrayList<>();
        while(i < mid && j < right) {
            if(nums[indices[i]] <= nums[indices[j]]) {
                result[indices[i]] += j - mid;
                tmp.add(indices[i]);
                i++;
            } else {
                tmp.add(indices[j]);
                j++;
            }
        }
        
        while(i < mid) {
            result[indices[i]] += j - mid;
            tmp.add(indices[i]);
            i++;
        }
        
        while(j < right) {
            tmp.add(indices[j]);
            j++;
        }
        
        for(int k = left; k < right; k++) {
           indices[k] = tmp.get(k - left);
        }
    }
```
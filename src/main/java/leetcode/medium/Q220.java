package leetcode.medium;

import leetcode.easy.Q219;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains solution to <a href = "https://leetcode.com/problems/contains-duplicate-iii/">LeetCode Q220</>
 *
 * Thinking process:
 *
 * As a followup question, it naturally also requires maintaining a window of size k. When t == 0, it reduces to the
 * previous question so we just reuse the solution.
 *
 * Since there is now a constraint on the range of the values of the elements to be considered duplicates, it reminds us
 * of doing a range check which is implemented in tree data structure and would take O(LogN) if a balanced tree
 * structure is used, or doing a bucket check which is constant time. We shall just discuss the idea using bucket here.
 *
 * Bucketing means we map a range of values to the a bucket. For example, if the bucket size is 3, we consider 0, 1, 2
 * all map to the same bucket. However, if t == 3, (0, 3) is a considered duplicates but does not map to the same
 * bucket. This is fine since we are checking the bucket immediately before and after as well. So, as a rule of thumb,
 * just make sure the size of the bucket is reasonable such that elements having the same bucket is immediately
 * considered duplicates or duplicates must lie within adjacent buckets. So this actually gives us a range of possible
 * bucket size, i.e. t and t + 1. We just choose it to be t and a bucket mapping to be num / t.
 *
 * Another complication is that negative ints are allowed. A simple num / t just shrinks everything towards 0.
 * Therefore, we can just reposition every element to start from Integer.MIN_VALUE.
 */
public class Q220 {
    public static void main(String[] args) {
        Q220 q220 = new Q220();
        System.out.println(q220.containsNearbyAlmostDuplicate(new int[]{-3, 3}, 2, 4));
        System.out.println(q220.containsNearbyAlmostDuplicate(new int[]{-1, 2147483647}, 1, 2147483647));
        System.out.println(q220.containsNearbyAlmostDuplicate2(new int[]{-1, 2147483647}, 1, 2147483647));
    }

    /*
    Used bucket size of t + 1, code is simpler

    Time complexity: O(N)
    Space complexity: O(k)
     */
    public boolean containsNearbyAlmostDuplicate2(int[] nums, int k, int t) {
        if (k < 1 || t < 0) return false;
        Map<Long, Long> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            long remappedNum = (long) nums[i] - Integer.MIN_VALUE;
            long bucket = remappedNum / ((long) t + 1);
            if (map.containsKey(bucket)
                    || (map.containsKey(bucket - 1) && remappedNum - map.get(bucket - 1) <= t)
                    || (map.containsKey(bucket + 1) && map.get(bucket + 1) - remappedNum <= t))
                return true;
            if (map.entrySet().size() >= k) {
                long lastBucket = ((long) nums[i - k] - Integer.MIN_VALUE) / ((long) t + 1);
                map.remove(lastBucket);
            }
            map.put(bucket, remappedNum);
        }
        return false;
    }

    /*
    Used bucket size of t

    Time complexity: O(N)
    Space complexity: O(k)
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        if (k < 1 || t < 0) return false;
        Map<Long, Long> map = new HashMap<>();
        if (t == 0) return new Q219().containsNearbyDuplicate(nums, k);
        for (int i = 0; i < nums.length; i++) {
            long remappedNum = (long) nums[i] - Integer.MIN_VALUE;
            long bucket = remappedNum / t;
            if (map.containsKey(bucket)
                    || (map.containsKey(bucket - 1) && remappedNum - map.get(bucket - 1) <= t)
                    || (map.containsKey(bucket + 1) && map.get(bucket + 1) - remappedNum <= t))
                return true;
            if (map.entrySet().size() >= k) {
                long lastBucket = ((long) nums[i - k] - Integer.MIN_VALUE) / t;
                map.remove(lastBucket);
            }
            map.put(bucket, remappedNum);
        }
        return false;
    }
}

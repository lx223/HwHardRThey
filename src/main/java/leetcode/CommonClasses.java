package leetcode;

/**
 * Created by Lan Xiao
 */
abstract public class CommonClasses {
    protected class ListNode {
        public ListNode next;
        public int val;

        public ListNode(int val) {
            this.val = val;
        }

    }

    protected class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}

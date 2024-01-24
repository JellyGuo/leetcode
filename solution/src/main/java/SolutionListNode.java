import java.util.*;

public class SolutionListNode {
    //region----------------------------------------------链表-----------------------------------------------
    // 2 两数相加
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode();
        ListNode cur = dummy;
        int r = 0;
        while (l1 != null || l2 != null || r > 0) {
            int sum = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + r;
            cur.next = new ListNode(sum % 10);
            r = sum / 10;
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
            cur = cur.next;
        }
        return dummy.next;
    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        while (l1 != null) {
            stack1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            stack2.push(l2.val);
            l2 = l2.next;
        }
        ListNode dummy = new ListNode(0);
        int carry = 0;
        while (!stack1.isEmpty() || !stack2.isEmpty() || carry != 0) {
            int a = stack1.isEmpty() ? 0 : stack1.pop();
            int b = stack2.isEmpty() ? 0 : stack2.pop();
            int sum = a + b + carry;
            carry = sum / 10;
            sum = sum % 10;
            ListNode curr = new ListNode(sum);
            curr.next = dummy.next;
            dummy.next = curr;
        }
        return dummy.next;
    }

    //23 合并K个有序链表
    public ListNode mergeKLists(ListNode[] lists) {
        return merge(lists, 0, lists.length - 1);
    }

    private ListNode merge(ListNode[] lists, int l, int r) {
        if (l == r) {
            return lists[l];
        }
        if (l > r) {
            return null;
        }
        int mid = (l + r) >> 1;
        return mergeTwoLists(merge(lists, l, mid), merge(lists, mid + 1, r));
    }

    private ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null || list2 == null) {
            return list1 == null ? list2 : list1;
        }
        ListNode head = new ListNode(0);
        ListNode tail = head;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                tail.next = list1;
                list1 = list1.next;
            } else if (list1.val > list2.val) {
                tail.next = list2;
                list2 = list2.next;
            }
            tail = tail.next;
        }
        tail.next = list1 == null ? list2 : list1;
        return head.next;
    }

    //1669. 合并两个链表
    public ListNode mergeInBetween(ListNode list1, int a, int b, ListNode list2) {
        ListNode dummy = new ListNode();
        dummy.next = list1;
        ListNode first = dummy;
        ListNode second = list1;
        while (a-- > 0) {
            first = first.next;
        }
        while (b-- > 0) {
            second = second.next;
        }
        ListNode tail = list2;
        while (tail.next != null) {
            tail = tail.next;
        }
        first.next = list2;
        tail.next = second.next;
        return dummy.next;
    }

    // 25 K个一组反转链表
    public ListNode reverseKGroup2(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = head;
        for (int i = 0; i < k; i++) {
            //剩余数量小于k的话，则不需要反转。
            if (tail == null) {
                return head;
            }
            tail = tail.next;
        }
        // 反转前 k 个元素
        ListNode newHead = reverse(head, tail);
        //下一轮的开始的地方就是tail
        head.next = reverseKGroup2(tail, k);

        return newHead;
    }

    /*
    左闭又开区间
     */
    private ListNode reverse(ListNode head, ListNode tail) {
        ListNode pre = null;
        ListNode next;
        while (head != tail) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;

    }

    // 203移除链表元素
    // 删除链表
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode prev = dummy;

        while (prev.next != null) {
            if (prev.next.val == val) {
                prev.next = prev.next.next;
            } else {
                prev = prev.next;
            }
        }
        return dummy.next;
    }

    // 61 旋转链表
    //输入：head = [1,2,3,4,5], k = 2
//输出：[4,5,1,2,3]
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        ListNode tmp = head;
        int n = 1;
        while (tmp.next != null) {
            n++;
            tmp = tmp.next;
        }
        tmp.next = head;
        int position = (n - 1) - k % n;
        ListNode newTail = head;
        if (position == n) {
            return head;
        }
        while (position-- > 0) {
            newTail = newTail.next;
        }
        ListNode newHead = newTail.next;
        newTail.next = null;
        return newHead;
    }

    // 92 反转链表
    //输入：head = [1,2,3,4,5], left = 2, right = 4
//输出：[1,4,3,2,5]
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode prev = dummy;
        for (int i = 0; i < left - 1; i++) {
            prev = prev.next;
        }
        ListNode cur = prev.next;
        ListNode next;
        // prev->cur->next ->then
        // prev->next->cur ->then
        for (int i = left; i < right; i++) {
            next = cur.next;
            cur.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        return dummy.next;
    }

    // 86 分隔链表
    //给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。
// 你应当 保留 两个分区中每个节点的初始相对位置。
    //输入：head = [1,4,3,2,5,2], x = 3
//输出：[1,2,2,4,3,5]
    public ListNode partition(ListNode head, int x) {
        ListNode dummy1 = new ListNode(0);
        ListNode first = dummy1;
        ListNode dummy2 = new ListNode(0);
        ListNode second = dummy2;
        while (head != null) {
            if (head.val < x) {
                first.next = head;
                first = first.next;
            } else {
                second.next = head;
                second = second.next;
            }
            head = head.next;
        }
        second.next = null;
        first.next = dummy2.next;
        return dummy1.next;
    }

    // 141 环形链表
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    // 142 环形链表2
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        ListNode dummy = head;
        HashSet<ListNode> set = new HashSet<>();
        set.add(head);
        while (dummy.next != null) {
            if (set.contains(dummy.next)) {
                break;
            }
            set.add(dummy);
            dummy = dummy.next;
        }
        return dummy.next;
    }

    //双指针flyod 判圈算法
    // f = 2s
    // f = s+nb => s = nb
    public ListNode detectCycle2(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) break;
        }
        if (fast == null || fast.next == null) return null;
        fast = head;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    //相交链表
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode p = headA;
        ListNode q = headB;
        while (p != q) {
            p = p != null ? p.next : headB;
            q = q != null ? q.next : headA;
        }
        return p;
    }

    // 9 回文数
    public boolean isPalindrome(int x) {
        if (x < 0) return false;
        int cur = 0;
        int tmp = x;
        while (tmp != 0) {
            cur = cur * 10 + tmp % 10;
            tmp /= 10;
        }
        return cur == x;
    }

    // 234 回文链表
    public boolean isPalindrome(ListNode head) {
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        return isPalindrome(list, 0, list.size() - 1);
    }

    public boolean isPalindrome(List<ListNode> list, int left, int right) {
        if (left >= right) {
            return true;
        }
        if (list.get(left).val != list.get(right).val) {
            return false;
        }
        return isPalindrome(list, left + 1, right - 1);
    }

    public boolean isPalindrome2(ListNode head) {
        if (head == null || head.next == null) return true;
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode l1 = head;
        ListNode l2 = reverseList(slow.next);
        slow.next = null;

        while (l1 != null && l2 != null) {
            if (l1.val != l2.val) return false;
            l1 = l1.next;
            l2 = l2.next;
        }

        return true;
    }

    // 链表反转
    private ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        while (current != null) {
            ListNode nextTemp = current.next;
            current.next = prev;
            prev = current;
            current = nextTemp;
        }
        return prev;
    }

    // offer 从尾到头打印链表
    public int[] reversePrint(ListNode head) {
        List<Integer> list = new ArrayList<>();
        dfs(head, list);
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    private void dfs(ListNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        dfs(node.next, list);
        list.add(node.val);
    }

    // 143 重排链表
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        List<ListNode> list = new LinkedList<>();
        ListNode dumpy = head;
        while (dumpy != null) {
            list.add(dumpy);
            dumpy = dumpy.next;
        }
        int i = 0;
        int j = list.size() - 1;
        while (i < j) {
            list.get(i).next = list.get(j);
            i++;
            if (i == j) {
                break;
            }
            list.get(j).next = list.get(i);
            j--;
        }
        list.get(i).next = null;
    }

    public void reorderList2(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return;
        }
        int length = 0;
        ListNode dummy = head;
        while (dummy != null) {
            length++;
            dummy = dummy.next;
        }
        recurseList(head, length);
    }

    private ListNode recurseList(ListNode head, int length) {
        if (length == 1) {
            ListNode out = head.next;
            head.next = null;
            return out;
        }
        if (length == 2) {
            ListNode out = head.next.next;
            head.next.next = null;
            return out;
        }
        ListNode tail = recurseList(head.next, length - 2);
        ListNode out = tail.next;
        tail.next = head.next;
        head.next = tail;
        return out;
    }

    public void reorderList3(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return;
        }
        ListNode middle = middleNode2(head);
        ListNode l2 = reverseListNode(middle.next);
        middle.next = null;
        while (l2 != null) {
            ListNode l1tmp = head.next;
            ListNode l2tmp = l2.next;
            head.next = l2;
            l2.next = l1tmp;
            l2 = l2tmp;
            head = l1tmp;
        }
    }

    private ListNode reverseListNode(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode tail = head;
        head = head.next;
        tail.next = null;
        while (head != null) {
            ListNode tmp = head.next;
            head.next = tail;
            tail = head;
            head = tmp;
        }
        return tail;
    }

    // 328 奇偶链表
    public ListNode oddEvenList(ListNode head) {
        if (head == null) return head;

        ListNode odd = head;
        ListNode even = head.next;

        ListNode dummy = new ListNode();
        dummy.next = even;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = dummy.next;
        return head;
    }

    public ListNode middleNode2(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    //链表插入排序
    public ListNode insertionSortList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode lastSorted = head;
        ListNode curr = head.next;
        while (curr != null) {
            if (curr.val >= lastSorted.val) {
                lastSorted = curr;
            } else {
                ListNode prev = dummy;
                while (prev.next.val <= curr.val) {
                    prev = prev.next;
                }
                lastSorted.next = curr.next;
                curr.next = prev.next;
                prev.next = curr;
            }

            curr = lastSorted.next;
        }
        return dummy.next;
    }

    //mergesort 链表归并排序
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode tmp = slow.next;
        slow.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(tmp);
        ListNode dummy = new ListNode(0);
        ListNode res = dummy;
        while (left != null && right != null) {
            if (left.val <= right.val) {
                dummy.next = left;
                left = left.next;
            } else {
                dummy.next = right;
                right = right.next;
            }
            dummy = dummy.next;
        }
        dummy.next = left == null ? right : left;
        return res.next;
    }

    //自底向上merge sort
    public ListNode sortList2(ListNode head) {
        if (head == null) {
            return head;
        }
        int length = 0;
        ListNode node = new ListNode(0);
        node = head;
        while (node != null) {
            length++;
            node = node.next;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        for (int subLength = 1; subLength < length; subLength <<= 1) {
            ListNode prev = dummy;
            ListNode curr = dummy.next;
            while (curr != null) {
                ListNode head1 = curr;
                for (int i = 1; i < subLength && curr != null && curr.next != null; i++) {
                    curr = curr.next;
                }
                ListNode head2 = curr.next;
                curr.next = null;
                curr = head2;
                for (int i = 1; i < subLength && curr != null && curr.next != null; i++) {
                    curr = curr.next;
                }
                ListNode next = null;
                if (curr != null) {
                    next = curr.next;
                    curr.next = null;
                }
                ListNode merge = mergeTwoLists(head1, head2);
                prev.next = merge;
                while (prev.next != null) {
                    prev = prev.next;
                }
                curr = next;
            }
        }
        return dummy.next;
    }

    // 237 删除链表中的节点
    public void deleteNode(ListNode node) {
        ListNode prev = node;
        while (node.next != null) {
            node.val = node.next.val;
            prev = node;
            node = node.next;
        }
        prev.next = null;
//        while (node.next != null) {
//            node.val = node.next.val;
//            if (node.next.next == null) {
//                node.next = null;
//                break;
//            }
//            node = node.next;
//        }
    }

    public void deleteNode2(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

    // offer 18
    public ListNode deleteNode(ListNode head, int val) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode prev = dummy;
        while (prev != null && prev.next != null) {
            if (prev.next.val == val) {
                prev.next = prev.next.next;
            }
            prev = prev.next;
        }
        return dummy.next;
    }

    //offer 36 二叉搜索树与双向链表
    // 注意：本题与主站 426 题相同
    public Node treeToDoublyList(Node root) {
        if (root == null) return null;
        if (root.left == null && root.right == null) {
            root.right = root;
            root.left = root;
            return root;
        }
        List<Node> list = new ArrayList<>();
        dfs(root, list);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.get(i).left = list.get(list.size() - 1);
                list.get(i).right = list.get(i + 1);
            } else if (i == list.size() - 1) {
                list.get(i).left = list.get(i - 1);
                list.get(i).right = list.get(0);
            } else {
                list.get(i).right = list.get(i + 1);
                list.get(i).left = list.get(i - 1);
            }
        }
        return list.get(0);
    }

    private void dfs(Node node, List<Node> list) {
        if (node != null) {
            dfs(node.left, list);
            list.add(node);
            dfs(node.right, list);
        }
    }

    Node pre, head;

    public Node treeToDoublyList2(Node root) {
        if (root == null) return null;
        dfs(root);
        head.left = pre;
        pre.right = head;
        return head;
    }

    private void dfs(Node node) {
        if (node == null) return;
        dfs(node.left);
        if (pre != null) pre.right = node;
        else head = node;
        node.left = pre;
        pre = node;
        dfs(node.right);
    }

    // offer 22 链表中倒数第k个节点
    public ListNode getKthFromEnd(ListNode head, int k) {
        ListNode first = head;
        while (k-- > 0) {
            first = first.next;
        }
        ListNode second = head;
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        return second;
    }

    // 19 删除链表的倒数第N个节点
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode first = head;
        ListNode second = dummy;
        for (int i = 0; i < n && first != null; i++) {
            first = first.next;
        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }


    //输入：head = [1,2,3,4]
//输出：[2,1,4,3]
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = head.next;
        head.next = swapPairs(tail.next);
        tail.next = head;
        return tail;
    }

    //输入：head = [1,2,3,4]
//输出：[2,1,4,3]
    public ListNode swapPairs2(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode temp = dummy;
        while (temp.next != null && temp.next.next != null) {
            ListNode node1 = temp.next;
            ListNode node2 = temp.next.next;
            temp.next = node2;
            node1.next = node2.next;
            node2.next = node1;
            temp = node1;
        }
        return dummy.next;
    }

    //2807. 在链表中插入最大公约数
    public ListNode insertGreatestCommonDivisors(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode cur = head;
        while (cur.next != null) {
            ListNode next = cur.next;
            int gcd = gcd(cur.val, next.val);
            ListNode gcdNode = new ListNode(gcd);
            gcdNode.next = next;
            cur.next = gcdNode;
            cur = next;
        }
        return dummy.next;
    }

    private int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }

    //endregion------------------------------------------------------------------------------------------------

}

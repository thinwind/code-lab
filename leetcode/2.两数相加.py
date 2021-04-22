#
# @lc app=leetcode.cn id=2 lang=python3
#
# [2] 两数相加
#


# @lc code=start
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next
        
class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        root = ListNode()
        result = root
        item1 = l1.val
        item2 = l2.val
        cache = 0
        while item1 is not None and item2 is not None:
            result.val = (item1 + item2 + cache) % 10
            cache = (item1 + item2 + cache) // 10
            result.next = ListNode()
            result = result.next
            item1 = l1.next
            item2 = l2.next
        while item1 is not None:
            result.val = (item1 + cache) % 10
            cache = (item1 + cache) // 10
            result.next = ListNode()
            result = result.next
            item1 = l1.next
        while item2 is not None:
            result.val = (item2 + cache) % 10
            cache = (item2 + cache) // 10
            result.next = ListNode()
            result = result.next
            item2 = l2.next
        
        return root

# @lc code=end

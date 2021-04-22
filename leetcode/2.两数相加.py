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

class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        root = ListNode()
        root.next = ListNode()
        result = root
        item1 = l1
        item2 = l2
        cache = 0
        while item1 is not None and item2 is not None:
            result = result.next
            result.val = (item1.val + item2.val + cache) % 10
            cache = (item1.val + item2.val + cache) // 10
            result.next = ListNode()
            item1 = item1.next
            item2 = item2.next
        while item1 is not None:
            result = result.next
            result.val = (item1.val + cache) % 10
            cache = (item1.val + cache) // 10
            result.next = ListNode()
            item1 = item1.next
        while item2 is not None:
            result = result.next
            result.val = (item2.val + cache) % 10
            cache = (item2.val + cache) // 10
            result.next = ListNode()
            item2 = item2.next

        if cache > 0:
            result.next.val = cache
        else:
            result.next = None
        
        return root.next
# @lc code=end

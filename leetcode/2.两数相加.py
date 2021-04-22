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
    def addTwoNumbers(self, l1, l2):
        result = []
        cur = 0
        cache = 0
        for l, r in zip(l1, l2):
            result.append((l + r + cache) % 10)
            cache = (l + r + cache) // 10
        if len(result) < len(l1):
            for i in range(len(result),len(l1)):
                result.append((l1[i] + cache) % 10)
                cache = (l1[i] + cache) // 10
        elif len(result)<len(l2):
            for i in range(len(result),len(l2)):
                result.append((l2[i] + cache) % 10)
                cache = (l2[i] + cache) // 10
        if cache>0:
            result.append(cache)


# @lc code=end

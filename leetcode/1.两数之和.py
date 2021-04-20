#
# @lc app=leetcode.cn id=1 lang=python3
#
# [1] 两数之和
#

# @lc code=start
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        nums_with_idx = sorted(list(zip(range(0,len(nums)),nums)),key=lambda x:x[1])
        for i,n in zip(range(1,len(nums)+1),nums_with_idx):
            m = target -n[1]
            pos = self.binsearch(nums_with_idx,m,i)
            if pos>=0:
                return [n[0],pos]
        
    def binsearch(self,a,k,start):
        ## 对于已经排好序的序列
        alen = len(a)
        low = start
        hi = alen - 1
        while low <= hi:
            mid = (low + hi) // 2
            if a[mid][1] == k:
                return a[mid][0]
            elif a[mid][1] > k:
                hi = mid -1
            else:
                low = mid + 1
        return -1
# @lc code=end

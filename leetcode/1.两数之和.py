#
# @lc app=leetcode.cn id=1 lang=python3
#
# [1] 两数之和
#

# @lc code=start
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        check_table=dict()
        for i,num in enumerate(nums):
            if target - num in check_table:
                return[check_table[target-num],i]
            check_table[num] = i
        
        return []
            
# @lc code=end

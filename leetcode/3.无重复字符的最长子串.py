#
# @lc app=leetcode.cn id=3 lang=python3
#
# [3] 无重复字符的最长子串
#


# @lc code=start
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        if len(s) == 1:
            return 1
        if len(s) == 0:
            return 0
        max_len = 0

        for cur in range(0, len(s) - 1):
            for pos in range(cur + 1, len(s)):
                sub_str = s[cur:pos]
                if s[pos] in sub_str:
                    if pos - cur > max_len:
                        max_len = pos - cur
                    break
                if pos == len(s) - 1:
                    if pos + 1 - cur > max_len:
                        max_len = pos + 1 - cur
        if max_len == 0:
            return len(s)

        return max_len

# @lc code=end

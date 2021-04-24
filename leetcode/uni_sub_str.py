#
# [3] 无重复字符的最长子串
#
class Solution:
    def lengthOfLongestSubstring(self, s):
        s_len = len(s)
        if s_len == 0 : return 0
        if s_len == 1 : return 1
        slide_window = []
        max_len = 0
        for i in range(s_len):
            cur = s[i]
            if cur in slide_window:
                pos = slide_window.index(cur)
                slide_window = slide_window[pos+1::]
            slide_window.append(cur)
            max_len = max(max_len,len(slide_window))

        return max_len

if __name__ == "__main__":
    print(Solution().lengthOfLongestSubstring("dvdf"))
# -*- coding: utf-8 -*-
'''
@Time   :  2021-Mar-02 10:40
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        bst
'''

import random

class IntTree:
    def __init__(self, key = 0, value = None):
        self.key = key
        self.value = value
        self.prefix = key
        self.mask = 1
        self.left = self.right = None

    def isleaf(self):
        return self.left is None and self.right is None

    def replace(self, x, y):
        if self.left == x:
            self.left = y
        else:
            self.right = y

    def match(self, k):
        return maskbit(k, self.mask) == self.prefix

def maskbit(x, mask):
    return x & (~(mask - 1))

def zero(x, mask):
    return x & (mask >> 1) == 0

# The longest common prefix

def lcp(p1, p2):
    diff = p1 ^ p2
    mask = 1
    while diff != 0:
        diff >>= 1
        mask <<= 1
    return (maskbit(p1, mask), mask)

def branch(t1, t2):
    t = IntTree()
    (t.prefix, t.mask) = lcp(t1.prefix, t2.prefix)
    if zero(t1.prefix, t.mask):
        t.left, t.right = t1, t2
    else:
        t.left, t.right = t2, t1
    return t

def insert(t, key, value):
    if t is None:
        return IntTree(key, value)
    node = t
    parent = None
    while (not node.isleaf()) and node.match(key):
        parent = node
        if zero(key, node.mask):
            node = node.left
        else:
            node = node.right
    if node.isleaf() and key == node.key:
        node.value = value
    else:
        p = branch(node, IntTree(key, value))
        if parent is None:
            return p
        parent.replace(node, p)
    return t

def lookup(t, key):
    while t is not None and (not t.isleaf()) and t.match(key):
        if zero(key, t.mask):
            t = t.left
        else:
            t = t.right
    if t is not None and t.isleaf() and t.key == key:
        return t.value
    return None

def from_map(m):
    t = None
    for k, v in m.items():
        t = insert(t, k, v)
    return t

def test():
    def generate_map(size):
        m = {}
        xs = range(size);
        random.shuffle(xs)
        for i in xrange(size):
            m[i] = xs[i]
        return m
    sz = 100
    for i in xrange(sz):
        m = generate_map(random.randint(0, sz-1))
        t = from_map(m)
        for k, v in m.items():
            val = lookup(t, k)
            if v != val:
                print "lookup", k, "got", val, "!=", v, "t=\n", to_str(t)
                exit()
        for k in xrange(sz):
            if k not in m and lookup(t, k) is not None:
                print "lookup", i, "expected None, but get", lookup(t, i) ,to_str(t)
                exit()
    print "passed", sz, "cases"

if __name__ == "__main__":
    test()

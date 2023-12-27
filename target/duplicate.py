
from ast import arg
import os
import shlex
import subprocess
from sys import stderr, stdout
from sys import argv
import re
import sys
import time
import re
import csv
import codecs
import io
import base64
import hashlib
from collections import Counter
 
# MD5加密
def md5String(str):
    if (len(str) > 0):
        md5 = hashlib.md5()
        md5.update(str.encode(encoding="utf-8"))
        return md5.hexdigest()
    else:
        return "N/A"

def compareString(str1, str2):
    if(len(str1) != len(str2)):
        # print("len is different!")
        return False
    for i in range(len(str1)):
        if( str1[i] != str2[i] ):
            # print("str1[%c] str2[%c]" % (str1[i] , str2[i]))
            return False
    return True


if __name__=="__main__":
    if( len(argv) != 2):
        print("================================================================================================================\n")
        print("Usage: python3 duplicate.py filename\n")
        print("================================================================================================================\n")
        sys.exit()

    filename=argv[1]
    print(filename)
    
    file = open(filename, encoding="utf-8")
    lines = file.readlines()
    list_array_count = []

    
    # for i in range(len(lines)):
    #     lines[i] = hash(lines[i])


    # for line in lines:
    #     print(line)

    count = 0
    for i in range(len(lines)):
        # print("*", end='', flush= True)
        # print(lines[i])
        array_itmes = re.findall(r"id:", lines[i] )
        list_array_count.append(len(array_itmes))
        # print(len(array_itmes))
        for j in range(i + 1, len(lines)):

            if( compareString( lines[i], lines[j] ) == True ):    
                print("line# %d duplicated with line# %d" % (i+1, j+1))
                count = count + 1
                break
    
    print("[%d] times duplicated item found in total [%d] lines" % (count, len(lines)) )
    print('percent: {:.1f}%'.format(count/len(lines)*100))

    cl = Counter(list_array_count)
    for k, v in cl.items():
        if v > 1:
            print("Array Count{}, 重复{}次".format(k, v))
    
    

    

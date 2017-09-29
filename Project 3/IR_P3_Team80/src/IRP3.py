# -*- coding: utf-8 -*-
"""
Thanks to the author Ruhan Sa, who is the TA of IR project 3 in Fall 2015
"""

import json
import urllib.request
import os
os.chdir('output')
#outfn = 'path_to_your_file.txt'
#count = 1
models = ["BM25","DFR","VSM"]
for model in models:
    count = 1
    with open('queries.txt', encoding="utf-8") as f:
        for line in f:
            query = line.strip('\n').replace(':', '')
            query = urllib.parse.quote(query)
            inurl = 'http://localhost:8984/solr/demo_'+model+'/select?defType=customqp&fl=score,id&indent=on&q=' + query + '&rows=20&wt=json'
            
            qid = str(count).zfill(3)
            
            outf = open(model + '.txt', 'a+')
            data = urllib.request.urlopen(inurl).read()
            docs = json.loads(data.decode('utf-8'))['response']['docs']
            rank = 1

            for doc in docs:
                outf.write(str(qid) + ' ' + 'Q0' + ' ' + str(doc['id']) + ' ' + str(rank) + ' ' + str(doc['score']) + ' ' + model + '\n')
                rank += 1
            outf.close()
            count += 1

#os.system("")



import os
directory='output'
#os.chdir(directory)
models = ["BM25","VSM","DFR"]
for model in models:
    filename = model + ".txt"
    file=filename.split('.')
    print filename
    os.system('/trec_eval.9.0/trec_eval -c -M1000 qrel.txt'+' '+filename+ ' '+ '>'+' '+ file[0]+'_MAP.txt')

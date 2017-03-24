# -*- coding: utf-8 -*-
"""
Created on Thu Sep 15 16:53:25 2016

@author: arunchandrapendyala
"""

import json
import re
import dateutil.parser as parser
import string

input_file=open('ko_us_got.json', 'r')  #input file is processed for ingestion into Apache solr
output_file=open('final_ko_us_got.json', 'w')
json_decode=json.load(input_file)
result = []

smileys = """:-) :) :o) :] :3 :c) :> =] 8) =) :} :^) 
             :D 8-D 8D x-D xD X-D XD =-D =D =-3 =3 B^D""".split()
pattern = "|".join(map(re.escape, smileys))

i=0
uhashes =[]
hashes = []

emoticons_pattern = re.compile(u'('
    u'\ud83c[\udf00-\udfff]|'
    u'\ud83d[\udc00-\ude4f\ude80-\udeff]|'
    u'[\u2600-\u26FF\u2700-\u27BF])+',               #ucs 2 
    re.UNICODE)
    
def find_topic(stringg):    #this is to assign the category to each tweet
   if any (word in " #ios10 ,ios 10 , #apple , watch2 , #airpods , airpods , iphone7 , #iphone7 , iphone7 plus" for word in stringg):
       top = 'tech'
   elif any (word in " hillary clinton , trump , us elections , #makeamericagreatagain, democrats , republicans, #imwithher, hillaryhealth , #nevertrump" for word in stringg):
       top = 'politics'
   elif any (word in " syrian civil war , #syria , #aleppo, #syrianCeasefire, syria, #syriancivilwar , #syrianrefugees" for word in stringg):
       top = 'world news'
   elif any (word in " usopen, us open , #usopen , djokovic , wawrinka , pliskova , kerber , serena williams" for word in stringg):
       top = 'sports'
   elif  any (word in " #got , game of thrones , angela lansbury , #Gameofthrones , tyrion , ramsey bolton , jon snow , arya stark , #got7 " for word in stringg):
       top = 'entertainment'
   else : 
       top = 'entertainment'
   return top
                          
                          
def strip_links(text):  #strip urls from tweet text to get only text
    link_regex    = re.compile('((https?):((//)|(\\\\))+([\w\d:#@%/;$()~_?\+-=\\\.&](#!)?)*)', re.DOTALL)
    links         = re.findall(link_regex, text)
    for link in links:
        text = text.replace(link[0], ', ')    
    return text

def strip_all_entities(text): #special characters are also removed from tweet text
    entity_prefixes = ['@','#']
    for separator in  string.punctuation:
        if separator not in entity_prefixes :
            text = text.replace(separator,' ')
    words = []
    for word in text.split():
        word = word.strip()
        if word:
            if word[0] not in entity_prefixes:
                words.append(word)
    return ' '.join(words)

                          
                          
for item in json_decode: #new JSON file is created with modified fields
    my_dict={}
    stringg = item.get('text')
    my_dict['topic']=find_topic(stringg)
    text_em = item.get('text')
    text_enc = text_em.encode('Utf-8')
    print(text_enc)
    my_dict['text']=text_enc
    my_dict['tweet_lang']=item.get('lang')
    text_xx = strip_all_entities(strip_links(emoticons_pattern.sub(r'', stringg)))
    lang = item.get('lang')
    if lang == 'tr':
        my_dict['text_tr'] = text_xx
    elif lang == 'en':
        my_dict['text_en'] = text_xx
    elif lang == 'es':
        my_dict['text_es'] = text_xx
    elif lang == 'ko':
        my_dict['text_ko'] = text_xx
    hashtags =item.get('entities').get('hashtags')
    for hash in hashtags :
        hashes = []
        hashes.append(hash['text'])
    my_dict['hashtags'] = hashes
    my_dict['mentions']=item.get('entities').get('user-mentions')
    urls =item.get('entities').get('urls')
    for hash in urls :
        uhashes = []
        uhashes.append(hash['url'])
    my_dict['tweet_urls'] = uhashes 
    my_dict['tweet_loc']=item.get('coordinates')
    my_dict['tweet_emoticons']= re.findall(pattern,text_enc)
    s=re.findall(emoticons_pattern,text_enc)
    my_dict['tweet_emoticons'].append(s)
    date = (parser.parse(item.get('created_at')))
    my_dict['date']=date.isoformat()
    print my_dict
    i=i+1
    result.append(my_dict)
print i    
    
back_json=json.dumps(result, output_file)
output_file.write(back_json)
output_file.close()


# -*- coding: utf-8 -*-
"""
Created on Fri Sep  9 15:29:11 2016

@author: arunchandrapendyala
"""
from __future__ import absolute_import, print_function

from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream

#This code is for getting code using tweepy streaming API for selected topics

#https://github.com/tweepy/tweepy/tree/master/examples
#https://github.com/tweepy/tweepy/blob/master/examples/streaming.py

# Go to http://apps.twitter.com and create an app.
# The consumer key and secret will be generated for you after
consumer_key="xxxxxxxxxxx"
consumer_secret="xxxxxxxxxxx"

# After the step above, you will be redirected to your app's page.
# Create an access token under the the "Your access token" section
access_token="xxxxxxxx"
access_token_secret="xxxxxxxxx"


file=open('usopen.json', 'w')

class StdOutListener(StreamListener):
    """ A listener handles tweets that are received from the stream.
    This is a basic listener that just prints received tweets to stdout.
    """
    def on_data(self, data):
        #if (not data.retweeted) and ('RT @' not in data.text):
                        
            print(data)
            file.write(data+",")
        
            return True

    def on_error(self, status):
        print(status)

if __name__ == '__main__':
    l = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    stream = Stream(auth, l)
    stream.filter(track=['usopen', 'djokovic' , 'wawrinka'])  #insert topics here
    
    

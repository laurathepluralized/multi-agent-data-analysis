#!/usr/bin/env python3
#
# Basic structure inspired/informed by 
# https://blog.miguelgrinberg.com/post/easy-websockets-with-flask-and-gevent

from python import outfiles2condensed as dat
from flask import Flask, render_template
from flask_socketio import SocketIO, emit
from random import random
from time import sleep
from threading import Thread, Event


app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)


thread = Thread()
thread_stop_event = Event()

class RandomThread(Thread):
    def __init__(self):
        self.delay = 1
        super(RandomThread, self).__init__()

    def rng(self):
        while not thread_stop_event.isSet():
            num = round(random()*10, 3)
            print(num)
            socketio.emit('newnum', {'number': num}, namespace='/testing')
            print('emitted number ', num)
            sleep(self.delay)

    def run(self):
        self.rng()



@app.route('/')
def index():
    return render_template('index.html')

@socketio.on('connect', namespace='/testing')
def test_connect():
    global thread
    print('client connected')

    if not thread.isAlive():
        print('starting thread')
        thread = RandomThread()
        thread.start()

@socketio.on('disconnect', namespace='/testing')
def test_disconnect():
    print('client disconnected')

if __name__ == '__main__':
    socketio.run(app)



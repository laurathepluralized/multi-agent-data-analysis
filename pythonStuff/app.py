#!/usr/bin/env python3

from threading import Lock
from flask import Flask, render_template, session, request
from flask_socketio import SocketIO, emit, disconnect
from random import random


from python import outfiles2condensed as dat

async_mode = 'eventlet'

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app, async_mode=async_mode)
thread = None
thread_lock = Lock()

def background_thread():
    count = 0;
    while True:
        print('sleeping')
        socketio.sleep(2)
        count += 1;
        number = round(random()*10, 3)
        socketio.emit('my_response',
                {'data': number, 'count': count},
                namespace='/testing')


@app.route('/')
def index():
    return render_template('index.html', async_mode=socketio.async_mode)


@socketio.on('connect', namespace='/testing')
def test_connect():
    global thread
    with thread_lock:
        if thread is None:
            thread = socketio.start_background_task(target=background_thread)
    emit('my_response', {'data': 'connected!', 'count': 0})

@socketio.on('myping', namespace='/testing')
def ping_resp():
    emit('resp')




@socketio.on('disconnect', namespace='/testing')
def test_disconnect():
    print('client disconnected', request.sid)

if __name__ == '__main__':
    socketio.run(app, debug=True, host='0.0.0.0')

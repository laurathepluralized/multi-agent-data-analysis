#!/usr/bin/env python3
#
# Basic structure inspired/informed by 
# https://blog.miguelgrinberg.com/post/easy-websockets-with-flask-and-gevent

from flask import Flask, render_template
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

@app.route('/')
def index():
    return render_template('index.html')

@socketio.on('got_data')
def test_message(message):
    emit('test_response', {'data': message['data']})

if __name__ == '__main__':
    socketio.run(app)



#!/usr/bin/env python3
#
from flask import Flask, render_template
from flask_socketio import SocketIO, emit
import summary2json

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

@app.route('/')
def index():
    return render_template('index.html')

@socketio.on('my event')
def test_message(message):
    emit('my response', {'data': 'got it!'})


@socketio.on('datareq')
def send_data(datadir):
    thedata = summary2json.glob2json(filename, jobnum, False)
    # I have no idea what I'm doing. Is this a reasonable size of thing to cram 
    # into one emit message?
    emit('newdata', thedata)

if __name__ == '__main__':
    socketio.run(app)

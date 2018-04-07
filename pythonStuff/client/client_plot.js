
// The following is based on the github repo here:
// https://github.com/Chainfrog-dev/async_flask_2
$(document).ready(function() {
    namespace = '/testing';

    var socket = io.connect(location.protocol + '//' + document.domain + ':' + location.port + namespace);

    socket.on('connect', function() {
        socket.emit('my_event', {data: 'connected from js side!'});
    });

    socket.on('my_response', function(msg) {
        $('#log').append('<br>' + $('<div>').text('Received #' + msg.counter + ':' + msg.data).html());

    });


    var resp_times = [];
    var starttime;
    window.setInterval(function() {
        starttime = (new Date).getTime();
        socket.emit('myping');
    }, 1000);


    socket.on('resp', function() {
        var latency = (new Date).getTime() - starttime;
        resp_times.push(latency);
        resp_times = resp_times.slice(-50);
        var sum = 0;
        for (var ii = 0; ii < resp_times.length; ii++) {
            sum += resp_times[ii];
        };

        $('ping-resp').text(Math.round(10*sum/resp_times.length) / 10);
    });

    $('form#disconnect').submit(function(event) {
        socket.emit('disconnect_request');
        return false;
    )};
});



// var axPadding = 50;
// var svgSize = {width: 1000, height: 800};
// var margin = {top: 20, right: 5, bottom: 20, left: 100};
// var widthWithLegend = svgSize.width - margin.left - margin.right;
// var width = widthWithLegend - 4*axPadding;
// var heightWithTitle = svgSize.height - margin.top - margin.bottom;
// var height = heightWithTitle - axPadding;

// // Based on 
// // https://stackoverflow.com/questions/14118076/socket-io-server-to-server/24657202#24657202
// var socket = io('http://localhost');

// socket.on('connect', function(socket) {
//     console.log('got connection message from server!');

//     socket.on('message', function(msgdata) {
//         console.log('message: ' + msgdata);
//     });

//     socket.on('json', function(data) {
//         console.log('json: ' + data);
//         console.log('TODO: make nice plots');
//     });

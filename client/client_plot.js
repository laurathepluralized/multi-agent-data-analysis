
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




// });

// socket.on('newdata', function(d) {
//     d = JSON.parse(d);
//     data.push({
//         x: +d.x,
//         y: +d.y
//     });

//     tick();
// });

// Click on scatter point point, print out the name of the mission file (or the 
// parmeters from the mission file)
// function emit_filename(point, recipient) {
// };


//function tick() {


////d3.json("wine_quality.csv", function(plotdata) {

//    plotdata.push(
//        plotdata.forEach(function(d) {
//            console.log('this is where intermediary data processing would go')
//        });

//    var xExtent = d3.extent(plotdata, function(d) {
//        return +d.x;
//    });
//    var yExtent = d3.extent(plotdata, function(d) {
//        return +d.y;
//    });
//    var xScale = d3.scale.linear()
//        .domain([d3.min([0, xExtent[0]]), xExtent[1]])
//        .range([0, width]);

//    var yScale = d3.scale.linear()
//        .domain([d3.min([0, yExtent[0]]), yExtent[1]])
//        .range([height, 0]);


//    var xAxis = d3.svg.axis()
//        xAxis.scale(xScale)
//        .orient("bottom");

//    var yAxis = d3.svg.axis()
//        yAxis.scale(yScale)
//        .orient("left");

//    var svg = d3.select("body").append("svg")
//        .attr("width", svgSize.width)
//        .attr("height", svgSize.height);
//    var svgg = svg.append("g")
//        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

//    svgg.append("g")
//        .attr("class", "axis")
//        .attr("transform", "translate(" + 0 + "," + height + ")")
//        .call(xAxis);

//    svgg.append("g")
//        .attr("class", "axis")
//        .attr("transform", "translate(" + 0 + "," + 0 + ")")
//        .call(yAxis);

//    svgg.selectAll(".point")
//        .data(plotdata)
//      .enter().append("path")
//        .attr("class", "point")
//        .attr("stroke", "black")
//        .attr("transform", function(d) {
//            return "translate(" + (xScale(d.x)) + "," + yScale(d.y) + ")";
//        });


//}




var axPadding = 50;
var svgSize = {width: 1000, height: 800};
var margin = {top: 20, right: 5, bottom: 20, left: 100};
var widthWithLegend = svgSize.width - margin.left - margin.right;
var width = widthWithLegend - 4*axPadding;
var heightWithTitle = svgSize.height - margin.top - margin.bottom;
var height = heightWithTitle - axPadding;




// Based on 
// https://stackoverflow.com/questions/14118076/socket-io-server-to-server/24657202#24657202
var socket = io('http://localhost');

socket.on('connect', function(sock) {
    console.log('got connection message from server!');

    socket.on('message', function(msgdata) {
        console.log('message: ' + msgdata);
    });

    socket.on('json', function(data) {
        console.log('json: ' + data);
        console.log('TODO: make nice plots');
    });




});

socket.on('newdata', function(d) {
    d = JSON.parse(d);
    data.push({
        x: +d.x,
        y: +d.y
    });

    tick();
});

// Click on scatter point point, print out the name of the mission file (or the 
// parmeters from the mission file)
// function emit_filename(point, recipient) {
// };


function tick() {


//d3.json("wine_quality.csv", function(plotdata) {
        plotdata.forEach(function(d) {
            console.log('this is where intermediary data processing would go')
        });

    var xExtent = d3.extent(plotdata, function(d) {
        return +d.x;
    });
    var yExtent = d3.extent(plotdata, function(d) {
        return +d.y;
    });
    var xScale = d3.scale.linear()
        .domain([d3.min([0, xExtent[0]]), xExtent[1]])
        .range([0, width]);

    var yScale = d3.scale.linear()
        .domain([d3.min([0, yExtent[0]]), yExtent[1]])
        .range([height, 0]);


    var xAxis = d3.svg.axis()
        xAxis.scale(xScale)
        .orient("bottom");

    var yAxis = d3.svg.axis()
        yAxis.scale(yScale)
        .orient("left");

    var svg = d3.select("body").append("svg")
        .attr("width", svgSize.width)
        .attr("height", svgSize.height);
    var svgg = svg.append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    svgg.append("g")
        .attr("class", "axis")
        .attr("transform", "translate(" + 0 + "," + height + ")")
        .call(xAxis);

    svgg.append("g")
        .attr("class", "axis")
        .attr("transform", "translate(" + 0 + "," + 0 + ")")
        .call(yAxis);

    svgg.selectAll(".point")
        .data(plotdata)
      .enter().append("path")
        .attr("class", "point")
        .attr("stroke", "black")
        .attr("transform", function(d) {
            return "translate(" + (xScale(d.x)) + "," + yScale(d.y) + ")";
        });


}


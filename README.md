# About

## What is this project for?
The tool in this repository is meant to simplify the process of analyzing 
simulation output from multi-agent robotics simulations.
In particular, it was built with simulation data from the SCRIMMAGE multi-agent 
simulator (https://github.com/gtri/scrimmage/) in mind.

## I don't want to use SCRIMMAGE, but I have lots of CSV files. Can I still use this tool?
Any non-temporal simulation data that can be put into a CSV file can be
analyzed with this tool.
Just be aware that the aggregation and cleaning scripts in the pythonStuff
directory are not likely to work on your data if it wasn't generated with
SCRIMMAGE.


# Installation:

## Install R:
Follow the directions for your operating system of choice from
https://cran.r-project.org/ to install R.

### Required R Dependencies:
The Shiny application uses the following R libraries:
* shiny
* shinydashboard
* ggplot2
* scatterD3
* data.table
* sqldf
* dplyr
* readxl
* corrplot
* randomForest

To install these dependencies, after installing R, execute the following at
the command line:

    $ R
    > install.packages(c("shiny", "shinydashboard", "ggplot2", "scatterD3", "data.table", "sqldf", "dplyr", "readxl", "corrplot", "randomForest", "caTools"))



To run the Shiny application, run the following at the command line (assumes
current directory is `multi-agent-data-analysis`):

    $ R
    > library(shiny)
    > runApp("shinyApp")












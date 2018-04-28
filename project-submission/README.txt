# DESCRIPTION

## What is this project for?
The tool in this repository is meant to simplify the process of analyzing 
simulation output from multi-agent robotics simulations.
In particular, it was built with simulation data from the SCRIMMAGE multi-agent 
simulator (https://github.com/gtri/scrimmage/) in mind.

## I don't want to use SCRIMMAGE, but I have lots of CSV files. Can I still use this tool?
Any non-temporal simulation data that can be put into a CSV file can be
analyzed with this tool, with minor modifications (specifying column names
and types, file locations).
Just be aware that the aggregation and cleaning scripts in the pythonStuff
directory are not likely to work on your data if it wasn't generated with
SCRIMMAGE.


# INSTALLATION

## Install R:
Follow the directions for your operating system of choice from
https://cran.r-project.org/ to install R.

### Required R Dependencies:
The Shiny application depends upon a number of R libraries. 
To install these dependencies, after installing R, execute the following in the
R console:

    install.packages(c("shiny",
    "shinydashboard",
    "ggplot2",
    "scatterD3",
    "data.table",
    "sqldf",
    "dplyr",
    "readxl",
    "corrplot",
    "randomForest",
    "caTools",
    "pls"))


# EXECUTION

To run the Shiny application, first start R, then run the following at the R
console from the root directory of this repository:

    > library(shiny)
    > runApp("shinyApp")

Demo data is included with the Shiny application, in the shinyApp/data
directory.
Future work on this application will see the data upload feature interact
with the entire set of visualization and analysis tools, but for the current
iteration, the following filepaths can be modified in the code to try different
datasets.
The included datasets are described below:


`shinyApp/data/default_data.csv` contains results and parameters from 18,000
SCRIMMAGE runs of a predator-prey simulation of fifty prey and five predators.
Each of the 36 combinations of parameter levels was run 500 times, making
this demo dataset ideal for testing stability analysis.
The parameters varied and their levels are:
* `max_speed_t_2_predator` (m/s): 30, 40, 50
* `turn_rate_max_t_1` (rad/s): 0.25, 0.5, 0.75, 1.0, 1.25, 1.5
* `allow_prey_switching_t_2_predator`: True, False


`shinyApp/data/pred-prey-lhs-small.csv` contains results from nearly 7,000
SCRIMMAGE runs in which the same variables were varied using Latin
Hypercube Sampling
(please see https://en.wikipedia.org/wiki/Latin_hypercube_sampling for an
overview of this technique).
This data is provided to more clearly demonstrate the capabilities of the 
visualization overview; please note that it is not intended for use with the 
stability analysis aspects of this tool.
The ranges of these variables are:
* `max_speed_t_2_predator` (m/s): [30, 50)
* `turn_rate_max_t_1` (rad/s): [0.25, 1.5)
* `allow_prey_switching_t_2_predator`: True, False





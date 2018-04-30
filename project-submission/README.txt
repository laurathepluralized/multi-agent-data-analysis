# DESCRIPTION

## What is this project for?
The tool in this repository is meant to simplify the process of analyzing 
simulation output from multi-agent robotics simulations.
In particular, it was built with simulation data from the SCRIMMAGE multi-agent 
simulator (https://github.com/gtri/scrimmage/) in mind.

## I don't want to use SCRIMMAGE, but I have a CSV file I want to analyze. Can I still use this tool?
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

    > install.packages(c("shiny",
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
iteration, the filepaths can be modified in the code to try different datasets.
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



## Data Loading
While the application automatically loads a sample set of data, the CSV loading 
page can be used to upload other datasets.
To upload a new CSV file, first indicate if your CSV file has a header row, 
then use the "Browse" button to bring up a file selection dialog.

### Stability Analysis
On this page you can select the pertinent Categorical and Numeric variables, 
along with a Target variable for running a stability analysis. Please take care 
not to let any of the selection sets overlap.
Then start the stability analysis by clicking the "Run Stability Analysis" 
button.
For the demonstration datasets included in the repositories, select the 
following columns as noted:
* `max_speed_t_2_predator`: numerical
* `turn_rate_max_t_1`: numerical
* `allow_prey_switching_t_2_predator`: categorical
* 'team_id': categorical
For the target variable, select `NonTeamCapture`.

Once the stability analysis completes, you should see a table containing one 
row for each unique combination of input factors. Each of these rows will list 
the values for each input factor along with distribution information about the 
output variable associated with the input values.

### Correlation Analysis
On this page you can select the pertinent Categorical and Numeric variables, 
along with a Target variable for running a Correlation analysis. Please take 
care not to let any of the selection sets overlap.
Then start the stability analysis by clicking the "Run Stability Analysis" 
button.
For the demonstration datasets included in the repositories, select the 
following columns as noted:
* `max_speed_t_2_predator`: numerical
* `turn_rate_max_t_1`: numerical
* `allow_prey_switching_t_2_predator`: categorical
* 'team_id': categorical
For the target variable, select `NonTeamCapture`.


### Interactive Scatter/Box Plots
In this tab, a dataset is uploaded automatically. (Future work is to integrate 
it to use the data from the file upload tab, and to allow for selection of
numerical and categorical columns from lists as in the other tabs.)
If you wish to select another dataset, please change the filepath in `app.R` 
loaded into the variable `dsim` and fill in the `paramcols` variable, the list
of parameter columns, as appropriate for your dataset (the column names
already in this list are correct for both default_data.csv and
for pred-prey-lhs-small.csv).



### Sidenote: Aggregating and cleaning data using included helper scripts

If you wish to analyze your own dataset that was generated with SCRIMMAGE,
use the pythonStuff/outfiles2condensed.py aggregation script to aggregate the
results and mission file parameters.
To use these tools, first run the following at the Ubuntu command line to
install the Python3 dependencies.

    sudo apt-get install python3-pip
    sudo -H pip3 install os shutil sys platform subprocess numpy json \
        pickle pandas pydoe glob argparse pdb

If you are not using Ubuntu, the above instructions may not apply; follow
your OS's procedure for installing Python 3
(see https://www.python.org/downloads/), then install the dependency
packages mentioned in the pip3 command.


Run the following to see an explanation of the aggregation script's inputs:

    python3 pythonStuff/outfiles2condensed.py --help

A sample run of outfiles2condensed might look like this:

    python3 scripts/outfiles2condensed.py --path ~/.scrimmage/logs \
        --filename my_agg_data -c -r

After aggregating your data with outfiles2condensed, if you are doing similar
predator-vs-prey simulations to the demo simulations, run the following
(or similar; replace the path with the path to your CSV file, no extension) to
remove unnecessary columns from your data:
    
    python3 pythonStuff/clean_data.py --path ~/.scrimmage/logs/my_agg_data

The cleaned output file name will be amended with the suffix _clean but will be 
in the same directory as the uncleaned data.




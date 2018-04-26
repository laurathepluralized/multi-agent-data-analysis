#
# This is the user-interface definition of a Shiny web application. You can
# run the application by clicking 'Run App' above.
#
# Find out more about building applications with Shiny here:
# 
#  http://shiny.rstudio.com/
#

options(shiny.maxRequestSize=500*1024^2)
palette(c("#E41A1C", "#377EB8", "#4DAF4A", "#984EA3",
      "#FF7F00", "#FFFF33", "#A65628", "#F781BF", "#999999"))

library(shiny)
library(ggplot2)
library(shinydashboard)
library(scatterD3)
source("modeling.R")
source("analysis.R")
source("loading.R")
source("stability.R")

# Read Default CSV into R

#I vote that we rename this to default_data
dsim <- read.csv(file="data/default_data.csv", header=TRUE, sep=",")
# dsim <- read.csv(file="data/pred-prey-lhs-small.csv", header=TRUE, sep=",")
default_data = dsim

dsnames <- names(dsim)
default_columns <- dsnames
# paramcols <- c('turn_rate_max_t_1','max_speed_t_2_predator','allow_prey_switching_t_2_predator')
paramcols <- c('max_speed_t_2_predator','turn_rate_max_t_1','allow_prey_switching_t_2_predator')
metriccols <- c('NonTeamCapture')

# These are the types of modeling we can use in the modeling tab
models <- c('Multivariate linear regression',
            'Linear regression with AIC',
            'Principal component regression (using PCA)',
            'Partial least squares',
            'Random Forest Regression',
            'Neural networks')

#recommendation <- read.csv('recommendation.csv',stringsAsFactors = F,header=T)
#head(dsim)

default_lines <- data.frame(slope = c(0, Inf), 
  intercept = c(0, 0),
  stroke = "#000",
  stroke_width = 1,
  stroke_dasharray = c(5, 5))
threshold_line <- data.frame(slope = 0, 
  intercept = 30, 
  stroke = "#F67E7D",
  stroke_width = 2,
  stroke_dasharray = "")

ui <- dashboardPage(
  dashboardHeader(title="Multi-Robot Simulation Dashboard", titleWidth = 350),
  
  ## Sidebar content
  dashboardSidebar(
    sidebarMenu(
      # Setting id makes input$tabs give the tabName of currently-selected tab
      id = "tabs",
      menuItem("Main", tabName = "main", icon = icon("caret-up")),
      menuItem("Stability Analysis", tabName = "stability", icon = icon("th")),
      menuItem("Widgets", tabName = "widgets", icon = icon("bar-chart-o")),
      menuItem("Scatter Plot", tabName = "scatter", icon = icon("sliders")),
      menuItem("Modeling", tabName = "modeling", icon = icon("calculator"))
    ),
    textOutput("res")
  ),
  
  ## Body content
  dashboardBody(
    tabItems(
      # First tab content
      tabItem(tabName = "main", loading_ui(), preview_ui()),
      tabItem(tabName = "stability", stabilityAnalysisUI("stability", "Stability Analysis")),
      # Second tab content
      tabItem(tabName = "widgets",
        h2("Using k-means clustering"),
# The following code between the lines beginning with # --- was heavily based
# on the kmeans-example code at this repo:
# https://github.com/rstudio/shiny-examples/tree/master/050-kmeans-example
# which was released under the MIT license here:
# https://shiny.rstudio.com/gallery/kmeans-example.html
# MIT license preamble prepended to the code by Laura Strickland as a
# good-faith effort to adhere to the licensing requirements.
# The code was changed to fit with the data we wish to analyze.

# --- Begin K-Means Clustering MIT-licensed UI code
# Copyright (c) 2014, Joe Cheng <joe@rstudio.com>

# Permission is hereby granted, free of charge, to any person obtaining
# a copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so, subject to
# the following conditions:

# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
# LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
        fluidRow(
          box(
            selectInput('xcol', 'X Variable', names(dsim)),
            selectInput('ycol', 'Y Variable', names(dsim),
              selected = names(dsim)[[12]]),
            numericInput('clusters', 'Cluster count', 3,
                   min = 1, max = 9)
          ),
          box(
            plotOutput('kmeansplot',
              click = "plot_click",
              dblclick = "plot_dblclick",
              hover = "plot_hover",
              brush = "plot_brush"
            ),
            verbatimTextOutput("info")
          )
        )
# --- end K-Means Clustering MIT-licensed UI code

      ),
      tabItem(tabName='modeling',
        h2("Modeling"),
        selectInput('model_to_use', 'Select Which Model To Use', models),
        plotOutput("graph_modeling"),
        verbatimTextOutput("info_modeling")
      ),
      #3rd tab content
      tabItem(tabName="scatter",
        h2("Interactive Scatterplot"),
        fluidRow(
          box(
            selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim[paramcols])),
            selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim[metriccols]))
          ),
          box(
            uiOutput('valuefixers')
          )
        ),
        fluidRow(
          box(
            plotOutput("plot_scatter", click = "plot_click", brush = "plot_brush")
          ),
          verbatimTextOutput("info_scatter")
        )
      )
    )
  )
)


server <- function(input, output, session) {
  session$userData$data_file <- default_data
  session$userData$columnNames <- default_columns
  session$userData$testText <- "using default data"
  
  handle_loading(input, output, session)
  callModule(stabilityAnalysis, "stability", stringsAsFactors = FALSE)

  
  #This previews the CSV data file
  #output$filetable <- renderTable ({
  #  filedata()
  #})

  ## output text message for sidebar selection
  output$res <- renderText({
    paste("You've selected:", input$tabs)
  })
  
# The following code between the lines beginning with # --- was heavily based
# on the kmeans-example code at this repo:
# https://github.com/rstudio/shiny-examples/tree/master/050-kmeans-example
# which was released under the MIT license here:
# https://shiny.rstudio.com/gallery/kmeans-example.html
# MIT license preamble added by Laura Strickland as a good-faith effort to
# adhere to the licensing requirements.
# The code was changed to fit with the data we wish to analyze.

# --- Begin K-Means Clustering MIT-licensed server code

# Copyright (c) 2014, Joe Cheng <joe@rstudio.com>

# Permission is hereby granted, free of charge, to any person obtaining
# a copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so, subject to
# the following conditions:

# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
# LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

  ## output plot for multi-robot simulation data (Widgets)
  ## data for k-means cluster - dsim
  selectedData <- reactive({
  dsim[, c(input$xcol, input$ycol)]
  })
  
  clusters <- reactive({
  kmeans(selectedData(), input$clusters)
  })
  
  output$kmeansplot <- renderPlot({
  par(mar = c(5.1, 4.1, 0, 1))
  plot(selectedData(),
    col = clusters()$cluster,
    pch = 20, cex = 3)
  text(selectedData(), pos=1, labels = dsim$score)
  points(clusters()$centers, pch = 4, cex = 4, lwd = 4)
  })
  
  ## output text info box for plot click, hover etc. (Widgets)
  output$info <- renderText({
  xy_str <- function(e) {
    if(is.null(e)) return("NULL\n")
    paste0("x=", round(e$x, 1), " y=", round(e$y, 1), "\n")
  }
  xy_range_str <- function(e) {
    if(is.null(e)) return("NULL\n")
    paste0("xmin=", round(e$xmin, 1), " xmax=", round(e$xmax, 1), 
    " ymin=", round(e$ymin, 1), " ymax=", round(e$ymax, 1))
  }
# --- end K-Means Clustering MIT-licensed server code
  
  paste0(
    "click: ", xy_str(input$plot_click),
    "dblclick: ", xy_str(input$plot_dblclick),
    "hover: ", xy_str(input$plot_hover),
    "brush: ", xy_range_str(input$plot_brush)
  )
  })

  variables = reactiveValues(not_to_plot_params = c())

  observeEvent(input$theparamx, {
    variables$not_to_plot_params = paramcols[paramcols != input$theparamx]
  })

  #reactive({not_to_plot_params <- paramcols[paramcols != input$theparamx]})

  #print(variables$not_to_plot_params)
  #a <- 1
  #b <- 10
  mydata <- reactive({
    dsim[, c(input$theparamx, input$themetricy)]
  })


  output$valuefixers <- renderUI({
    valuefixers <- lapply(1:length(paramcols), function(i) {
      thevals <- dsim[paramcols[i]]
      inname <- names(thevals)
      # only make a variable-fixing element if this isn't the x-axis var
      if (inname != input$theparamx) {
        if (is.numeric(thevals[,1])) {
          themin = min(thevals[,1])
          themax = max(thevals[,1])
          stepsize <- 0.1
          uniquevals <- unique(thevals[,1])
          if (length(uniquevals) > 1 & length(uniquevals) < 10) {
            stepsize <- round(uniquevals[2], digits=-2) - round(uniquevals[1], digits=-2)
          }
          sliderInput(inname,
          paste('Select fixed value for ', inname), min=themin,
          max=themax, value=c(themin, themax), step = stepsize, round = -2)
        }
        else {
          pickvals <- unique(thevals[,1])
          selectInput(inname,
          paste('Select fixed value for ', inname), pickvals)
        }
      }
    })
    do.call(tagList, valuefixers)
  })


  plotIsScatter <- TRUE
  output$plot_scatter<- renderPlot({
    data = dsim 
    to_plot = data
    # we are only looking at team 2 right now; obviously remove this next line
    # if your metrics cover multiple teams
    # TODO: add an input to select this?
    to_plot = to_plot[which(to_plot['team_id'] == 2),]
    if (!is.null(to_plot)) {
      for (i in 1:length(variables$not_to_plot_params)) {
        param = variables$not_to_plot_params[i]
        vals = input[[param]]
        # If only one value is selected, make vals length 1 so it doesn't
        # try to filter by range
        if (length(vals) > 1) {
          if (min(vals) == max(vals)) {
            vals = min(vals)
          }
        }
        if (length(vals) == 1) {
          tempdf = to_plot[which(to_plot[param] == input[[param]]),]
          to_plot = tempdf
        } else if (length(vals) > 1) {
          # I'm getting the feeling R doesn't like doing df stuff in-place
          tempdf = to_plot[which(to_plot[param] >= min(vals)),]
          tempdf2 = tempdf[which(tempdf[param] <= max(vals)),]
          to_plot = tempdf2
        }
      }
      if (is.numeric(dsim[,input$theparamx][1])) {
        plot(to_plot[ c(input$theparamx, input$themetricy)])
        plotIsScatter <- TRUE
      } else {
        x = (to_plot[, c(input$theparamx)])
        y = (to_plot[, c(input$themetricy)])
        boxplot(y ~ x, xlab = input$theparamx, ylab = input$themetricy)
        plotIsScatter <- FALSE
      }

    output$info_scatter <- renderPrint({
      # With base graphics, need to tell it what the x and y variables are.
      # Max of 10, otherwise we overload the user
      points <- brushedPoints(to_plot, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)
      head(points, 10)
    })
    }
  })
  
  #Leaving this as an output debugger that updates that we can look at
  #output$info <- renderPrint({
    ##variables$not_to_plot_params[1]
    ##dsimcsv[which(dsimcsv[variables$not_to_plot_params[1]] == input[[variables$not_to_plot_params[1]]]),]
  #})
  #model = run_modeling(dsim, 1)


  # Writes the summary output of run_modeling
  # TODO: Get the summary working for all model types
  output$info_modeling <- renderPrint({
    model = run_modeling(dsim, which(models == input$model_to_use))
    #input$model_to_use
    summary(model)
  })

  # Draws the graph output of the model from run_modeling
  # TODO: Get summaries and graphs working for all of the model types
  output$graph_modeling <- renderPlot({
    model = run_modeling(dsim, which(models == input$model_to_use))
    par(mfrow=c(2,2))
    plot(model,which=c(1:4), col = "cornflowerblue")
  })
}
shinyApp(ui = ui, server = server)

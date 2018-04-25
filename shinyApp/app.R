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
source('loading.R')

# Read CSV into R
dsim <- read.csv(file="data/betterdata.csv", header=TRUE, sep=",")
paramcols <- c('turn_rate_max_t_1','vel_max_predator','allow_prey_switching_t_2_predator')
metriccols <- c('NonTeamCapture')

# These are the types of modeling we can use in the modeling tab
models <- c('Multivariate linear regression',
            'Linear regression with backward elimination',
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

dsnames <- c()

ui <- dashboardPage(
  dashboardHeader(title="Multi-Robot Simulation Dashboard", titleWidth = 350),
  
  ## Sidebar content
  dashboardSidebar(
    sidebarMenu(
      # Setting id makes input$tabs give the tabName of currently-selected tab
      id = "tabs",
      menuItem("Main", tabName = "main", icon = icon("dashboard")),
      menuItem("Widgets", tabName = "widgets", icon = icon("bar-chart-o")),
      menuItem("Scatter Plot", tabName = "scatter", icon = icon("bar-chart-o")),
      menuItem("Modeling", tabName = "modeling", icon = icon("calculator"))
    ),
    textOutput("res")
  ),
  
  ## Body content
  dashboardBody(
    tabItems(
      # First tab content
      tabItem(tabName = "main", loading_ui()
      ),
      # Second tab content
      tabItem(tabName = "widgets",
        h2("Using k-means clustering"),
        fluidRow(
          box(
            selectInput('xcol', 'X Variable', names(dsim)),
            selectInput('ycol', 'Y Variable', names(dsim),
                  selected = names(dsim)[[12]]),
            numericInput('clusters', 'Cluster count', 3,
                   min = 1, max = 9)
          ),
          box(
            plotOutput('plot2',
                 click = "plot_click",
                 dblclick = "plot_dblclick",
                 hover = "plot_hover",
                 brush = "plot_brush"
            ),
            verbatimTextOutput("info")
          )
        )
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
        selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim[paramcols])),
        selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim[metriccols])),
        uiOutput('valuefixers'),
        plotOutput("plot_scatter", click = "plot_click", brush = "plot_brush"),
        verbatimTextOutput("info_scatter")
      )
    )
  )
)


server <- function(input, output, session) {

  ## output table for file contents (Main)
  output$contents <- renderTable({
  
  # input$file1 will be NULL initially. After the user selects
  # and uploads a file, head of that data file by default,
  # or all rows if selected, will be shown.
  
  req(input$file1)
  
  df <- read.csv(input$file1$datapath,
    header = input$header,
    sep = input$sep,
    quote = input$quote)
  
  
  if(input$disp == "head") {
    return(head(df))
  }
  else {
    return(df)
  }
    
  })

  #This function is responsible for loading in the selected file
  filedata <- reactive ({
  req(input$file1)
  infile <- input$file1
  if (is.null(infile)) {
    # User has not uploaded a file yet
    return(NULL)
  }
  read.csv(infile$datapath)
  })

  observe ({
  req(input$file1)
  dsnames <- names(filedata())
  cb_options <- list()
  cb_options[ dsnames] <- dsnames
  updateCheckboxGroupInput(session, "inCheckboxGroup",
    label = "Check Box Group",
    choices = cb_options,
    selected = "")
  })
  
  #The following set of functions populate the column selectors
  output$toCol <- renderUI ({
  df <-filedata()
  if (is.null(df)) return(NULL)
  
  items=names(df)
  names(items)=items
  selectInput("to", "To:",items)
  
  })
  
  output$fromCol <- renderUI ({
  df <-filedata()
  if (is.null(df)) return(NULL)
  
  items=names(df)
  names(items)=items
  selectInput("from", "From:",items)
  
  })
  
  #The checkbox selector is used to determine whether we want an optional column
  output$amountflag <- renderUI ({
  df <-filedata()
  if (is.null(df)) return(NULL)
  
  checkboxInput("amountflag", "Use values?", FALSE)
  })
  
  #If we do want the optional column, this is where it gets created
  output$amountCol <- renderUI({
  df <-filedata()
  if (is.null(df)) return(NULL)
  #Let's only show numeric columns
  nums <- sapply(df, is.numeric)
  items=names(nums[nums])
  names(items)=items
  selectInput("amount", "Amount:",items)
  })
  
  #This previews the CSV data file
  #output$filetable <- renderTable ({
  #  filedata()
  #})
  
  ## render table for stability analysis 
  ## data for stability analysis
  stabilityData <- reactive ({
  filedata()
  if (is.null(filedata())) return(NULL)
  })
  
  stabilityOutput <- reactive ({
  result_col = "NonTeamCapture"
  numericCol <- c("vel_max_t_1",
    "vel_max_predator",
    "pitch_rate_max_predator",
    "turn_rate_max_predator")
  categoryCol <- c("team_id",
    "allow_prey_switching_t_2_predator")
  runStablilityCheck(stabilityData(), result_col, numericCol, categoryCol)
  
  })
  
  output$stabilityAnalysis <- renderTable ({
  stabilityOutput()
  })
#   output$stabilityAnalysis <- renderTable ({
#     # input$file1 will be NULL initially. After the user selects
#     # and uploads a file, head of that data file by default,
#     # or all rows if selected, will be shown.
#   
#     if(input$runStability) {
#       req(input$file1)
#     
#       df <- read.csv(input$file1$datapath,
#         header = input$header,
#         sep = input$sep,
#         quote = input$quote)
#     
#       result_col <- input$result_col
#       numericCol <- c("vel_max.t.1",
#         "vel_max.predator",
#         "pitch_rate_max.predator",
#         "turn_rate_max.predator")
#       categoryCol <- c("team_id",
#         "allow_prey_switching.t.2.predator")
#     
#       stabilityResults <- runStablilityCheck(df, result_col, numericCol, categoryCol)
#    
#       return(stabilityResults)
#  }
#  
#  return (c(0,0))
#  }
#  )
  
  
  ## output text message for sidebar selection
  output$res <- renderText({
  paste("You've selected:", input$tabs)
  })
  
  ## output plot for multi-robot simulation data (Widgets)
  ## data for k-means cluster - dsim
  selectedData <- reactive({
  dsim[, c(input$xcol, input$ycol)]
  })
  
  clusters <- reactive({
  kmeans(selectedData(), input$clusters)
  })
  
  output$plot2 <- renderPlot({
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
          themin = min(thevals)
          themax = max(thevals)
          stepsize <- NULL
          uniquevals <- unique(thevals[,1])
          if (length(uniquevals) > 1) {
            stepsize <- uniquevals[2] - uniquevals[1]
          }
          sliderInput(inname,
          paste('Select fixed value for ', inname), min=themin,
          max=themax, value=c(themin, themax), step = stepsize, round = -2)
        }
        else {
          pickvals <- unique(thevals)
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
    for (i in 1:length(variables$not_to_plot_params)) {
      param = variables$not_to_plot_params[i]
      to_plot = to_plot[which(to_plot[param] == input[[param]]),]
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
    plot(to_plot[, c(input$theparamx, input$themetricy)])
  })

  output$info_scatter <- renderPrint({
    # With base graphics, need to tell it what the x and y variables are.
    # Max of 10, otherwise we overload the user
    points <- brushedPoints(dsim, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)
    head(points, 10)
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
    par(mfrow=c(2,2))#drawing in 2 by 2 format
    plot(model,which=c(1:4), col = "cornflowerblue")
    plot(model$fitted.values, model$residuals);
  })
}
shinyApp(ui = ui, server = server)

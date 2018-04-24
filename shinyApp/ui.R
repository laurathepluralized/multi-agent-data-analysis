#
# This is the user-interface definition of a Shiny web application. You can
# run the application by clicking 'Run App' above.
#
# Find out more about building applications with Shiny here:
# 
#    http://shiny.rstudio.com/
#

palette(c("#E41A1C", "#377EB8", "#4DAF4A", "#984EA3",
          "#FF7F00", "#FFFF33", "#A65628", "#F781BF", "#999999"))

library(shiny)
library(ggplot2)
library(shinydashboard)
library(scatterD3)

# Read CSV into R
#dsim <- read.csv(file="data/betterdata2.csv", header=TRUE, sep=",")
dsim <- read.csv('data/betterdata2.csv', stringsAsFactors = FALSE, header=TRUE)

#recommendation <- read.csv('recommendation.csv',stringsAsFactors = F,header=T)
#head(dsim)

ui <- dashboardPage(
  dashboardHeader(title="Multi-Robot Simulation Dashboard", titleWidth = 350),
  
  ## Sidebar content
  dashboardSidebar(
    sidebarMenu(
      # Setting id makes input$tabs give the tabName of currently-selected tab
      id = "tabs",
      menuItem("Main", tabName = "main", icon = icon("dashboard")),
      menuItem("Widgets", tabName = "widgets", icon = icon("bar-chart-o"))
    ),
    textOutput("res")
  ),
  
  ## Body content
  dashboardBody(
    tabItems(
      # First tab content
      tabItem(tabName = "main",
              fluidRow(
                box(plotOutput("plot1", height = 250)),
                
                box(
                  title = "Controls",
                  sliderInput("slider", "Number of observations:", 1, 100, 50)
                )
              ),
              fluidRow(
                # Dynamic infoBoxes
                infoBoxOutput("progressBox")
              ),
              fluidRow(
                # Clicking this will increment the progress amount
                box(width = 4, actionButton("count", "Increment progress"))
              ),
              fluidRow(
                box (
                  # Input: Select a file ----
                  fileInput("file1", "Choose CSV File",
                            multiple = TRUE,
                            accept = c("text/csv",
                                       "text/comma-separated-values,text/plain",
                                       ".csv")),
                  
                  #These column selectors are dynamically created when the file is loaded
                  uiOutput("fromCol"),
                  uiOutput("toCol"),
                  uiOutput("amountflag"),
                  #The conditional panel is triggered by the preceding checkbox
                  conditionalPanel(
                    condition="input.amountflag==true",
                    uiOutput("amountCol")
                  ),
                  
                  # Horizontal line ----
                  tags$hr(),
                  
                  # Input: Checkbox if file has header ----
                  checkboxInput("header", "Header", TRUE),
                  
                  # Input: Select separator ----
                  radioButtons("sep", "Separator",
                               choices = c(Comma = ",",
                                           Semicolon = ";",
                                           Tab = "\t"),
                               selected = ","),
                  
                  # Input: Select quotes ----
                  radioButtons("quote", "Quote",
                               choices = c(None = "",
                                           "Double Quote" = '"',
                                           "Single Quote" = "'"),
                               selected = '"'),
                  
                  # Horizontal line ----
                  tags$hr(),
                  
                  # Input: Select number of rows to display ----
                  radioButtons("disp", "Display",
                               choices = c(Head = "head",
                                           All = "all"),
                               selected = "head"),
                  checkboxGroupInput("inCheckboxGroup",
                                     "Checkbox group input:",
                                     c("label 1" = "option1",
                                       "label 2" = "option2")),
                  uiOutput("choose_columns")
                  
                  
                ),
                box(
                  tableOutput("contents")
                ),
                box(
                  tableOutput("filetable")
                )
              ),
              fluidRow(
                h2("Stability Analysis"),
                box(
                  tableOutput("stabilityAnalysis"),
                  width = 12
                )
                
              )
      ),
      
      # Second tab content
      tabItem(tabName = "widgets",
              
              
              h2("Sample D3 Scatter plot (mtcars)"),
              fluidRow(
                box(
                  div(class="row",
                      div(class="col-md-12",
                          div(class="alert alert-warning alert-dismissible",
                              HTML('<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'),
                              HTML("<strong>What you can try here :</strong>
                                   <ul>
                                   <li>Zoom on the chart with the mousewheel</li>
                                   <li>Pan the plot</li>
                                   <li>Drag text labels</li>
                                   <li>Hover over a dot to display tooltips</li>
                                   <li>Hover over the color or symbol legends items</li>
                                   <li>Change data settings to see transitions</li>
                                   <li>Resize the window to test for responsiveness</li>
                                   <li>Try the lasso plugin with the toggle button or by using Shift+click</li>
                                   <li>Click on a dot to open its associated URL (doesn't work in RStudio's internal browser)</li>
                                   </ul>"))))
                              )
                              ),
              fluidRow(
                box(
                  sliderInput("scatterD3_nb", "Number of observations",
                              min = 3, max = nrow(mtcars), step = 1, value = nrow(mtcars)),
                  selectInput("scatterD3_x", "x variable :",
                              choices = c("Miles/(US) gallon" = "mpg",
                                          "Displacement (cu.in.)" = "disp",
                                          "Gross horsepower" = "hp",
                                          "Rear axle ratio" = "drat",
                                          "Weight (lb/1000)" = "wt",
                                          "1/4 mile time" = "qsec",
                                          "Number of cylinders (categorical)" = "cyl_cat"),
                              selected = "wt"),
                  checkboxInput("scatterD3_x_log", "Logarithmic x scale", value = FALSE),
                  selectInput("scatterD3_y", "y variable :",
                              choices = c("Miles/(US) gallon" = "mpg",
                                          "Displacement (cu.in.)" = "disp",
                                          "Gross horsepower" = "hp",
                                          "Rear axle ratio" = "drat",
                                          "Weight (lb/1000)" = "wt",
                                          "1/4 mile time" = "qsec",
                                          "Number of cylinders (categorical)" = "cyl_cat"),
                              selected = "mpg"),
                  checkboxInput("scatterD3_y_log", "Logarithmic y scale", value = FALSE),
                  selectInput("scatterD3_col", "Color mapping variable :",
                              choices = c("None" = "None",
                                          "Number of cylinders" = "cyl_cat",
                                          "V/S" = "vs",
                                          "Transmission" = "am",
                                          "Miles/(US) gallon" = "mpg",
                                          "Displacement (cu.in.)" = "disp",
                                          "Gross horsepower" = "hp",
                                          "Rear axle ratio" = "drat",
                                          "Weight (lb/1000)" = "wt",
                                          "1/4 mile time" = "qsec"),
                              selected = "cyl_cat"),
                  checkboxInput("scatterD3_ellipses", "Confidence ellipses", value = FALSE),
                  selectInput("scatterD3_symbol", "Symbol mapping variable :",
                              choices = c("None" = "None",
                                          "Number of cylinders" = "cyl_cat",
                                          "V/S" = "vs",
                                          "Transmission" = "am",
                                          "Number of forward gears" = "gear",
                                          "Number of carburetors" = "carb"),
                              selected = "am"),
                  selectInput("scatterD3_size", "Size mapping variable :",
                              choices = c("None" = "None",
                                          "Miles/(US) gallon" = "mpg",
                                          "Displacement (cu.in.)" = "disp",
                                          "Gross horsepower" = "hp",
                                          "Rear axle ratio" = "drat",
                                          "Weight (lb/1000)" = "wt",
                                          "1/4 mile time" = "qsec"),
                              selected = "hp"),
                  checkboxInput("scatterD3_threshold_line", "Arbitrary threshold line", value = FALSE),    
                  sliderInput("scatterD3_labsize", "Labels size :",
                              min = 5, max = 25, value = 11),
                  sliderInput("scatterD3_opacity", "Points opacity :", min = 0, max = 1, value = 1, step = 0.05),
                  checkboxInput("scatterD3_transitions", "Use transitions", value = TRUE),
                  tags$p(actionButton("scatterD3-reset-zoom", HTML("<span class='glyphicon glyphicon-search' aria-hidden='true'></span> Reset Zoom")),
                         actionButton("scatterD3-lasso-toggle", HTML("<span class='glyphicon glyphicon-screenshot' aria-hidden='true'></span> Toggle Lasso"), "data-toggle" = "button"),
                         tags$a(id = "scatterD3-svg-export", href = "#",
                                class = "btn btn-default", HTML("<span class='glyphicon glyphicon-save' aria-hidden='true'></span> Download SVG"))),
                  tags$ul(tags$li(tags$a(href = "https://github.com/juba/scatterD3", "scatterD3 on GitHub")),
                          tags$li(tags$a(href = "https://github.com/juba/scatterD3_shiny_app", "This app on GitHub")))
                ),
                box(scatterD3Output("scatterPlot", height = "700px"))
              ),
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
              
                              )
                      )
                )
      )

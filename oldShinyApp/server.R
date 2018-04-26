#
# This is the server logic of a Shiny web application. You can run the 
# application by clicking 'Run App' above.
#
# Find out more about building applications with Shiny here:
# 
#    http://shiny.rstudio.com/
#

library(shiny)
library(ggplot2)
library(shinydashboard)
library(scatterD3)
source("analysis.R")
source('loading.R')

options(shiny.maxRequestSize=500*1024^2)
dsnames <- c()
targetColumn <- c()
numericColumns <- c()
categoricalColumns <- c()

## mtcars data for sample scatter plot in D3
d <- mtcars
d$names <- rownames(mtcars)
d$cyl_cat <- paste(d$cyl, "cylinders")

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
  
  #This function is repsonsible for loading in the selected file
  filedata <- reactive({
    req(input$file1)
    infile <- input$file1
    if (is.null(infile)) {
      # User has not uploaded a file yet
      return(NULL)
    }
    read.csv(infile$datapath)
  })
  observe({
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
  output$toCol <- renderUI({
    df <-filedata()
    if (is.null(df)) return(NULL)
    
    items=names(df)
    names(items)=items
    selectInput("to", "To:",items)
    
  })
  
  output$fromCol <- renderUI({
    df <-filedata()
    if (is.null(df)) return(NULL)
    
    items=names(df)
    names(items)=items
    selectInput("from", "From:",items)
    
  })
  
  #The checkbox selector is used to determine whether we want an optional column
  output$amountflag <- renderUI({
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
  #output$filetable <- renderTable({
  #  filedata()
  #})
  
  ## render table for stability analysis 
  ## data for stability analysis
  stabilityData <- reactive({
    filedata()
    if (is.null(filedata())) return(NULL)
  })
  
  stabilityOutput <- reactive({
    result_col = "NonTeamCapture"
    numericCol <- c("vel_max_t_1",
                    "vel_max_predator",
                    "pitch_rate_max_predator",
                    "turn_rate_max_predator")
    categoryCol <- c("team_id",
                     "allow_prey_switching_t_2_predator")
    runStablilityCheck(stabilityData(), result_col, numericCol, categoryCol)
    
  })
  
  output$stabilityAnalysis <- renderTable({
    stabilityOutput()
  })
#   output$stabilityAnalysis <- renderTable({
#     # input$file1 will be NULL initially. After the user selects
#     # and uploads a file, head of that data file by default,
#     # or all rows if selected, will be shown.
#     
#     if(input$runStability){
#       req(input$file1)
#       
#       df <- read.csv(input$file1$datapath,
#                      header = input$header,
#                      sep = input$sep,
#                      quote = input$quote)
#       
#       result_col <- input$result_col
#       numericCol <- c("vel_max.t.1",
#                       "vel_max.predator",
#                       "pitch_rate_max.predator",
#                       "turn_rate_max.predator")
#       categoryCol <- c("team_id",
#                        "allow_prey_switching.t.2.predator")
#       
#       stabilityResults <- runStablilityCheck(df, result_col, numericCol, categoryCol)
#      
#      return(stabilityResults)
#    }
#    
#    return (c(0,0))
#    }
#    )
  
  ## output progress box increment (Main)
  output$progressBox <- renderInfoBox({
    infoBox(
      "Progress", paste0(25 + input$count, "%"), icon = icon("list"),
      color = "purple"
    )
  })
  
  ## output text message for sidebar selection
  output$res <- renderText({
    paste("You've selected:", input$tabs)
  })
  
  ## output plot of histogram (Main)
  set.seed(122)
  histdata <- rnorm(500)
  
  output$plot1 <- renderPlot({
    data <- histdata[seq_len(input$slider)]
    hist(data)
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
  
  ## output for D3 scatter plot using mtcars data
  data <- reactive({
    d[1:input$scatterD3_nb,]
  })
  
  lines <- reactive({
    if (input$scatterD3_threshold_line) {
      return(rbind(default_lines, threshold_line))
    }
    default_lines
  })
  
  output$scatterPlot <- renderScatterD3({
    col_var <- if (input$scatterD3_col == "None") NULL else data()[,input$scatterD3_col]
    symbol_var <- if (input$scatterD3_symbol == "None") NULL else data()[,input$scatterD3_symbol]
    size_var <- if (input$scatterD3_size == "None") NULL else data()[,input$scatterD3_size]
    scatterD3(x = data()[,input$scatterD3_x],
              y = data()[,input$scatterD3_y],
              lab = data()[,"names"],
              xlab = input$scatterD3_x,
              ylab = input$scatterD3_y,
              x_log = input$scatterD3_x_log,
              y_log = input$scatterD3_y_log,
              col_var = col_var,
              col_lab = input$scatterD3_col,
              ellipses = input$scatterD3_ellipses,
              symbol_var = symbol_var,
              symbol_lab = input$scatterD3_symbol,
              size_var = size_var,
              size_lab = input$scatterD3_size,
              url_var = paste0("https://www.duckduckgo.com/?q=", rownames(data())),
              key_var = rownames(data()),
              point_opacity = input$scatterD3_opacity,
              labels_size = input$scatterD3_labsize,
              transitions = input$scatterD3_transitions,
              left_margin = 90,
              lines = lines(),
              lasso = TRUE,
              caption = list(title = "Sample scatterD3 shiny app",
                             subtitle = "A sample application to show animated transitions",
                             text = "Yep, you can even use <strong>markup</strong> in caption text. <em>Incredible</em>, isn't it ?"),
              lasso_callback = "function(sel) {alert(sel.data().map(function(d) {return d.lab}).join('\\n'));}")
  })



  scatter <- callModule(scatter, "scatter", stringsAsFactors=False)
  output <- scatter()
}

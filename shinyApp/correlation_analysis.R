#https://shiny.rstudio.com/articles/modules.html

correlationAnalysisUI <- function(id, label = "Correlation Analysis"){
  ns <- NS(id)
  return (
    tagList(
      h2("Correlation Plot"),
      fluidRow(
        box(checkboxGroupInput(ns("categoricalCols"),
                               "Select Pertinent Categorical Variables",
                               c("Column names will show up" = "option1",
                                 "here post file load" = "option2"))),
        box(checkboxGroupInput(ns("numericCols"),
                               "Select Pertinent Categorical Variables",
                               c("Column names will show up" = "option1",
                                 "here post file load" = "option2"))),
        box(
          selectInput(ns('theTargetParam'), 'Select the target param', c("placeholder", "placeholder1")),
          actionButton(ns("runAnalysis"), "Run Correlation Analysis")
        ),
        verbatimTextOutput(ns("placeHolder"))
        
      ),
      fluidRow(
        box(plotOutput(ns('correlation'), width = "100%", height = "400px"))
      )
    )
  )
}

# Module server function
correlationAnalysis <- function(input, output, session, stringsAsFactors) {
  output$placeHolder <- renderText({
    cb_options <- list()
    cb_options[ session$userData$columnNames ] <- session$userData$columnNames
    updateCheckboxGroupInput(session, "categoricalCols",
                             label = "Select Pertinent Categorical Variables",
                             choices = cb_options,
                             selected = "")
    updateCheckboxGroupInput(session, "numericCols",
                             label = "Select Pertinent Numeric Variables",
                             choices = cb_options,
                             selected = "")
    updateSelectInput(session,"theTargetParam", label = "Target Variable", choices = cb_options, selected = cb_options[1])
    
    paste(sep = "",
          "protocol: ", session$clientData$url_protocol, "\n",
          "hostname: ", session$clientData$url_hostname, "\n",
          "pathname: ", session$clientData$url_pathname, "\n",
          "port: ",     session$clientData$url_port,     "\n",
          "search: ",   session$clientData$url_search,   "\n"
    )
  })
  
  observeEvent(input$runAnalysis, {
    cat(file=stderr(), "\nrunning correlation analysis with: ", session$userData$testText)
    cat(file=stderr(), "\nrunning correlation analysis with target val of : ", input$theTargetParam)
    cat(file=stderr(), "\nrunning correlation analysis with stabilityCategorical: ", input$categoricalCols)
    cat(file=stderr(), "\nrunning correlation analysis with stabilityNumeric: ", input$numericCols)
    
    correlationResultIntermed <- runCorrelation(session$userData$data_file, input$theTargetParam, input$numericCols, input$categoricalCols)
    print(correlationResultIntermed)

    output$correlation <- renderPlot({
      corrplot(correlationResultIntermed, method="circle")
    })
  })
}
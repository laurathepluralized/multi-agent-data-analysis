stabilityAnalysisUI <- function(id, label="Stability UI"){
  ns <- NS(id)
  
  tagList(
    fluidRow(
      box(checkboxGroupInput(ns("stabilityCategorical"),
                             "Select Pertinent Categorical Variables",
                             c("Column names will show up" = "option1",
                               "here post file load" = "option2"))),
      box(checkboxGroupInput(ns("stabilityNumeric"),
                             "Select Pertinent Numeric Variables",
                             c("Column names will show up" = "option1",
                               "here post file load" = "option2"))),
      box(
        selectInput(ns('theTargetParam'), 'Select the target param', c("placeholder", "placeholder1")),
        actionButton(ns("runStab"), "Run Stability Analysis")
      ),
      verbatimTextOutput(ns("placeHolder"))
      
    ),
    fluidRow(
      box(width=12,
          title = "Stability Analysis",
          column(12,
                 div(style = 'overflow-x: scroll', dataTableOutput(ns('stabilityAnalysisResult')))
          )
      )
    )
    
  )
}

stabilityAnalysis <- function(input, output, session, stringsAsFactors){
  
  output$placeHolder <- renderText({
    print(c(">stabilityAnalyCat:", session$userData$columnNamesCategoric()))
    cb_categorical_options <- list()
    cb_categorical_options[ session$userData$columnNamesCategoric() ] <- session$userData$columnNamesCategoric()
    updateCheckboxGroupInput(session, "stabilityCategorical",
                             label = "Select Pertinent Categorical Variables",
                             choices = cb_categorical_options,
                             selected = "")
    
    print(c(">stabilityAnalyNum:",session$userData$columnNamesNumeric()))
    cb_numerical_options <- list()
    cb_numerical_options[ session$userData$columnNamesNumeric() ] <- session$userData$columnNamesNumeric()
    updateCheckboxGroupInput(session, "stabilityNumeric",
                             label = "Select Pertinent Numeric Variables",
                             choices = cb_numerical_options,
                             selected = "")
    
    cb_options <- list()
    cb_options[ session$userData$columnNames ] <- session$userData$columnNames
    updateSelectInput(session,"theTargetParam", label = "Target Variable", choices = cb_options, selected = cb_options[1])
    
    paste(sep = "",
          "protocol: ", session$clientData$url_protocol, "\n",
          "hostname: ", session$clientData$url_hostname, "\n",
          "pathname: ", session$clientData$url_pathname, "\n",
          "port: ",     session$clientData$url_port,     "\n",
          "search: ",   session$clientData$url_search,   "\n"
    )
  })
  
  observeEvent(input$runStab, {
    cat(file=stderr(), "\nrunning stability analysis with: ", session$userData$testText)
    cat(file=stderr(), "\nrunning stability analysis with target val of : ", input$theTargetParam)
    cat(file=stderr(), "\nrunning stability analysis with stabilityCategorical: ", input$stabilityCategorical)
    cat(file=stderr(), "\nrunning stability analysis with stabilityNumeric: ", input$stabilityNumeric)
    
    stabilityResultIntermed <- runStablilityCheck(session$userData$data_file, input$theTargetParam, input$stabilityNumeric, input$stabilityCategorical)
    print(stabilityResultIntermed)
    output$stabilityAnalysisResult <- renderDataTable (
      stabilityResultIntermed
    )
  })
}

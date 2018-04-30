#modelling tab

# These are the types of modeling we can use in the modeling tab
models <- c('Multivariate linear regression',
            'Linear regression with AIC',
            'Principal component regression (using PCA)',
            'Partial least squares',
            'Random Forest Regression',
            'Neural networks')

modellingTabUI <- function(id, label = "Correlation Analysis"){
  ns <- NS(id)
  return (
    tagList(
      h2("Modeling"),
      selectInput(ns('model_to_use'), 'Select Which Model To Use', models),
      fluidRow(
        box(checkboxGroupInput(ns("categoricalCols"),
                               "Select Pertinent Categorical Variables",
                               c("Column names will show up" = "option1",
                                 "here post file load" = "option2"))),
        box(checkboxGroupInput(ns("numericCols"),
                               "Select Pertinent Numerical Variables",
                               c("Column names will show up" = "option1",
                                 "here post file load" = "option2"))),
        box(
          selectInput(ns('theTargetParam'), 'Select the target param', c("placeholder", "placeholder1")),
          actionButton(ns("runAnalysis"), "Fit Model to Data")
        ),
        verbatimTextOutput(ns("placeHolder"))
        
      ),
      plotOutput(ns("graph_modeling")),
      verbatimTextOutput(ns("info_modeling"))
    )
  )
}

# Module server function
modellingTab <- function(input, output, session, stringsAsFactors) {
  output$placeHolder <- renderText({
    cb_options <- list()
    cb_options[ session$userData$columnNames ] <- session$userData$columnNames
    updateSelectInput(session,"theTargetParam", label = "Target Variable", choices = cb_options, selected = cb_options[1])
    
    cb_categorical_options <- list()
    cb_categorical_options[ session$userData$columnNamesCategoric() ] <- session$userData$columnNamesCategoric()
    updateCheckboxGroupInput(session, "categoricalCols",
                             label = "Select Pertinent Categorical Variables",
                             choices = cb_categorical_options,
                             selected = "")
    
    cb_numerical_options <- list()
    cb_numerical_options[ session$userData$columnNamesNumeric() ] <- session$userData$columnNamesNumeric()
    updateCheckboxGroupInput(session, "numericCols",
                             label = "Select Pertinent Numeric Variables",
                             choices = cb_numerical_options,
                             selected = "")
    
    
    paste(sep = "",
          "protocol: ", session$clientData$url_protocol, "\n",
          "hostname: ", session$clientData$url_hostname, "\n",
          "pathname: ", session$clientData$url_pathname, "\n",
          "port: ",     session$clientData$url_port,     "\n",
          "search: ",   session$clientData$url_search,   "\n"
    )
  })
  
  observeEvent(input$runAnalysis, {
    cat(file=stderr(), "\nrunning model fitting analysis with: ", session$userData$testText)
    cat(file=stderr(), "\nrunning model fitting analysis with target val of : ", input$theTargetParam)
    cat(file=stderr(), "\nrunning model fitting analysis with stabilityCategorical: ", input$categoricalCols)
    cat(file=stderr(), "\nrunning model fitting analysis with stabilityNumeric: ", input$numericCols)
    
    # correlationResultIntermed <- runCorrelation(session$userData$data_file, input$theTargetParam, input$numericCols, input$categoricalCols)
    # print(correlationResultIntermed)
    
    model = run_modelingWithParams(session$userData$data_file, input$theTargetParam, input$numericCols, input$categoricalCols, which(models == input$model_to_use))
    
    
    # Writes the summary output of run_modeling
    # TODO: Get the summary working for all model types
    output$info_modeling <- renderPrint({
      #model = run_modeling(dsim, which(models == input$model_to_use))
      #input$model_to_use
      summary(model)
    })
    
    # Draws the graph output of the model from run_modeling
    # TODO: Get summaries and graphs working for all of the model types
    output$graph_modeling <- renderPlot({
      # model = run_modeling(dsim, which(models == input$model_to_use))
      model_selection = which(models == input$model_to_use)
      actual = result(session$userData$data_file,input$theTargetParam, input$numericCols, input$categoricalCols,model_selection)
      fitted = ypred(session$userData$data_file,input$theTargetParam, input$numericCols, input$categoricalCols,model_selection)
      if (model_selection == 1 | model_selection == 2) {
        par(mfrow=c(2,2))
        plot(model,which=c(1:4), col = "cornflowerblue")
      } else if(model_selection == 3) {
        # plot(model, xlab = "Actual values",
        #      ylab = "Predicted values", main = "Principal Component Regression predictions vs. actual"
        #      , col = "cornflowerblue")
        par(mfrow=c(2,2))
        plot(model,which=c(1:4), col = "cornflowerblue")
      } else if(model_selection == 4) {
        plot(actual, fitted, xlab = "Actual values",
             ylab = "Predicted values", main = "Partial Least Squares predictions vs. actual"
             , col = "cornflowerblue")
      } else if(model_selection == 5) {
        plot(actual, fitted, xlab = "Actual values",
             ylab = "Predicted values", main = "Random Forest Regression predictions vs. actual"
             , col = "cornflowerblue")
      } else {
        plot(actual, fitted, xlab = "Actual values",
             ylab = "Predicted values", main = "Neural Network predictions vs. actual"
             , col = "cornflowerblue")
      }
    })
    
  })
  
  #old code
  

  
}
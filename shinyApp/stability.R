stabilityAnalysisUI <- function(id, label="Stability UI"){
  ns <- NS(id)
  
  tagList(
    fluidRow(
      box(checkboxGroupInput(ns("stabilityCategorical"),
                             "Select Pertinent Categorical Variables",
                             c("Column names will show up" = "option1",
                               "here post file load" = "option2"))),
      box(checkboxGroupInput(ns("stabilityNumeric"),
                             "Select Pertinent Categorical Variables",
                             c("Column names will show up" = "option1",
                               "here post file load" = "option2"))),
      box(
        selectInput(ns('theTargetParam'), 'Select the target param', c("placeholder", "placeholder1")),
        actionButton(ns("runStab"), "Run Stability Analysis")
      )
      
    )
    
  )
}

stabilityAnalysis <- function(input, output, session, stringsAsFactors){
  
  observeEvent(input$runStab, {
    cat(file=stderr(), "running stability analysis with: ", session$userData$testText)
  })
  
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
    runStablilityCheck(session$userData$data_file, result_col, numericCol, categoryCol)
    
  })
  
  output$stabilityAnalysis <- renderTable ({
    stabilityOutput()
  })

}
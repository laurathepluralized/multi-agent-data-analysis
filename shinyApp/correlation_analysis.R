library(shiny)
library(shinydashboard)
source('analysis.R')
#https://shiny.rstudio.com/articles/modules.html

correlationAnalysisUI <- function(id, label = "Correlation Analysis"){
  ns <- NS(id)
  return (
    tagList(
      #put your usual ui stuff in here. You will also need to wrap all ids in ns("id_here")
      fluidRow(
        tags$h1("Correlation Analysis"),
        textOutput(ns("sessionText"), container = span),
        textInput('test', "input text"),
        textOutput(ns("textInputResult"), container = div)
      )
      
    )
  )
}

# Module server function
correlationAnalysis <- function(input, output, session, stringsAsFactors) {
  output$sessionText <- renderText({
    session$userData$testText
  })
  output$textInputResult <- renderText({
    input$teset
  })
}
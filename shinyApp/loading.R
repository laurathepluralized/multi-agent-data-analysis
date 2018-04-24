library(shiny)
library(shinydashboard)

handle_loading <- function(input, output, session) {
  
}

loading_ui <- function() {
  return (
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
                           c("Column names will show up" = "option1",
                             "label 2" = "option2")),
        uiOutput("choose_columns")
  )))
}
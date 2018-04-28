library(shiny)
library(shinydashboard)

handle_loading <- function(input, output, session) {
  cat(file=stderr(), "handle loading")
  return(
    output$loading_hp_data_file_preview <-  renderDataTable({
      cat(file=stderr(), "handle preview")
      
      # input$loading_hp_data_file will be NULL initially. After the user selects
      # and uploads a file, head of that data file by default,
      # or all rows if selected, will be shown.
      
      req(input$loading_hp_data_file)
      
      session$userData$data_file <- read.csv(input$loading_hp_data_file$datapath,
                                    header = input$loading_hp_data_file_header,
                                    sep = input$loading_hp_data_file_sep,
                                    quote = input$loading_hp_data_file_quote)
      
      populate_session_columns(session)
      
      session$userData$testText <- "testText"
      
      
      
      cat(file=stderr(), "options: ", session$userData$columnNames)
      cb_options <- list()
      cb_options[ session$userData$columnNames] <- session$userData$columnNames
      updateCheckboxGroupInput(session, "loading_hp_data_file_headers",
                               label = "Check Box Group",
                               choices = cb_options,
                               selected = "")
      
      updateSelectInput(session,"theTargetParam", label = "Target Variable", choices = cb_options, selected = cb_options[1])
      
      if(input$loading_hp_data_file_disp == "head") {
        return(head(session$userData$data_file))
      }
      else {
        return(session$userData$data_file)
      }
      
    })
    
  )
}

isInteger <- function(number){
  if(is.factor(number[1])) {
    return (FALSE)
  } else {
    if(is.numeric(number[1])){
      return (number[1]%%1==0)
    } else {
      return (FALSE)
    }
    
  }
}

initialize_session_columns <- function(session){
  session$userData$columnNamesCategoric <- reactiveVal()
  session$userData$columnNamesNumeric <- reactiveVal()
}

populate_session_columns <- function(session){
  print("loading column info")
  #print(session$userData$data_file)
  dsnames <- names(session$userData$data_file)
  print(c(">dsnames: ", dsnames))
  session$userData$columnNames <- dsnames[order(dsnames)]
  print(c(">session col names", session$userData$columnNames))
  
  #generate and store numeric column options
  numericColOptions <- dsnames[sapply(session$userData$data_file, is.numeric)]
  print(c(">numericColOptions:", numericColOptions))
  session$userData$columnNamesNumeric(numericColOptions)
  
  
  #generate and store categorical column options (integer and non numeric)
  nonNumericColOptions <- dsnames[!sapply(session$userData$data_file, is.numeric)]
  integerColOptions <- dsnames[sapply(session$userData$data_file, isInteger)]
  
  categoricalColOptions <- append(nonNumericColOptions, integerColOptions)
  session$userData$columnNamesCategoric(categoricalColOptions)
  
  print(c("columnNamesCategoric: ", session$userData$columnNamesCategoric ))
}

filedata_hp <- function(input){
  return (
    reactive({
      req(input$loading_hp_data_file)
      infile <- input$loading_hp_data_file
      if (is.null(infile)) {
        # User has not uploaded a file yet
        return(NULL)
      }
      read.csv(infile$datapath)
    })
  )
}

handle_displaying_column_options <- function(input, output, session){
  return (
    observe({
      req(input$loading_hp_data_file)
      dsnames <- names(filedata_hp(input))
      cb_options <- list()
      cb_options[ dsnames] <- dsnames
      updateCheckboxGroupInput(session, "loading_hp_data_file_headers",
                               label = "Check Box Group",
                               choices = cb_options,
                               selected = "")
    })
  )
}

loading_ui <- function() {
  return (
    fluidRow(
      box (
        # Input: Select a file ----
        fileInput("loading_hp_data_file", "Choose CSV File",
                  multiple = TRUE,
                  accept = c("text/csv",
                             "text/comma-separated-values,text/plain",
                             ".csv")),
        
        # Input: Checkbox if file has header ----
        checkboxInput("loading_hp_data_file_header", "Header", TRUE),
        
        # Input: Select separator ----
        radioButtons("loading_hp_data_file_sep", "Separator",
                     choices = c(Comma = ",",
                                 Semicolon = ";",
                                 Tab = "\t"),
                     selected = ","),
        
        # Horizontal line ----
        tags$hr(),
        
        # Input: Select quotes ----
        radioButtons("loading_hp_data_file_quote", "Quote",
                     choices = c(None = "",
                                 "Double Quote" = '"',
                                 "Single Quote" = "'"),
                     selected = '"')
  ), box(
    # Horizontal line ----
    tags$hr(),
    
    # Input: Select number of rows to display ----
    radioButtons("loading_hp_data_file_disp", "Display",
                 choices = c(Head = "head",
                             All = "all"),
                 selected = "head"),
    checkboxGroupInput("loading_hp_data_file_headers",
                       "Checkbox group input:",
                       c("Column names will show up" = "option1",
                         "here post file load" = "option2")),
    selectInput('theTargetParam', 'Select the target param', c("placeholder", "placeholder1"))
  )
  
  )
  )
}

preview_ui <- function(){
  return (
    fluidRow(
      # tableOutput("loading_hp_data_file_preview")
      box(width=12,
        column(12,
               div(style = 'overflow-x: scroll', dataTableOutput('loading_hp_data_file_preview'))
        )
      )
      
    )
  )
}
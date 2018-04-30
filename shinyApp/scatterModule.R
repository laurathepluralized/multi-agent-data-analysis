#https://shiny.rstudio.com/articles/modules.html

scatterIntegratedUI <- function(id, label = "Correlation Analysis"){
  ns <- NS(id)
  return (
    tagList(
      h2("Interactive Scatterplot"),
      
      fluidRow(
        box(
          selectInput(ns('theparamx'), 'Select parameter to plot on x-axis', c()),
          selectInput(ns('themetricy'), 'Select metric to plot on y-axis', c())
        ),
        box(
          uiOutput(ns('valuefixers'))
        )
      ),
      fluidRow(
        box(
          plotOutput(ns("plot_scatter"), click = "plot_click", brush = "plot_brush")
        ),
        verbatimTextOutput(ns("info_scatter"))
      ),
      verbatimTextOutput(ns("placeHolder"))
    )
  )
}

# Module server function
scatterIntegrated <- function(input, output, session, stringsAsFactors) {
  
  output$placeHolder <- renderText({
    
    cb_x_options <- list()
    cb_x_options[ session$userData$columnNamesReactive() ] <- session$userData$columnNamesReactive()
    updateSelectInput(session, 'theparamx', 'Select parameter to plot on x-axis', cb_x_options, cb_x_options[1])
    
    cb_y_options <- list()
    cb_y_options[ session$userData$columnNamesReactive() ] <- session$userData$columnNamesReactive()
    updateSelectInput(session, 'themetricy', 'Select parameter to plot on y-axis', cb_y_options, cb_y_options[1])
    
    paste(sep = "",
          "protocol: ", session$clientData$url_protocol, "\n",
          "hostname: ", session$clientData$url_hostname, "\n",
          "pathname: ", session$clientData$url_pathname, "\n",
          "port: ",     session$clientData$url_port,     "\n",
          "search: ",   session$clientData$url_search,   "\n"
    )
  })
  
  variables = reactiveValues(not_to_plot_params = c())
  
  observeEvent(input$theparamx, {
    variables$not_to_plot_params = session$userData$columnNames[session$userData$columnNames != input$theparamx]
  })
  
  #reactive({not_to_plot_params <- paramcols[paramcols != input$theparamx]})
  
  #print(variables$not_to_plot_params)
  #a <- 1
  #b <- 10
  mydata <- reactive({
    session$userData$reactiveData()[, c(input$theparamx, input$themetricy)]
  })
  
  
  output$valuefixers <- renderUI({
    valuefixers <- lapply(1:length(session$userData$columnNames), function(i) {
      thevals <- session$userData$reactiveData()[session$userData$columnNames[i]]
      inname <- names(thevals)
      # only make a variable-fixing element if this isn't the x-axis var
      if (inname != input$theparamx) {
        if (is.numeric(thevals[,1])) {
          themin = min(thevals[,1])
          themax = max(thevals[,1])
          stepsize <- 0.1
          uniquevals <- unique(thevals[,1])
          if (length(uniquevals) > 1 & length(uniquevals) < 10) {
            stepsize <- round(uniquevals[2], digits=-2) - round(uniquevals[1], digits=-2)
          }
          sliderInput(inname,
                      paste('Select fixed value for ', inname), min=themin,
                      max=themax, value=c(themin, themax), step = stepsize, round = -2)
        }
        else {
          pickvals <- unique(thevals[,1])
          selectInput(inname,
                      paste('Select fixed value for ', inname), pickvals)
        }
      }
    })
    do.call(tagList, valuefixers)
  })
  
  plotIsScatter <- TRUE
  output$plot_scatter<- renderPlot({
    data = session$userData$reactiveData() 
    to_plot = data
    # we are only looking at team 2 right now; obviously remove this next line
    # if your metrics cover multiple teams
    # TODO: add an input to select this?
    # to_plot = to_plot[which(to_plot['team_id'] == 2),]
    if (!is.null(to_plot)) {
      for (i in 1:length(variables$not_to_plot_params)) {
        param = variables$not_to_plot_params[i]
        vals = input[[param]]
        # If only one value is selected, make vals length 1 so it doesn't
        # try to filter by range
        if (length(vals) > 1) {
          if (min(vals) == max(vals)) {
            vals = min(vals)
          }
        }
        if (length(vals) == 1) {
          tempdf = to_plot[which(to_plot[param] == input[[param]]),]
          to_plot = tempdf
        } else if (length(vals) > 1) {
          # I'm getting the feeling R doesn't like doing df stuff in-place
          tempdf = to_plot[which(to_plot[param] >= min(vals)),]
          tempdf2 = tempdf[which(tempdf[param] <= max(vals)),]
          to_plot = tempdf2
        }
      }
      if (NROW(to_plot) > 0) {
        if (is.numeric(session$userData$reactiveData()[,input$theparamx])) {
          plot(to_plot[ c(input$theparamx, input$themetricy)])
          plotIsScatter <- TRUE
        } else {
          x = (to_plot[, c(input$theparamx)])
          y = (to_plot[, c(input$themetricy)])
          boxplot(y ~ x, xlab = input$theparamx, ylab = input$themetricy)
          plotIsScatter <- FALSE
        }
      }
      
      if (NROW(to_plot) > 0) {
        output$info_scatter <- renderPrint({
          # With base graphics, need to tell it what the x and y variables are.
          # Max of 10, otherwise we overload the user
          points <- brushedPoints(to_plot, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)
          head(points, 10)
        })
      }
    }
  })
  
}
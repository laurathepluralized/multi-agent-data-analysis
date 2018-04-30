# Interactive Plot tab

scatterboxUI <- function(id, label = "Interactive Plotting") {
  sbns <- NS(id)
  return (
    tagList(
      h2("Interactive Plotting"),
      fluidRow(
        box(checkboxGroupInput(sbns("categoricalCols"),
                               "Select Pertinent Categorical Variables",
                               c("Column names will show up" = "option1",
                                 "here post file load" = "option2"))),
        box(checkboxGroupInput(sbns("numericCols"),
                               "Select Pertinent Numerical Variables",
                               c("Column names will show up" = "option1",
                                 "here post file load" = "option2"))),
        box(
          selectInput(sbns('theTargetParam'), 'Select the target param',
                      c("placeholder", "placeholder1")),
          selectInput(sbns('theparamx'),
                      'Select parameter to plot on x-axis',
                      c('placeholder', 'anotherplaceholder')),
          actionButton(sbns("runAnalysis"), "Start plotting")
        ),
        verbatimTextOutput(sbns("select_vars_out"))
      ),
      fluidRow(
        box(
          uiOutput('valuefixers')
        )
      ),
      fluidRow(
        box(
          plotOutput("plot_scatter",
                     click = "plot_click", brush = "plot_brush")
        ),
        verbatimTextOutput("info_scatter")
      )
    )
  )
}

# server interactive plot stuff

scatterboxTab <- function(input, output, session, stringsAsFactors) {
  cat(file=stdout(), "\nnumerical parameter columns are ", session$userData$numParamCols)
  cat(file=stdout(), "\ncategorical parameter columns are ", session$userData$catParamCols)
  cat(file=stdout(), "\nTarget column is ", session$userData$targCols)

  paramcols = c()
  output$select_vars_out <- renderUI({
    cb_options <- list()
    cb_options[ session$userData$columnNames ] <- session$userData$columnNames

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

    updateSelectInput(session,"theTargetParam", label = "Target Variable",
                      choices = cb_options, selected = cb_options[1])

    paramcols = c(cb_categorical_options, cb_numerical_options)

    updateSelectInput(session,"theparamx",
                      label = "Parameter to Plot on x-axis",
                      choices = paramcols, selected = paramcols[1])
  })

  variables = reactiveValues(not_to_plot_params = c())

  observeEvent(input$theparamx, {
    variables$not_to_plot_params <- paramcols[paramcols != input$theparamx]
    print(variables$not_to_plot_params)
  })

  #a <- 1
  #b <- 10
  mydata <- reactive({
    session$userData$data_file[, c(input$theparamx, input$theTargetParam)]
  })


  output$valuefixers <- renderUI({
    valuefixers <- lapply(1:length(paramcols), function(i) {
      thevals <- session$userData$data_file[paramcols[i]]
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
          sliderInput(inname, paste('Select fixed value for ', inname),
                      min=themin, max=themax, value=c(themin, themax),
                      step = stepsize, round = -2)
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

  output$plot_scatter<- renderPlot({
    doPlotting(mydata, input$theTargetParam, input$theparamx,
               variables$not_to_plot_params,
               session$userData$numericCols,
               session$userData$categoryCols, input$plot_brush)
  })


  # plotIsScatter <- TRUE
  # output$plot_scatter<- renderPlot({
  #   data = session$userData$data_file
  #   to_plot = data
  #   # we are only looking at team 2 right now; obviously remove this next line
  #   # if your metrics cover multiple teams
  #   # TODO: add an input to select this?
  #   to_plot = to_plot[which(to_plot['team_id'] == 2),]
  #   if (!is.null(to_plot)) {
  #     for (i in 1:length(variables$not_to_plot_params)) {
  #       param = variables$not_to_plot_params[i]
  #       vals = input[[param]]
  #       # If only one value is selected, make vals length 1 so it doesn't
  #       # try to filter by range
  #       if (length(vals) > 1) {
  #         if (min(vals) == max(vals)) {
  #           vals = min(vals)
  #         }
  #       }
  #       if (length(vals) == 1) {
  #         tempdf = to_plot[which(to_plot[param] == input[[param]]),]
  #         to_plot = tempdf
  #       } else if (length(vals) > 1) {
  #         # I'm getting the feeling R doesn't like doing df stuff in-place
  #         tempdf = to_plot[which(to_plot[param] >= min(vals)),]
  #         tempdf2 = tempdf[which(tempdf[param] <= max(vals)),]
  #         to_plot = tempdf2
  #       }
  #     }
  #     if (NROW(to_plot) > 0) {
  #       if (is.numeric(session$userData$data_file[,input$theparamx][1])) {
  #         plot(to_plot[ c(input$theparamx, input$theTargetParam)])
  #         plotIsScatter <- TRUE
  #       } else {
  #         x = (to_plot[, c(input$theparamx)])
  #         y = (to_plot[, c(input$theTargetParam)])
  #         boxplot(y ~ x, xlab = input$theparamx, ylab = input$theTargetParam)
  #         plotIsScatter <- FALSE
  #       }
  #     }

  #     if (NROW(to_plot) > 0) {
  #       output$info_scatter <- renderPrint({
  #         # With base graphics, need to tell it what the x and y variables are.
  #         # Max of 10, otherwise we overload the user
  #         points <- brushedPoints(to_plot, input$plot_brush, xvar = input$theparamx, yvar = input$theTargetParam)
  #         head(points, 10)
  #       })
  #     }
  #   }
  # })
  

}



doPlotting <- function(mydata, result_col, x_val_col, not_to_plot_params, numericCols, categoryCols, brushed) {
    data = mydata
    to_plot = data
    # we are only looking at team 2 right now; obviously remove this next line
    # if your metrics cover multiple teams
    # TODO: add an input to select this?
    to_plot = to_plot[which(to_plot['team_id'] == 2),]
    if (!is.null(to_plot)) {
      for (i in 1:length(not_to_plot_params)) {
        param = not_to_plot_params[i]
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
        if (x_val_col %in% categoryCols) {
          x = (to_plot[, c(x_val_col)])
          y = (to_plot[, c(result_col)])
          boxplot(y ~ x, xlab = x_val_col, ylab = result_col)
        } else {
          plot(to_plot[ c(x_val_col, result_col)])
        }
        output$info_scatter <- renderPrint({
          # With base graphics, need to tell it what the x and y variables are.
          # Max of 10, otherwise we overload the user
          points <- brushedPoints(to_plot,
                                  brushed,
                                  xvar = x_val_col,
                                  yvar = result_col)
          head(points, 10)
        })
      }
    }
}


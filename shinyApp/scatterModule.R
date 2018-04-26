#https://shiny.rstudio.com/articles/modules.html

scatterIntegratedUI <- function(id, label = "Correlation Analysis"){
  ns <- NS(id)
  return (
    tagList(
      h2("Interactive Scatterplot"),
      
      fluidRow(
        selectInput(ns('theparamx'), 'Select parameter to plot on x-axis', names(dsim[paramcols])),
        selectInput(ns('themetricy'), 'Select metric to plot on y-axis', names(dsim[metriccols])),
        uiOutput(ns('valuefixers')),
        plotOutput(ns("plot_scatter)", click = "plot_click", brush = "plot_brush"),
        verbatimTextOutput(ns("info_scatter"))
      )
      )
    )
  )
}

# Module server function
scatterIntegrated <- function(input, output, session, stringsAsFactors) {
  variables = reactiveValues(not_to_plot_params = c())
  
  observeEvent(input$theparamx, {
    variables$not_to_plot_params = paramcols[paramcols != input$theparamx]
  })
  plotIsScatter <- TRUE
  output$plot_scatter<- renderPlot({
    dsim = session$userData$data_file
    data = dsim 
    to_plot = data
    for (i in 1:length(variables$not_to_plot_params)) {
      param = variables$not_to_plot_params[i]
      to_plot = to_plot[which(to_plot[param] == input[[param]]),]
    }
    if (is.numeric(dsim[,input$theparamx][1])) {
      plot(to_plot[ c(input$theparamx, input$themetricy)])
      plotIsScatter <- TRUE
    } else {
      x = (to_plot[, c(input$theparamx)])
      y = (to_plot[, c(input$themetricy)])
      boxplot(y ~ x, xlab = input$theparamx, ylab = input$themetricy)
      plotIsScatter <- FALSE
    }
    plot(to_plot[, c(input$theparamx, input$themetricy)])
  })
  
  output$info_scatter <- renderPrint({
    # With base graphics, need to tell it what the x and y variables are.
    # Max of 10, otherwise we overload the user
    points <- brushedPoints(dsim, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)
    head(points, 10)
  })
  
}
# https://shiny.rstudio.com/articles/modules.html
library(shiny)
library(ggplot2)
library(dplyr)




#############################
#Scatterplot Code
#options(shiny.maxRequestSize=1000*1024^2)
dsimcsv <- read.csv('./../shinyApp/data/betterdata.csv', stringsAsFactors = FALSE, header=TRUE)
dsimcsv <- dsimcsv[dsimcsv$team_id!=1,]  # our metrics for this data only involve team 2
storefile <- file.path('./', 'data.rds')
saveRDS(dsimcsv, file = storefile)
dsim <- readRDS(storefile)
paramcols <- c('max_speed_t_1','turn_rate_max_t_1','turn_rate_max_predator','vel_max_predator','allow_prey_switching_t_2_predator')
metriccols <- c('NonTeamCapture')


scatterInput <- function(id, label="scatter_viz") {
    ns <- NS(id)

    tagList(
        selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim[paramcols])),
        selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim[metriccols])),
        uiOutput('valuefixers'),
        # remainingparams = select(dsim, -c('themetricy', 'theparamx'))
        plotOutput("plot1", click = "plot_click", brush = "plot_brush"),
        verbatimTextOutput("info")
    )
}

scatter <- function(input, output, session, stringAsFactors) {
    variables = reactiveValues(not_to_plot_params = c())

    observeEvent(input$theparamx, {
        variables$not_to_plot_params = paramcols[paramcols != input$theparamx]
    })

    #reactive({not_to_plot_params <- paramcols[paramcols != input$theparamx]})

    #print(variables$not_to_plot_params)
    #a <- 1
    #b <- 10
    mydata <- reactive({
        dsim[, c(input$theparamx, input$themetricy)]
    })


    output$valuefixers <- renderUI({
        valuefixers <- lapply(1:length(paramcols), function(i) {
            thevals <- dsim[paramcols[i]]
            inname <- names(thevals)
            if (inname != input$theparamx) {
                if (is.numeric(thevals[1])) {
                    themin = min(thevals)
                    themax = max(thevals)
                    middle = 0.5*(themin + themax)
                    # only make a slider if this isn't the x-axis variable
                    sliderInput(inname, inname, min=themin, max=themax, value=middle, post="%")
                }
                else {
                    pickvals <- unique(thevals)
                    selectInput(inname, inname, pickvals)
                }
            }
        })
        do.call(tagList, valuefixers)
    })

    #observeEvent(input$valuefixers

    output$plot1 <- renderPlot({
        data = dsim 
        to_plot = data
        for (i in 1:length(variables$not_to_plot_params)) {
            param = variables$not_to_plot_params[i]

            # the below lines are for debugging if something goes wrong
            #print('=========================')
            #print(param)
            #print(to_plot[param][1,1])
            #
            
            to_plot = to_plot[which(to_plot[param] == input[[param]]),]
        }
        
        
        plot(to_plot[, c(input$theparamx, input$themetricy)])
    })

    output$info <- renderPrint({
        # With base graphics, need to tell it what the x and y variables are.
        # Max of 10, otherwise we overload the user
        points <- brushedPoints(dsim, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)
        if (nrow(points) >= 10) {
            points[1:10,]
        } else {
            points
        }
    })
    
    #Leaving this as an output debugger that updates that we can look at
    #output$info <- renderPrint({
        ##variables$not_to_plot_params[1]
        ##dsimcsv[which(dsimcsv[variables$not_to_plot_params[1]] == input[[variables$not_to_plot_params[1]]]),]
    #})

}


##############################

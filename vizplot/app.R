





library(shiny)
library(ggplot2)
library(dplyr)

#options(shiny.maxRequestSize=1000*1024^2)
dsimcsv <- read.csv('./../shinyApp/data/betterdata.csv', stringsAsFactors = FALSE, header=TRUE)
browser()
dsimcsv <- dsimcsv[dsimcsv$team_id!=1,]  # our metrics for this data only involve team 2
storefile <- file.path('./', 'data.rds')
saveRDS(dsimcsv, file = storefile)
dsim <- readRDS(storefile)
paramcols <- c('max_speed_t_1','turn_rate_max_t_1','turn_rate_max_predator','vel_max_predator','allow_prey_switching_t_2_predator')
metriccols <- c('NonTeamCapture')


# Given the x and y param and metric we want to plot, and a list containing
# the the other params and values that they are fixed at to plot, plot only
# the rows in which all of the other params are at those fixed values


ui <- fluidPage(
    selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim[paramcols])),
    selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim[metriccols])),
    uiOutput('sliders'),
    # remainingparams = select(dsim, -c('themetricy', 'theparamx'))
    plotOutput("plot1", click = "plot_click", brush = "plot_brush"),
    verbatimTextOutput("info")
)

server <- function(input, output, session) {

    #a <- 1
    #b <- 10
    mydata <- reactive({
        dsim[, c(input$theparamx, input$themetricy)]
    })


    output$sliders <- renderUI({
        sliders <- lapply(1:length(paramcols), function(i) {
            inname <- paramcols[i]
            sliderInput(inname, inname, min=10, max=50, value=25, post="%")
        })
        do.call(tagList, sliders)
    })

    output$plot1 <- renderPlot({
        plot(mydata())
    })

    output$info <- renderPrint({
        # With base graphics, need to tell it what the x and y variables are.
        # Max of 10, otherwise we overload the user
        points <- brushedPoints(dsim, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)
        if (nrow(points) >= 10) {
            points[0:10,]
        } else {
            points
        }
    })
    #output$info <- renderPrint(a)

}

shinyApp(ui, server)



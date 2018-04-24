





library(shiny)
library(ggplot2)
library(dplyr)

#options(shiny.maxRequestSize=1000*1024^2)
dsimcsv <- read.csv('./../shinyApp/data/alldata2.csv', stringsAsFactors = FALSE, header=TRUE)
storefile <- file.path('./', 'data.rds')
saveRDS(dsimcsv, file = storefile)
dsim <- readRDS(storefile)
paramcols <- c('avoid_nonteam_weight_t_1','avoid_team_weight_t_1', 'max_speed_t_1', 'max_pred_speed_t_2_predator')
metriccols <- c('NonTeamCapture')

ui <- fluidPage(
    selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim[paramcols])),
    selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim[metriccols])),
    uiOutput('sliders'),
    # remainingparams = select(dsim, -c('themetricy', 'theparamx'))
    plotOutput("plot1", click = "plot_click", brush = "plot_brush"),
    verbatimTextOutput("info")
)

server <- function(input, output, session) {

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
        brushedPoints(dsim, input$plot_brush, xvar = input$theparamx, yvar = input$themetricy)[1:10,]
    })

}

shinyApp(ui, server)



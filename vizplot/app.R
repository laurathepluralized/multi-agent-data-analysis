





library(shiny)
library(ggplot2)

#options(shiny.maxRequestSize=1000*1024^2)
dsimcsv <- read.csv('./../shinyApp/data/alldata2.csv', stringsAsFactors = FALSE, header=TRUE)
storefile <- file.path('./', 'data.rds')
saveRDS(dsimcsv, file = storefile)
dsim <- readRDS(storefile)

ui <- fluidPage(


    selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim)),
    selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim)),
    selectInput('thefixed', 'Select parameters to fix', names(dsim)),
    plotOutput("plot1", click = "plot_click", brush = "plot_brush"),
    verbatimTextOutput("info")
)

server <- function(input, output, session) {

    mydata <- reactive({
        dsim[, c(input$theparamx, input$themetricy)]
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



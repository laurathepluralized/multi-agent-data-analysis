





library(shiny)
library(ggplot2)

dsim <- read.csv('./shinyApp/data/alldata2.csv', stringsAsFactors = FALSE, header=TRUE)

ui <- fluidPage(
    selectInput('theparamx', 'Select parameter to plot on x-axis', names(dsim)),
    selectInput('themetricy', 'Select metric to plot on y-axis', names(dsim)),
    selectInput('thefixed', 'Select parameters to fix', names(dsim)),
    plotOutput("plot1", click = "plot_click"),
    verbatimTextOutput("info")
)

server <- function(input, output, session) {
    mydata <- reactive({dsim[, c(input$theparamx, input$themetricy)]})
    print(mydata)
  
    output$plot1 <- renderPlot({
        plot(mydata())
    })

    output$info <- renderText({
        paste0("x=", input$plot_click$x, "\ny=", input$plot_click$y)
    })
}

shinyApp(ui, server)



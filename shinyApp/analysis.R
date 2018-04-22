library(data.table)
library(sqldf)
library(plyr)
library(dplyr)
library(readxl)
library(datasets)
library(corrplot)
library(data.table)
library(ggplot2)


runStablilityCheck <- function(data, result_col, numericCol, categoryCol) {
  colnames(data2)[colnames(data2) == result_col] <- 'result'
  data <- data2
  
  ##read useful varaibles only (specified above)
  mydata <- cbind(data$result, data[,numericCol] , data[,categoryCol])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'
  
  ##Clean missing data
  mydata <- na.omit(mydata)
  mydataOriginal <- mydata
  mydata <- mydata[mydata$team_id > 0,]
  
  lastCol = length(mydata[1])
  for(i in 1:length(mydata$result)){
    mydata$predictors[i] = paste(mydata[i,2:lastCol],  collapse=" ")
  }
  combinations <- levels(factor(mydata$predictors))
  
  stabilityTable2 <- ddply(mydata, cbind(numericCol, categoryCol), summarise,
                           N    = length(result),
                           mean = mean(result),
                           sd   = sd(result),
                           cov   = sd(result) / mean(result)  )
  return (stabilityTable2[order(stabilityTable2$cov),])
} 
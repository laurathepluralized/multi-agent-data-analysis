library(data.table)
library(sqldf)
library(plyr)
library(dplyr)
library(readxl)
library(datasets)
library(corrplot)
library(randomForest)


runCorrelation <- function(data, result_col,  numericCols, categoryCols){
  numericCol <- numericCols
  categoryCol <- categoryCols
  
  ##specify response variable (y) and name it result
  colnames(data)[colnames(data) == result_col] <- 'result'
  numericCol <- numericCols
  categoryCol <- categoryCols
  
  ##read useful varaibles only (specified above)
  mydata <- cbind(data$result, data[,numericCol] , data[,categoryCol])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'
  
  ##Clean missing data
  mydata <- na.omit(mydata)
  mydataOriginal <- mydata
  
  ##Drop columns of no information (only one value exists)
  NumericColToAdd <- c()
  for(col in numericCol ){
    if(sd(mydata[,col]) > 0){
      NumericColToAdd <- cbind(NumericColToAdd, col)
    }
  }
  CatColToAdd <- c()
  for(col in categoryCol ){
    temp <- levels(factor(mydata[,col]))
    if( length(temp[temp != ""]) > 1){
      CatColToAdd <- cbind(CatColToAdd, col)
    }
  }
  
  mydata <- cbind(mydata$result, mydata[,NumericColToAdd] , mydata[,CatColToAdd])
  colnames(mydata) <- cbind("result", NumericColToAdd, CatColToAdd)
  
  
  ####Basic Correlation analysis for numeric varibles
  Corr <- cor(mydata[,cbind("result", NumericColToAdd)])
  
  return(Corr)
  
}

runStablilityCheck <- function(data2, result_col, numericCols, categoryCols) {
  numericCol <- numericCols
  categoryCol <- categoryCols
  colnames(data2)[colnames(data2) == result_col] <- 'result'
  data <- data2
  
  ##read useful variables only (specified above)
  mydata <- cbind(data$result, data[,numericCol] , data[,categoryCol])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'
  
  ##Clean missing data
  mydata <- na.omit(mydata)
  mydataOriginal <- mydata
  
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

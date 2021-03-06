# This is the file that has the function run_modeling that is called in app.R
# for the modeling tab
library(data.table)
library(sqldf)
library(dplyr)
library(plyr)
library(readxl)
library(datasets)
library(corrplot)
library(data.table)
library(ggplot2)
library(caTools)


# The function
run_modeling <- function(data2) {
  result_col = "NonTeamCapture"
  
  ##specify response variable (y) and name it result
  colnames(data2)[colnames(data2) == result_col] <- 'result'
  data <- data2
  
  dataTypes <- sapply(data, class)
  resultCol <- data['result']
  
  # Variables varied: {values they had}
  # max_speed_t_1 = vel_max_t_1: {40 50}
  # max_speed_t_2_predator = vel_max_t_2: {10 20 30 40 50}
  # turn_rate_max_t_1: {0.25 0.5 0.75}
  # turn_rate_max_predator: {0.25 0.5 0.75}
  # allow_prey_switching_t_2_predator: {True False}
  # 180 combinations
  
  ########provide column names of numeric variables
  numericCol <- c("vel_max_t_1",
                  "vel_max_predator",
                  "pitch_rate_max_predator",
                  "turn_rate_max_predator")
  # numericCol <- colnames(data[sapply(data, is.numeric)])
  
  ########provide column names of category variables
  categoryCol <- c("team_id",
                   "allow_prey_switching_t_2_predator")
  # categoryCol <- 
  
  ##read useful varaibles only (specified above)
  mydata <- cbind(data$result, data[,numericCol] , data[,categoryCol])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'
  
  ##Clean missing data
  mydata <- na.omit(mydata)
  
  # Part 1: Multivariate regerssion
  boxcox = FALSE
  
  if(boxcox){
    bc <- boxcox(result ~ ., data = mydata, lambda = seq(-1, 1, length = 10))
    lambda <- bc$x[which.max(bc$y)]
    
    mydata$result_old <- mydata$result
    mydata$result <- NULL
    mydata$result <- (mydata$result_old^lambda-1)/lambda
  }
  
  dataset = mydata
  split = sample.split(mydata$result, SplitRatio = 2/3)
  training_set = subset(dataset, split == TRUE)
  test_set = subset(dataset, split == FALSE)
  
  # Linear regression with backward selection
  lmtest <- lm(result ~ . , data=training_set)
  lmSummary <- summary(lmtest)
  pValues <- t(coef(lmSummary)[,4])
  varNames <- colnames(pValues)
  NewVar <- c()
  for(i in 2:length(varNames)){
    if(pValues[i] < 0.05){
      NewVar <- cbind(NewVar, varNames[i])
    }
  }
  backward = length(varNames) - 1 - length(NewVar)
  while(backward > 0){
    backward = length(NewVar)
    if(length(NewVar) > 0){
      NextData <- training_set[, cbind("result", NewVar)]
      lmtest2 <- lm(result ~ . , data=NextData)
      lmSummary2 <- summary(lmtest2)
      pValues <- t(coef(lmSummary2)[,4])
      varNames <- colnames(pValues)
      NewVar <- c()
      for(i in 2:length(varNames)){
        if(pValues[i] < 0.05){
          NewVar <- cbind(NewVar, varNames[i])
        }
      }
      backward = backward - length(NewVar)
    }
  }
  y_pred<- predict(lmtest2,test_set)
  
  summary(model)
  par(mfrow=c(2,2))#drawing in 2 by 2 format
  plot(model,which=c(1:4), col = "cornflowerblue")
  plot(model$fitted.values, model$residuals)
  return(model)
}
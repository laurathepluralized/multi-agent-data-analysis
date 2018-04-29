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
library(pls)
library(neuralnet)


run_modeling <- function(data2, model_selection) {
  result_col = "NonTeamCapture"
  
  # Variables varied: {values they had}
  # max_speed_t_1: {25}
  # max_speed_t_2_predator: {30 40 50}
  # turn_rate_max_t_1: {0.25 0.5 0.75 1.0 1.25 1.5}
  # turn_rate_max_predator: {0.5}
  # allow_prey_switching_t_2_predator: {True False}
  
  ########provide column names of numeric variables
  numericCol <- c("turn_rate_max_t_1",
                  "max_speed_t_2_predator",
                  "allow_prey_switching_t_2_predator")
  # numericCol <- colnames(data[sapply(data, is.numeric)])
  
  categoryCol <- c("team_id",
                   "allow_prey_switching_t_2_predator")
  
  model = run_modelingWithParams(data2, result_col, numericCol, categoryCol , model_selection)
  
  return (model)
}

# The function
run_modelingWithParams <- function(data2, result_col, numericCol, categoryCol , model_selection) {
  

##specify response variable (y) and name it result
  colnames(data2)[colnames(data2) == result_col] <- 'result'
  data <- data2

  dataTypes <- sapply(data, class)
  resultCol <- data['result']


########provide column names of category variables
  

##read useful variables only (specified above)
  mydata <- cbind(data$result, data[ append(numericCol, categoryCol)])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'

##Clean missing data
  mydata <- na.omit(mydata)
  mydata <- mydata[mydata$team_id > 1,]
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


# Fitting the model to the training set
  if (model_selection == 1) { # Multivarite linear regression model
    
    
    model = lm(formula = result ~., data = training_set)
    y_pred<- predict(model,test_set)
    
  } else if(model_selection == 2) { # Linear regression with backward selection
    lmtest <- lm(result ~ . , data=training_set)
    model <- step(lmtest)
    y_pred<- predict(model,test_set)
    
  } else if (model_selection == 3) { # Principal component regresson
    require(pls)
    model = pcr(result~., data = training_set, ncomp=dim(training_set)[2]-1, validation="CV")
    pcrCV<- RMSEP(model,estimate = "CV")
    #plot(pcrCV, main(""))
    param_num<-which.min(pcrCV$val)
    y_pred = predict(model,test_set,ncop = param_num)
  } else if (model_selection == 4) { # Partial least squares
    model = plsr(result~., data = training_set, ncomp = dim(training_set)[2]-1, validation ="CV")
    plsCV<- RMSEP(model, estimate = "CV")
    #plot(plsCV, main = "")
    param_num <- which.min(plsCV$val)
    y_pred = predict(model,test_set,ncomp = param_num)
  } else if (model_selection == 5) { # Random Forest Regression
    library(randomForest)
    model = randomForest(x = training_set[,-1],y = training_set$result, subset = training_set, ntree = 20)
    y_pred <- predict(model,test_set)
  } else if (model_selection == 6) { #  Neural networks
    require(nnet)
    model = nnet(result~., data = training_set,size = 10)
    y_pred<- predict(model,test_set)
  }

# The function returns the model so that we can access whatever we need in the
# app.R file for the visualization
  return(model)
#summary(model)
#(mean((y_pred-test_set$result)^2))
#par(mfrow=c(2,2))#drawing in 2 by 2 format
#plot(model,which=c(1:4), col = "cornflowerblue")
#plot(model$fitted.values, model$residuals)
}












###### fitted values
ypred <- function(data2, model_selection) {
  result_col = "NonTeamCapture"
  
  ##specify response variable (y) and name it result
  colnames(data2)[colnames(data2) == result_col] <- 'result'
  data <- data2
  
  dataTypes <- sapply(data, class)
  resultCol <- data['result']
  
  # Variables varied: {values they had}
  # max_speed_t_1: {25}
  # max_speed_t_2_predator: {30 40 50}
  # turn_rate_max_t_1: {0.25 0.5 0.75 1.0 1.25 1.5}
  # turn_rate_max_predator: {0.5}
  # allow_prey_switching_t_2_predator: {True False}
  
  ########provide column names of numeric variables
  numericCol <- c("turn_rate_max_t_1",
                  "max_speed_t_2_predator",
                  "allow_prey_switching_t_2_predator")
  # numericCol <- colnames(data[sapply(data, is.numeric)])
  
  ########provide column names of category variables
  categoryCol <- c("team_id",
                   "allow_prey_switching_t_2_predator")
  
  ##read useful variables only (specified above)
  mydata <- cbind(data$result, data[,numericCol] , data[,categoryCol])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'
  
  ##Clean missing data
  mydata <- na.omit(mydata)
  mydata <- mydata[mydata$team_id > 1,]
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
  
  
  # Fitting the model to the training set
  if (model_selection == 1) { # Multivariate linear regression model
    model = lm(formula = result ~., data = training_set)
    y_pred<- predict(model,test_set)
    
  } else if(model_selection == 2) { # Linear regression with backward selection
    lmtest <- lm(result ~ . , data=training_set)
    model <- step(lmtest)
    y_pred<- predict(model,test_set)
    
  } else if (model_selection == 3) { # Principal component regresson
    require(pls)
    model = pcr(result~., data = training_set, ncomp=dim(training_set)[2]-1, validation="CV")
    pcrCV<- RMSEP(model,estimate = "CV")
    #plot(pcrCV, main(""))
    param_num<-which.min(pcrCV$val)
    y_pred = predict(model,test_set,ncop = param_num)
  } else if (model_selection == 4) { # Partial least squares
    model = plsr(result~., data = training_set, ncomp = dim(training_set)[2]-1, validation ="CV")
    plsCV<- RMSEP(model, estimate = "CV")
    #plot(plsCV, main = "")
    param_num <- which.min(plsCV$val)
    y_pred = predict(model,test_set,ncomp = param_num)
  } else if (model_selection == 5) { # Random Forest Regression
    library(randomForest)
    model = randomForest(x = training_set[,-1],y = training_set$result, subset = training_set, ntree = 20)
    y_pred <- predict(model,test_set)
  } else if (model_selection == 6) { #  Neural networks
    require(nnet)
    model = nnet(result~., data = training_set,size = 2)
    y_pred<- predict(model,test_set)
  }
  return(y_pred)
}


###### actual values
result <- function(data2, model_selection) {
  result_col = "NonTeamCapture"
  
  ##specify response variable (y) and name it result
  colnames(data2)[colnames(data2) == result_col] <- 'result'
  data <- data2
  
  dataTypes <- sapply(data, class)
  resultCol <- data['result']
  
  # Variables varied: {values they had}
  # max_speed_t_1: {25}
  # max_speed_t_2_predator: {30 40 50}
  # turn_rate_max_t_1: {0.25 0.5 0.75 1.0 1.25 1.5}
  # turn_rate_max_predator: {0.5}
  # allow_prey_switching_t_2_predator: {True False}
  
  ########provide column names of numeric variables
  numericCol <- c("turn_rate_max_t_1",
                  "max_speed_t_2_predator",
                  "allow_prey_switching_t_2_predator")
  # numericCol <- colnames(data[sapply(data, is.numeric)])
  
  ########provide column names of category variables
  categoryCol <- c("team_id",
                   "allow_prey_switching_t_2_predator")
  
  ##read useful variables only (specified above)
  mydata <- cbind(data$result, data[,numericCol] , data[,categoryCol])
  colnames(mydata)[colnames(mydata) == 'data$result'] <- 'result'
  
  ##Clean missing data
  mydata <- na.omit(mydata)
  mydata <- mydata[mydata$team_id > 1,]
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
  return(test_set$result)
}

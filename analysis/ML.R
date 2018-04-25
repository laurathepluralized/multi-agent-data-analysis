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
run_modeling <- function(data2, model_selection) {
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
  
  # Machine learning Model
  # 1. Principal component regresson
  # 2. Partial Least Squares
  # 3. Random Forest Regression
  # 4. Neural networks
  
  dataset = mydata
  split = sample.split(mydata$result, SplitRatio = 2/3)
  training_set = subset(dataset, split == TRUE)
  test_set = subset(dataset, split == FALSE)
  
  
  # Fitting the model to the training set
  if (model_selection == 1) { # Principal component regresson
    require(pls)
    model = pcr(result~., data = training_set, ncomp=dim(training_set)[2]-1, validation="CV")
    pcrCV<- RMSEP(model,estimate = "CV")
    plot(pcrCV, main(""))
    param_num<-which.min(pcrCV$val)
    y_pred = predict(model,test_set,ncop = param_num)
  } else if (model_selection == 2) { # Partial least squares
    model = plsr(result~., data = training_set, ncomp = dim(training_set)[2]-1, validation ="CV")
    plsCV<- RMSEP(model, estimate = "CV")
    plot(plsCV, main = "")
    param_num <- which.min(plsCV$val)
    y_pred = predict(model,test_set,ncomp = param_num)
  } else if (model_selection == 3) { # Random Forest Regression
    library(randomForest)
    model = randomForest(x = training_set[,-1],y = training_set$result, subset = training_set, ntree = 20)
    y_pred <- predict(model,test_set)
  } else if (model_selection == 4) { #  Neural networks
    require(nnet)
    model = nnet(result~., data = training_set,size = 2)
    y_pred<- predict(model,test_set)
  }
  # Root mean squared error for model evaluation
  (mean((y_pred-test_set$result)^2))
  
  # The function returns the model so that we can access whatever we need in the
  # app.R file for the visualization
  return(model)
  
}

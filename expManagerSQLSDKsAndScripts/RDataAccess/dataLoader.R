library(httr)

getData <- function() {

}

authenticate <- function(username, password, serverDomain) {
    data=list(j_username = username, j_password = password, 'submit' = 'Login')
     initialCallResponse <- GET(paste(serverDomain, "/#", sep=""), verbose())
     authRes <- POST(
       paste(serverDomain, '/api/authentication', sep=""), 
       add_headers('X-XSRF-TOKEN' = cookies(initialCallResponse)[1,'value']),
       body = data,
       encode = "form",
       verbose()
       )
     return(authRes)
}

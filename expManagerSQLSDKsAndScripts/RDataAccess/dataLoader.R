library(httr)

getData <- function(serverDomain, path, params, authCall) {
  dataCall <- GET(url = paste(serverDomain, path, sep=""),
                  add_headers('X-XSRF-TOKEN' = cookies(authCall)[1,'value']),
                  query = params,
                  )
  return(dataCall)
}

# authResp <- authenticate("admin","admin", serverDomain)
authenticate <- function(username, password, serverDomain) {
    data=list(j_username = username, j_password = password, 'submit' = 'Login')
     initialCallResponse <- GET(paste(serverDomain, "/#", sep=""))
     authRes <- POST(
       paste(serverDomain, '/api/authentication', sep=""), 
       add_headers('X-XSRF-TOKEN' = cookies(initialCallResponse)[1,'value']),
       body = data,
       encode = "form"
       )
     return(authRes)
}

rapi
====

rapi

This is an example REST API written in Java and using a combination of Spring as the IoC and Jersey as 
an implementation of the JAX-RS API.

Build using Maven:
    mvn install
    
You can deploy the resulting war to Tomcat. The application uses the in memory implementation of Hypersonic database
and pre-populates some test data on start up (using DBUnit).

You can fire requests using Accept type headers with types of text/html, application/xml or application/json.

The default URI is:

  http://localhost:8080/rapi/people

So the following command should return a list of 'persons' in json:

  curl -H 'Accept:application/json' http://localhost:8080/rapi/people
  
Follow the links in the resulting paged json resource to navigate to specific people, their addresses 
or to fetch the next page of person data.

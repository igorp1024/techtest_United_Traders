# Unitedtraders java test project
This is a simple test task.

## Running the project
To run the project please do 

    mvn clean test jetty:run
After successful run it is possible to perform tests.

## Test cases
Any of the following `curl` invocations can be changed into the following way to pretty-print the JSON response:

    $ curl ...| jq .
But it may require installing `jq` in your system (Example: `$ sudo apt-get install jq` on Ubuntu)
Also, it is possible to show the requests and headers sent/received during curl communications bu using `-v` option

    $ curl -v ...

### Testing 'admin' user
Request:

    $ curl -s -u admin:admin --digest -H "Content-Type: application/json" http://localhost:8080/

Expected response:

    {
      "message": "Hail to the king!",
      "count": 1
    }


### Testing plain 'user'
Request:

    $ curl -s -u user:user --digest -H "Content-Type: application/json" http://localhost:8080/

Expected response:

    {
      "message": "Welcome user!",
      "count": 1
    }

### Testing 'other' user
Request:

    $ curl -s -u other:other --digest -H "Content-Type: application/json" http://localhost:8080/

Expected response:

    {
      "message": "User other does not have access"
    }

### Testing wrong http method with 'admin' user
Request:

    $ curl -s -X POST -u admin:admin --digest -H "Content-Type: application/json" http://localhost:8080/

Expected response:

    {
      "message": "Method not supported"
    }

### Testing access with wrong credentials
Request:

    $ curl -s -u bogus:user --digest -H "Content-Type: application/json" http://localhost:8080/

Expected response:

    {
      "message": "Access denied"
    }

### Testing access without credentials
Request:

    $ curl -s -H "Content-Type: application/json" http://localhost:8080/

Expected response:

    {
      "message": "Access denied"
    }

# Lambda Function Handlers Module Info
This is all the code to actually handle the information coming from the Lambda function invocation
into the Java application. I also included the high level handlers here so that each type of request 
will go into their individual function handlers.

For the handling of the invocation, the LambdaFunctionHandler is the entry point for the lambda 
function, where it puts the payload into our POJO LambdaRequest. All the fields from the payload 
request are then placed into the LambdaRequest's field through what I assume is pure magic. Then,
in the handler, I basically call LambdaRequest.handle and then that function returns an Object that
is returned through the POJO LambdaResponse which contains the Object data returned from the handle.

The LambdaRequest does a lot of checking and decisions based on the inputted values and essentially
is a bunch of big switch statements deciding what to do.
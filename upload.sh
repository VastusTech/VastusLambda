mvn package
aws lambda update-function-code --function-name VastusDatabaseLambdaFunction --zip-file fileb://./target/vastus-database-lambda-1.0-SNAPSHOT.jar


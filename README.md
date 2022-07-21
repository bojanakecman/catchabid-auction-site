
# Backend

### Build Application without Tests

`mvn clean install -DskiptTests=true`

### Build Application with Tests

`mvn clean install`

### Execute E2E Tests

`mvn failsafe:integration-test -P E2E`

### Clean DB

`mvn sql:execute -P clean-db`

### Insert Test Data

`mvn sql:execute -P insert-testdata`

User | Password 
--- | --- 
user1@test.com | test1234
user2@test.com | test1234
auctionhouse1@test.com | test1234
auctionhouse2@test.com | test1234

# Frontend

### Install dependencies

`npm install`

### build an run

`ng serve`
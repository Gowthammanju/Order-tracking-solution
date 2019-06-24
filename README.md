# Order tracking solution

Please implement a solution to record user shopping order from an online shop. 

Your application will receive order data from shops consisting of an order reference, payment type and online shop URL as well as some information about the user (first name, last name, address and email). There are three valid payment types: credit_card, paypal and cash_on_delivery.

Your application should validate each request. First, an order can only be processed if it contains a shop URL, order reference, payment type and user email. Otherwise, the request cannot be processed. Second, users are unique by email address, so the application may not create a new user database entry if the user already exists. Third, if the payment type is cash_on_delivery then the user's address is required and needs to be persisted. For other payment types, the address is not required. 

After the order is persisted, your application must return an order id. 

In addition, please provide an SQL DML statement to retrieve the total number of orders that has been made by a user grouped by shop and write it in the file ```number_of_orders_by_user_and_shop.sql```.

**Requirements:**
* Implement the solution using spring boot.
* Expose a REST API interface to the order tracking process defined.
* Expose a REST API interface to the retrieve orders for a given shop.
* Unit tests for all methods that are involved in the user creation process.

![alt text](task.png?raw=true "Task")


**Notes:**
* Don't hesitate to contact us in case you have any questions.
* Pay attenttion, the design of your solution will be discussed.
* You can modify any file in this coding challenge, this is just a skeleton.
* H2 Console can be reached under ```http://localhost:8080/h2-console``` username is ```sa``` and password should be blank. JDBC URL is ```jdbc:h2:mem:app_db;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_UPPER=false```
* It is estimated that this task won't take more than 8 hours of work.



**To build and run testcases:**

```shell
              ./gradlew clean test
 
```

**To bring up server:**

```shell
              ./gradlew clean bootrun

```

**Lombok integration to eclipse:**

```url

https://projectlombok.org/setup/eclipse

```

**Curl Commands:**

1. Retrieve orders for a given shop :

```curl

    curl -X GET -H 'Content-Type: application/json' -i http://localhost:8080/order/shopurl/superexampleshop.com

	Response : 
		HTTP/1.1 200
		Content-Disposition: inline;filename=f.txt
		Content-Type: application/json;charset=UTF-8
		Transfer-Encoding: chunked
		Date: Mon, 20 May 2019 14:37:37 GMT

		[{"id":1,"orderReference":"tx345678","user":{"id":1,"firstName":"John","lastName":"Doe","email":"john.doe@example.com","address":"Main Street 1 sample, USA"},"shop":{"id":1,"url":"superexampleshop.com"},"paymentType":"CASH_ON_DELIVERY"}]

```

2. Retrieve orders for a given shop not found:
```curl
	curl -X GET -H 'Content-Type: application/json' -i http://localhost:8080/order/shopurl/superexampl.com

	Response:
		HTTP/1.1 404 
		Content-Type: application/json;charset=UTF-8
		Transfer-Encoding: chunked
		Date: Mon, 20 May 2019 15:01:15 GMT
		{"timestamp":"2019-05-20T15:01:15.471+0000","status":404,"error":"Not Found","message":"superexampl.com is not found in database","path":"/order/shopurl/superexampl.com"}

```
3. Post orders :
```curl

	curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/orders --data '{"shopURL":"testexampleshop","orderReference":"tx1234","paymentType":"CASH_ON_DELIVERY","firstName":"testfirstname","lastName":"testlastname","email":"test@address.com","address":"34 test address ,Bonn, Germany"}'

	Response:
		HTTP/1.1 201 
		Content-Type: application/json;charset=UTF-8
		Transfer-Encoding: chunked
		Date: Mon, 20 May 2019 15:34:48 GMT

		{"id":3}
```
3. Post orders with out shopping url :

```curl
	curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/orders --data '{"shopURL":"","orderReference":"tx1234","paymentType":"CASH_ON_DELIVERY","firstName":"testfirstname","lastName":"testlastname","email":"test.com","address":"34 test address ,Bonn, Germany"}'
	
	Response :
	HTTP/1.1 400 
	Content-Type: application/json;charset=UTF-8
	Transfer-Encoding: chunked
	Date: Mon, 20 May 2019 15:35:57 GMT
	Connection: close

	{"timestamp":"2019-05-20T15:35:57.674+0000","status":400,"error":"Bad Request","message":"Shop Url cannot be missing or empty","path":"/orders"}

```

4. Post orders with out Address in case of CASH_ON_DELIVERY :

```curl

	 curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/orders --data '{"shopURL":"testexampleshop","orderReference":"tx1234","paymentType":"CASH_ON_DELIVERY","firstName":"testfirstname","lastName":"testlastname","email":"test@address.com","address":""}'

	response :
	HTTP/1.1 400 
	Content-Type: application/json;charset=UTF-8
	Transfer-Encoding: chunked
	Date: Mon, 20 May 2019 15:38:10 GMT
	Connection: close

	{"timestamp":"2019-05-20T15:38:10.034+0000","status":400,"error":"Bad Request","message":"address cannot be missing or empty in cash on delivery payment type","path":"/orders"}

```

5. Post orders with out Address in case of PAYPAL :

```curl

curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/orders --data '{"shopURL":"testshop1.com","orderReference":"tx1789","paymentType":"PAYPAL","firstName":"testfirstname","lastName":"testlastname","email":"test@address.com","address":""}'

	Response :
		HTTP/1.1 201 
		Content-Type: application/json;charset=UTF-8
		Transfer-Encoding: chunked
		Date: Mon, 20 May 2019 15:39:57 GMT

		{"id":5}

```

6. Sql query number_of_orders_by_user_and_shop : 

```sql

	select count(*) from SHOP_ORDER where user_ref= (select USER.id from USER  where email = 'john.doe@example.com') and  shop_ref= (select SHOP.id from SHOP  where url = 'superexampleshop.com')

```


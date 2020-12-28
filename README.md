# Real Estate Crawler Project

This project for real estate crawler prototype with design as Microservice.

You can also:

- Create data source for each website realestate to crawler
- Create list starter url to start crawl data
- Execute process starter url to get info of property on website

### Try it out

##### Step 1

You can run this application with docker-compose for development environment. So you should install docker-compose
before.

To check docker-compose is install you can run this command to check

```
docker-compose --version
```

After docker-compose is installed success in your machine. Run command below to run

```
./run-local.sh
```

if this command doesn't have permission run, you can set permission it

```
sudo chmod +x run-local.sh
```

Or if you can run by these step

```
./mvnw clean package
```

```
docker-compose up --build
```

##### Step 2

Access url http://{your-host-machine}:8083/swagger-ui and execute these steps below by order

* Create data source You can copy this json below to create data source by API /v1/admin/data-source to request body
    ```
    {
        "name": "Web batdongsan.com.vn",
        "url": "https://batdongsan.com.vn"
    }
    ```
* Create starter-url You can copy this json below to create data source by API /v1/admin/starter-url to request body
    ```
    {
        "dataSourceId": 1,
        "urls": [
            "https://batdongsan.com.vn/nha-dat-ban-tp-hcm",
            "https://batdongsan.com.vn/nha-dat-ban-ha-noi",
            "https://batdongsan.com.vn/nha-dat-ban-da-nang",
            "https://batdongsan.com.vn/nha-dat-ban-binh-duong"
        ]
    }
    ```
* Execute starter-url to get property info You can copy this json below to create data source by API /v1/admin/executor
  to request body
    ```
    {
        "id": 1
    }
    ```

After run execute starter url, please access Database to check result of crawler

### Continue...

* Implement get property API
* Implement retry to using dead letter queue Kafka
* Limit rate download 

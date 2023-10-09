# Widgets
A REST API project for Tain Video Streaming. The project is built with following:
* Java 17
* Build tool: Maven 3.9+
* Backend framework: Sprint-boot
  * H2 Database
  * OpenAPI for API UI.

## Howto's:

### Build the project: 
* Checkout the project.
* Build the project with maven: 
    
    `mvn clean install`

### Launching the server:
* After the build is done launch the server with following:

    `mvn spring-boot:run`

* Server is launched on 8080 port. Target on http://localhost:8080/videos
### Swagger API:
* To access the interactive API (openAPI) on: http://localhost:8080/swagger-ui/index.html

### H2 Database:
* You can view the contents of the H2 database by navigating to http://localhost:8080/h2-console in a web browser. Make sure to configure the JDBC URL as jdbc:h2:mem:testdb to connect to the database.
## REST API Exposed: 

Following rest APIs are exposed. For all the below mentioned API, we can test them with swagger exposed API or use curl as given under usage. 

### Video
#### publishVideo
PUT api to update an existing video metadata in store. If the video doesnt exist, then returns 400.

`curl -X POST 'http://localhost:8080/videos' \
-H 'Content-Type: application/json' \
-d '{
"content": "Your Video Content",
"delisted": false
}'`


#### delistVideo
`curl -X PUT 'http://localhost:8080/videos/<videoId>/delist'`

#### loadVideo
`curl -X GET 'http://localhost:8080/videos/<videoId>'`

#### playVideo
`curl -X GET 'http://localhost:8080/videos/<videoId>/play'`

#### listAllVideos
`curl -X GET 'http://localhost:8080/videos'`

#### listAllVideosWithFullDetails
GET WS to get all the videos which exist in the store.

`curl -X GET 'http://localhost:8080/videos/all'`

#### searchVideos
- Without any search criteria:

`curl -X GET 'http://localhost:8080/videos/search'`

- With search criteria (for example: by director):

`curl -X GET 'http://localhost:8080/videos/search?director=SomeDirectorName'`

#### retrieveVideoEngagement
`curl -X GET 'http://localhost:8080/videos/<videoId>/engagement'`

### Metadata
#### add metadata 
`curl -X POST "http://localhost:8080/videos/{VIDEO_ID}/metadata" \
-H "Content-Type: application/json" \
-d '{
"title": "SampleTitle",
"synopsis": "Sample Synopsis",
"director": "SampleDirector",
"crew": ["CrewMember1", "CrewMember2"],
"yearOfRelease": 2021,
"genre": "Drama",
"runningTime": 120,
"format": "HD"
}'
`
### update metadata
`curl -X PUT "http://localhost:8080/videos/{VIDEO_ID}/metadata" \
-H "Content-Type: application/json" \
-d '{
"title": "UpdatedTitle",
"synopsis": "Updated Synopsis",
"director": "UpdatedDirector",
"crew": ["UpdatedCrewMember1", "UpdatedCrewMember2"],
"yearOfRelease": 2022,
"genre": "Action",
"runningTime": 130,
"format": "4K"
}'
`

## Test Coverage

Classes: % 
Lines: %

Full report screenshot below:


# Note

3. logging
4. version is not used ( not incremented or validated)
5. test cases 


## Improvements
- Current system does not identify duplicate contents meaning you can publish same video or metadata multiple times
as ID for both are system generated UUID and generates when we push Video or Metadata. This can be handled by comparing
values or whole object hashcode.
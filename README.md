# Video Streaming API
A REST API project for Tain Video Streaming. The project is built with following:
* Java 17
* Build tool: Maven 3.9+
* Backend framework: 
  * Sprint-boot 2.7.16
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
* <img width="1512" alt="image" src="https://github.com/vjysolanki/videostream/assets/89914381/1e81272b-7724-4330-85d3-df8f6f9b3124">


### H2 Database:
* You can view the contents of the H2 database by navigating to http://localhost:8080/h2-console in a web browser. Make sure to configure the JDBC URL as jdbc:h2:mem:testdb to connect to the database.

## REST API Exposed: 

Following rest APIs are exposed. For all the below mentioned API, we can test them with swagger exposed API or use curl as given under usage. 

### Video
#### publish a video
PUT api to update an existing video metadata in store. If the video doesnt exist, then returns 400.

`curl -X POST 'http://localhost:8080/videos' \
-H 'Content-Type: application/json' \
-d '{
"content": "Your Video Content"
}'`


#### soft delete video 

`curl -X DELETE "http://localhost:8080/videos/<videoId>?soft=true"
`

#### load video
`curl -X GET 'http://localhost:8080/videos/<videoId>/load'`

#### play video
`curl -X GET 'http://localhost:8080/videos/<videoId>/play'`

#### List all available videos
This should only a subset of the video metadata
such as: Title, Director, Main Actor, Genre and Running Time.
`curl -X GET 'http://localhost:8080/videos'`

#### list only videos
GET WS to get only video without metadata which exist in the store.

`curl -X GET 'http://localhost:8080/videos/only'`

#### search videos
- Without any search criteria:

`curl -X GET 'http://localhost:8080/videos/search'`

- With search criteria (for example: by director):

`curl -X GET 'http://localhost:8080/videos/search?director=SomeDirectorName&crew=SomeCrewName&genre=SomeGenre'`

#### retrieve video engagement
Retrieve the engagement statistic for a video. Engagement can be split in 2:
- Impressions – A client loading a video.
- Views – A client playing a video.
`curl -X GET 'http://localhost:8080/videos/<videoId>/engagement'`

### Metadata
#### add metadata 
The metadata associated with the video.

`curl -X POST "http://localhost:8080/videos/<videoId>/metadata" \
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
Add and Edit the metadata associated with the video

`curl -X PUT "http://localhost:8080/videos/<videoId>/metadata" \
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

### Exception 
- IllegalArgumentException
 - Missing or Invalid user input
- EntityNotFoundException
 - Referred Video or Metadata does not exists in DB
- EntityExistsException
 - Referred Video or Metadata already exists in DB 
- OptimisticLockException
 - Concurrent update for same record 
## Test Coverage

Classes: 100%
Lines: 85%

Full report screenshot below:
<img width="728" alt="image" src="https://github.com/vjysolanki/videostream/assets/89914381/4ce61f47-b0e6-4b2b-8698-2a1704e95595">


## Assumption and Improvements
- Service is implemented based on assumption that video and metadata will have 1-1 relationship 
- Video contents are assumed as String that can be extended to take bytes 
- For Play operation, system return mocked url location where videos are stored instead of actual video contents passed ( player need to download the video from url)
- Current system uses H2 in memory database which can later be extended to permanents storage like postgres etc.

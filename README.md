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
#### Publish a video
PUT api to update an existing video metadata in store. If the video doesnt exist, then returns 400.

`curl -X POST 'http://localhost:8080/videos' \
-H 'Content-Type: application/json' \
-d '{
"content": "Your Video Content"
}'`


#### Soft delete a video 

`curl -X DELETE "http://localhost:8080/videos/<videoId>?soft=true"
`

#### Load a video
`curl -X GET 'http://localhost:8080/videos/<videoId>/load'`

#### Play video
`curl -X GET 'http://localhost:8080/videos/<videoId>/play'`

#### List all available videos
This should only be a subset of the video metadata
such as: Title, Director, Main Actor, Genre and Running Time.
`curl -X GET 'http://localhost:8080/videos'`

#### List only videos without metadata ( mostly for testing )
GET WS to get only video without metadata that exist in the store.

`curl -X GET 'http://localhost:8080/videos/only'`

#### Search videos
- Without any search criteria:

`curl -X GET 'http://localhost:8080/videos/search'`

- With search criteria (for example: by director):

`curl -X GET 'http://localhost:8080/videos/search?director=SomeDirectorName&crew=SomeCrewName&genre=SomeGenre'`

#### Retrieve a video engagement
Retrieve the engagement statistic for a video. Engagement can be split in 2:
- Impressions – A client loading a video.
- Views – A client playing a video.
`curl -X GET 'http://localhost:8080/videos/<videoId>/engagement'`

### Metadata
#### Add metadata 
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
### Update metadata
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

## Exceptions 
- IllegalArgumentException
  - Missing or Invalid user input
- EntityNotFoundException
  - Referred Video or Metadata does not exist in DB
- EntityExistsException
  - Referred Video or Metadata already exists in DB 
- OptimisticLockException
  - Concurrent update for the same record 
## Test Coverage

Classes: 100%
Lines: 85%

Full report screenshot below:
<img width="728" alt="image" src="https://github.com/vjysolanki/videostream/assets/89914381/4ce61f47-b0e6-4b2b-8698-2a1704e95595">


## Assumptions and Improvements
- 1-to-1 Relationship: Currently, the service operates under the presumption that each video is uniquely associated with a single metadata entry.
- Video Content Encoding: The system accepts video content as base64 encoded strings, which serve as a textual representation of the video's raw bytes
- Data Validation: There's room for enhanced data validation. Certain fields, such as runtime or release year, should be subjected to checks ensuring their validity (e.g., runtime must be positive, and release year should be within plausible ranges).
- Playback Mechanism: As of now, the 'Play' operation fetches a mock URL, suggesting the video's storage location, rather than the direct video content. While this emulates real-world streaming services (where a player fetches content from a provided URL), a complete solution might involve integrating an actual content delivery system or leveraging cloud services for video streaming.
- Database Choices: The application presently utilizes an H2 in-memory database. This choice is optimal for development or demonstration purposes due to its simplicity and ephemeral nature. For production-grade applications or longer-term storage, transitioning to more robust databases, such as PostgreSQL, would be advisable.

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
* To access the interactive API (openAPI) on: http://localhost:8080/swagger-ui/index.html

## REST API Exposed: 

Following rest APIs are exposed. For all the below mentioned API, we can test them with swagger exposed API or use curl as given under usage. 

#### Store new video
POST api to save a new video or update existing video metadata. 
* <TO BE ADDED>
**curl example**

`curl -d '{
"content": "your video content here",
"title": "Title of the Video",
"synopsis": "Synopsis of the video",
"director": "Director's Name",
"crew": ["Crew Member 1", "Crew Member 2", "Crew Member 3"],
"yearOfRelease": 2023,
"genre": "Action",
"runningTime": 120,
"delisted": false
}' -H "Content-Type: application/json" http://localhost:8080/videos`

#### Update existing video metadata
PUT api to update an existing video metadata in store. If the video doesnt exist, then returns 400. 

**curl example**

`curl -d '{
"synopsis": "Synopsis of the video",
"director": "Director's Name",
"genre": "Drama, Action",
"runningTime": 180,
}' -H "Content-Type: application/json" http://localhost:8080/videos/{videoId}/metadata`

#### To retrieve all videos:
GET WS to get all the videos which exist in the store.

**curl example**

`curl http://localhost:8080/videos`

## Test Coverage

Classes: % 
Lines: %

Full report screenshot below:


# Note
1. fix proper response code
2. exception handling
3. logging
4. global exception handling
5. test cases 

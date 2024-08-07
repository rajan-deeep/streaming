It's a streaming service.

Have jdk 17 and maven installed

It's a springboot application so service up will be easy.

Once up:
1. hit localhost:8080/upload
2. upload any video, it store video in 3 bitrate low,medium,high
3. go to video list page and play any video.


This service does not have database integration for now, so videos are getting stored in same folder
so change the path in application.properties accordingly file.upload-dir (give path where you want to store the video segments)

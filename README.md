It's a streaming service.

Have jdk 17 and maven installed

It's a springboot application so service up will be easy.

Once up:
1. hit localhost:8080/upload
2. upload any video, it store video in 3 bitrate low,medium,high
3. go to video list page and play any video.


This service does not have database integration for now,
1. so processed video segments are getting stored in same folder(no original video is stored)
2. change the path in application.properties accordingly file.upload-dir (give path where you want to store the video segments)


live service:
myownpanda.com

logs tracking: http://89.116.32.180:5601/app/logs/stream

elasticsearch for storing the logs
logstash for collecting the logs and transforming before sending it to elastic
kibana for quering the logs and visualising the logs on top of elastic
filebeat as log shipper

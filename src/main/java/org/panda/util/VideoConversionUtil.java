package org.panda.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class VideoConversionUtil {

    public static void convertToHLS(String inputFilePath, String outputFolderPath) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFolder = new File(outputFolderPath);

        // Create output folder and subdirectories if they do not exist
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        // Ensure subdirectories are created
        new File(outputFolderPath + File.separator + "low").mkdirs();
        new File(outputFolderPath + File.separator + "medium").mkdirs();
        new File(outputFolderPath + File.separator + "high").mkdirs();
        new File(outputFolderPath + File.separator + "thumbnails").mkdirs(); // Thumbnail directory

        // Generate thumbnail
        generateThumbnail(inputFilePath, outputFolderPath);

        // Low bitrate command
        String lowBitrateCommand = String.format(
                "ffmpeg -i \"%s\" -vf scale=iw/2*2:ih/2*2 -b:v 800k -maxrate 856k -bufsize 1200k -hls_time 10 -hls_playlist_type vod -hls_segment_filename \"%s/low/segment_%%03d.ts\" \"%s/low/index.m3u8\"",
                inputFilePath, outputFolderPath, outputFolderPath);

        // Medium bitrate command
        String mediumBitrateCommand = String.format(
                "ffmpeg -i \"%s\" -vf scale=iw/2*2:ih/2*2 -b:v 1400k -maxrate 1498k -bufsize 2100k -hls_time 10 -hls_playlist_type vod -hls_segment_filename \"%s/medium/segment_%%03d.ts\" \"%s/medium/index.m3u8\"",
                inputFilePath, outputFolderPath, outputFolderPath);

        // High bitrate command
        String highBitrateCommand = String.format(
                "ffmpeg -i \"%s\" -vf scale=iw/2*2:ih/2*2 -b:v 2800k -maxrate 2996k -bufsize 4200k -hls_time 10 -hls_playlist_type vod -hls_segment_filename \"%s/high/segment_%%03d.ts\" \"%s/high/index.m3u8\"",
                inputFilePath, outputFolderPath, outputFolderPath);

        // Execute FFmpeg commands
        executeCommand(lowBitrateCommand);
        executeCommand(mediumBitrateCommand);
        executeCommand(highBitrateCommand);

        // Create master playlist
        String masterPlaylistContent = "#EXTM3U\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=640x360\n" +
                "low/index.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=1400000,RESOLUTION=960x540\n" +
                "medium/index.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=2800000,RESOLUTION=1280x720\n" +
                "high/index.m3u8\n";

        File masterPlaylist = new File(outputFolderPath + File.separator + "master.m3u8");
        Files.write(masterPlaylist.toPath(), masterPlaylistContent.getBytes());

        // Attempt to delete the original video file
        if (inputFile.exists()) {
            boolean deleted = inputFile.delete();
            if (!deleted) {
                log.warn("Failed to delete the original video file: " + inputFilePath);
                log.warn("File exists after deletion attempt: " + inputFile.exists());
            } else {
                log.info("Successfully deleted the original video file: " + inputFilePath);
            }
        } else {
            log.warn("Original video file does not exist: " + inputFilePath);
        }
    }

    private static void generateThumbnail(String inputFilePath, String outputFolderPath) throws IOException {
        String thumbnailsFolderPath = outputFolderPath + File.separator + "thumbnails";
        File thumbnailsFolder = new File(thumbnailsFolderPath);

        if (!thumbnailsFolder.exists()) {
            thumbnailsFolder.mkdirs();
        }

        String thumbnailPath = thumbnailsFolderPath + File.separator + "thumbnail.png";
        File thumbnailFile = new File(thumbnailPath);

        // Delete existing thumbnail if it exists
        if (thumbnailFile.exists()) {
            boolean deleted = thumbnailFile.delete();
            if (!deleted) {
                log.warn("Failed to delete existing thumbnail file: " + thumbnailPath);
            }
        }

        String thumbnailCommand = String.format(
                "ffmpeg -i \"%s\" -ss 00:00:01.000 -vframes 1 \"%s\"",
                inputFilePath, thumbnailPath);

        executeCommand(thumbnailCommand);
    }

    private static void executeCommand(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        new Thread(() -> {
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg command failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg command interrupted", e);
        }
    }
}

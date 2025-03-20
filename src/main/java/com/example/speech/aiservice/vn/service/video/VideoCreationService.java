package com.example.speech.aiservice.vn.service.video;

import com.example.speech.aiservice.vn.dto.response.CreateVideoResponseDTO;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.service.filehandler.FileNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@Service
public class VideoCreationService {

    private final FileNameService fileNameService;
    private final String ffmpegPath = "E:\\CongViecHocTap\\ffmpeg\\ffmpeg-master-latest-win64-gpl-shared\\bin\\ffmpeg.exe";
    private final String uploadDirectoryPath = "E:\\CongViecHocTap\\UploadVideo\\";
    private final String fileExtension = ".mp4";

    @Autowired
    public VideoCreationService(FileNameService fileNameService) {
        this.fileNameService = fileNameService;
    }

    public CreateVideoResponseDTO createVideoResponseDTO(String audioPath, String imagePath, Novel novel, Chapter chapter) {

        if (audioPath == null) {
            System.out.println("⚠️ No video files found!");
            return new CreateVideoResponseDTO("⚠️ No video files found!", null, null, null);
        }



        // Tạo thư mục cho bộ truyện nếu chưa tồn tại
        String safeNovelTitle = fileNameService.sanitizeFileName(novel.getTitle());
        String novelDirectory = uploadDirectoryPath + File.separator + safeNovelTitle;
        fileNameService.ensureDirectoryExists(novelDirectory);

        // Xử lý tên file chương hợp lệ
        String safeChapterTitle = fileNameService.sanitizeFileName(chapter.getTitle()) + fileExtension;
        String audioFilePath = novelDirectory + File.separator + safeChapterTitle;


        String videoFilePath = fileNameService.getAvailableFileName(audioFilePath, chapter.getTitle(), fileExtension);

        // FFmpeg command
        String command = "\"" + ffmpegPath + "\" -loop 1 -i \"" + imagePath + "\" -i \"" + audioPath +
                "\" -c:v libx264 -tune stillimage -c:a aac -b:a 192k -pix_fmt yuv420p -shortest \"" + videoFilePath + "\"";


        System.out.println("Run command : " + command);

        try {
            Process process = Runtime.getRuntime().exec(command);

            // Read FFmpeg output for debugging
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Complete combining photos into video! Output file : " + videoFilePath);
            } else {
                System.out.println("⚠️ FFmpeg encountered an error, check the output above.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new CreateVideoResponseDTO("Create a successful video", imagePath, audioPath, videoFilePath);
    }
}

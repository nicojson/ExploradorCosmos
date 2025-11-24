package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class VideoCardControllerTest {

    @Test
    public void testVideoDataMapping() {
        Publication videoPub = new Publication(
                2, "nasa-video-1", "video", "Moon Landing", "Apollo 11 landing", "http://example.com/thumb.jpg",
                LocalDateTime.now());

        assertEquals("video", videoPub.getContentType());
        assertEquals("Moon Landing", videoPub.getTitle());
        assertNotNull(videoPub.getPublishedDate());
    }
}

package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostCardControllerTest {

    @Test
    public void testPostDataMapping() {
        Publication postPub = new Publication(
                2, "nasa-image-1", "image", "Mars Rover", "Curiosity rover photo", "http://example.com/mars.jpg",
                LocalDateTime.now());

        assertEquals("image", postPub.getContentType());
        assertEquals("Mars Rover", postPub.getTitle());
    }
}

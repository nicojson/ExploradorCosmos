package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.Publication;

import java.util.List;

public interface OfflineContentDAO {
    void save(Publication publication);
    List<Publication> findAll();
    void delete(int publicationId);
}

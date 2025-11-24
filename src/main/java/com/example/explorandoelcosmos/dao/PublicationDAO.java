package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.Publication;
import java.util.List;
import java.util.Optional;

public interface PublicationDAO {
    void save(Publication publication);

    void update(Publication publication);

    void delete(int id);

    Optional<Publication> findById(int id);

    List<Publication> findAll();

    List<Publication> findFavorites();

    Optional<Publication> findByOriginalId(String originalId, int sourceApiId);
}

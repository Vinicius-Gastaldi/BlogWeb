package com.web.repositories;

import com.web.models.Comentario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComentarioRepository extends CrudRepository<Comentario, UUID> {
    List<Comentario> findByPostId(UUID postId); // Adicione este método
}

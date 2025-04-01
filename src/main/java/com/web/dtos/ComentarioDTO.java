package com.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class ComentarioDTO {
    // Getters e Setters
    private UUID id;
    private LocalDateTime data;

    @NotBlank(message = "O comentário não pode estar em branco.")
    @Size(max = 500, message = "O comentário não pode ter mais de 500 caracteres.")
    private String comentario;

    @NotNull(message = "O ID do post é obrigatório.")
    private UUID postId;

    // Construtor padrão (obrigatório)
    public ComentarioDTO() {}

    // Construtor com todos os campos
    public ComentarioDTO(UUID id, LocalDateTime data, String comentario, UUID postId) {
        this.id = id;
        this.data = data;
        this.comentario = comentario;
        this.postId = postId;
    }

}
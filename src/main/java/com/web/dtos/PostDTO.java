package com.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class PostDTO {
    // Getters e Setters (obrigatórios para binding do formulário)
    private UUID id;

    @NotBlank(message = "O autor não pode estar em branco.")
    @Size(max = 70, message = "O autor não pode ter mais de 70 caracteres.")
    private String autor;

    private LocalDateTime data;

    @NotBlank(message = "O título não pode estar em branco.")
    @Size(max = 70, message = "O título não pode ter mais de 70 caracteres.")
    private String titulo;

    @NotBlank(message = "O texto não pode estar em branco.")
    private String texto;

    private List<ComentarioDTO> comentarios;

    // Construtor padrão (obrigatório para o Spring)
    public PostDTO() {}

    // Construtor com todos os campos
    public PostDTO(UUID id, String autor, LocalDateTime data, String titulo, String texto, List<ComentarioDTO> comentarios) {
        this.id = id;
        this.autor = autor;
        this.data = data;
        this.titulo = titulo;
        this.texto = texto;
        this.comentarios = comentarios;
    }

}
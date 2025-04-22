package com.ec.ecommercev3.DTO.Comment;

import com.ec.ecommercev3.Entity.Enums.CommentType;

public record CommentDTO(String comment, CommentType commentType) {
}

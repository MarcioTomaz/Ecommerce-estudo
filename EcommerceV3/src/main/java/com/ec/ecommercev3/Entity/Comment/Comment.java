package com.ec.ecommercev3.Entity.Comment;


import com.ec.ecommercev3.Entity.*;
import com.ec.ecommercev3.Entity.Enums.CommentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "_comment")
@Entity
public class Comment extends DomainEntity{

    @Column
    private String comment;

    @Column
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true) // Avaliação vinculada ao produto
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true) // Comentário vinculado a um pedido
    private Order order;

    @ManyToOne
    @JoinColumn(name = "admin_id") // Comentário feito por um administrador
    private UserPerson admin;


    public Comment(String reason, CommentType commentType, Order order, UserPerson userPerson) {
        this.comment = reason;
        this.commentType = commentType;
        this.order = order;
        this.admin = userPerson;
    }
}

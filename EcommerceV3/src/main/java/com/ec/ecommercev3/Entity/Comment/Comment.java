package com.ec.ecommercev3.Entity.Comment;


import com.ec.ecommercev3.Entity.DomainEntity;
import com.ec.ecommercev3.Entity.Enums.CommentType;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.Person;
import com.ec.ecommercev3.Entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "_comment")
@Entity
public class Comment extends DomainEntity{

    @Column
    private String comment;

    @Column
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Avaliação vinculada ao produto
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false) // Comentário vinculado a um pedido
    private Order order;

    @ManyToOne
    @JoinColumn(name = "admin_id") // Comentário feito por um administrador
    private Person admin;




}

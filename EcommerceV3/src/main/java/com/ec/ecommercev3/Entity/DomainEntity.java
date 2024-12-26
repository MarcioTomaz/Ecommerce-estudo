package com.ec.ecommercev3.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@MappedSuperclass
public abstract class DomainEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Column(name = "updatedDate")
    private LocalDateTime updatedDate;

    @Transient
    private boolean globalSearch = false;

    public DomainEntity(Long id) {
        this.id = id;
    }

    public DomainEntity(LocalDate localDate) {this.creationDate = localDate;}
}

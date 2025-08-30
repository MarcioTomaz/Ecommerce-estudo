


alter table _product
    add image_path varchar(255) null after product_name;

alter table _product_history
    add image_path varchar(255) null after product_name;


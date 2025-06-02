create table _currency (
                           id            bigint auto_increment primary key,
                           active        bit not null,
                           creation_date datetime(6) not null,
                           deleted_date  datetime(6),
                           updated_date  datetime(6),
                           code          varchar(255),
                           name          varchar(255),
                           symbol        varchar(255)
);

create table _person (
                         id            bigint auto_increment primary key,
                         active        bit not null,
                         creation_date datetime(6) not null,
                         deleted_date  datetime(6),
                         updated_date  datetime(6),
                         birth_date    date,
                         first_name    varchar(255) not null,
                         gender        enum ('MASCULINO', 'FEMININO', 'OUTRO'),
                         last_name     varchar(255),
                         phone_number  varchar(255),
                         phone_type    enum ('FIXO', 'MOVEL')
);

create table _address (
                          id             bigint auto_increment primary key,
                          active         bit not null,
                          creation_date  datetime(6) not null,
                          deleted_date   datetime(6),
                          updated_date   datetime(6),
                          address_type   enum ('COBRANCA', 'ENTREGA') not null,
                          city           varchar(255),
                          country        varchar(50),
                          district       varchar(255) not null,
                          logradouro     varchar(255) not null,
                          number         varchar(255) not null,
                          observation    varchar(255),
                          residency_type enum ('CASA', 'APARTAMENTO') not null,
                          state          varchar(255),
                          street         varchar(255) not null,
                          zip_code       varchar(255) not null,
                          person_id      bigint,
                          constraint fk_address__person_id___person foreign key (person_id) references _person (id)
);

create table _card (
                       id              bigint auto_increment primary key,
                       active          bit not null,
                       creation_date   datetime(6) not null,
                       deleted_date    datetime(6),
                       updated_date    datetime(6),
                       alias           varchar(255),
                       expiration_date datetime(6),
                       flag            enum ('VISA', 'MASTERCARD', 'EUROCARD'),
                       holder          varchar(255),
                       holder_cpf      varchar(255),
                       number          varchar(255),
                       preferencial    bit,
                       security        varchar(255),
                       person_id       bigint,
                       constraint fk_card__person_id___person foreign key (person_id) references _person (id)
);

create table _order (
                        id                  bigint auto_increment primary key,
                        active              bit not null,
                        creation_date       datetime(6) not null,
                        deleted_date        datetime(6),
                        updated_date        datetime(6),
                        coupon              varchar(255),
                        status              enum ('PENDING', 'PROCESSING', 'WAITING_FOR_PAYMENT', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELED', 'EXPIRED'),
                        total               double,
                        billing_address_id  bigint not null,
                        person_id           bigint,
                        shipping_address_id bigint not null,
                        constraint fk_order__billing_address_id___address foreign key (billing_address_id) references _address (id),
                        constraint fk_order__shipping_address_id___address foreign key (shipping_address_id) references _address (id),
                        constraint fk_order__person_id___person foreign key (person_id) references _person (id)
);

create table _product (
                          id                  bigint auto_increment primary key,
                          active              bit not null,
                          creation_date       datetime(6) not null,
                          deleted_date        datetime(6),
                          updated_date        datetime(6),
                          product_category    enum ('CATEGORY1', 'CATEGORY2', 'CATEGORY3', 'CATEGORY4', 'CATEGORY5'),
                          product_description varchar(255),
                          product_name        varchar(255),
                          product_price       double not null,
                          stock               int,
                          currency_id         bigint not null,
                          constraint fk_product__currency_id___currency foreign key (currency_id) references _currency (id)
);

create table _review (
                         id            bigint auto_increment primary key,
                         active        bit not null,
                         creation_date datetime(6) not null,
                         deleted_date  datetime(6),
                         updated_date  datetime(6),
                         feed_back     varchar(255),
                         rating        int not null,
                         customer_id   bigint,
                         order_id      bigint,
                         product_id    bigint,
                         constraint fk_review__product_id___product foreign key (product_id) references _product (id),
                         constraint fk_review__order_id___order foreign key (order_id) references _order (id),
                         constraint fk_review__customer_id___person foreign key (customer_id) references _person (id)
);

create table _user_person (
                              id            bigint auto_increment primary key,
                              active        bit not null,
                              creation_date datetime(6) not null,
                              deleted_date  datetime(6),
                              updated_date  datetime(6),
                              email         varchar(255) not null,
                              password      varchar(255) not null,
                              role          tinyint,
                              user_type     enum ('CLIENTE', 'FUNCIONARIO'),
                              person_id     bigint,
                              constraint uk_user_person__person_id unique (person_id),
                              constraint fk_user_person__person_id___person foreign key (person_id) references _person (id),
                              check (`role` between 0 and 1)
);

create table _cart (
                       id             bigint auto_increment primary key,
                       active         bit not null,
                       creation_date  datetime(6) not null,
                       deleted_date   datetime(6),
                       updated_date   datetime(6),
                       total_value    double,
                       user_person_id bigint,
                       constraint fk_cart__user_person_id___user_person foreign key (user_person_id) references _user_person (id)
);

create table _comment (
                          id            bigint auto_increment primary key,
                          active        bit not null,
                          creation_date datetime(6) not null,
                          deleted_date  datetime(6),
                          updated_date  datetime(6),
                          comment       varchar(255),
                          comment_type  enum ('REJECTION_REASON', 'systemComment'),
                          admin_id      bigint,
                          order_id      bigint,
                          product_id    bigint,
                          constraint fk_comment__order_id___order foreign key (order_id) references _order (id),
                          constraint fk_comment__product_id___product foreign key (product_id) references _product (id),
                          constraint fk_comment__admin_id___user_person foreign key (admin_id) references _user_person (id)
);

create table _item (
                       id            bigint auto_increment primary key,
                       active        bit not null,
                       creation_date datetime(6) not null,
                       deleted_date  datetime(6),
                       updated_date  datetime(6),
                       quantity      bigint,
                       cart_id       bigint,
                       order_id      bigint,
                       product_id    bigint,
                       constraint fk_item__order_id___order foreign key (order_id) references _order (id),
                       constraint fk_item__cart_id___cart foreign key (cart_id) references _cart (id),
                       constraint fk_item__product_id___product foreign key (product_id) references _product (id)
);

create table _payment_method (
                                 payment_type   varchar(31) not null,
                                 id             bigint auto_increment primary key,
                                 active         bit not null,
                                 creation_date  datetime(6) not null,
                                 deleted_date   datetime(6),
                                 updated_date   datetime(6),
                                 amount_paid    double,
                                 transaction_id varchar(255),
                                 order_id       bigint not null,
                                 user_person_id bigint,
                                 constraint fk_payment_method__user_person_id___user_person foreign key (user_person_id) references _user_person (id),
                                 constraint fk_payment_method__order_id___order foreign key (order_id) references _order (id)
);

create table _pix_payment (
                              pix_key      varchar(255) not null,
                              pix_key_type enum ('CPF', 'CNPJ', 'PHONE', 'EMAIL', 'RANDOM'),
                              id           bigint not null primary key,
                              constraint fk_pix_payment__id___payment_method foreign key (id) references _payment_method (id)
);

create table credit_card_payment_method (
                                            installments int,
                                            id           bigint not null primary key,
                                            card_id      bigint,
                                            constraint fk_credit_card_payment_method__id___payment_method foreign key (id) references _payment_method (id),
                                            constraint fk_credit_card_payment_method__card_id___card foreign key (card_id) references _card (id)
);

create table notification (
                              id             bigint auto_increment primary key,
                              active         bit not null,
                              creation_date  datetime(6) not null,
                              deleted_date   datetime(6),
                              updated_date   datetime(6),
                              is_read        bit,
                              message        text,
                              read_at        datetime(6),
                              reference_id   bigint,
                              reference_type tinyint,
                              title          varchar(255),
                              type           tinyint,
                              user_id        bigint,
                              constraint fk_notification__user_id___user_person foreign key (user_id) references _user_person (id),
                              check (`reference_type` between 0 and 1),
                              check (`type` between 0 and 1)
);

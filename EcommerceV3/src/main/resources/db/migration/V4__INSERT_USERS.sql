

INSERT INTO _person (id, active, creation_date, deleted_date, updated_date, birth_date, first_name, gender, last_name, phone_number, phone_type) VALUES (1, true, '2025-06-02 14:30:00.865461', null, null, '1999-12-08', 'Marcio', 'MASCULINO', 'Tomaz', '40028922', 'FIXO');
INSERT INTO _person (id, active, creation_date, deleted_date, updated_date, birth_date, first_name, gender, last_name, phone_number, phone_type) VALUES (2, true, '2025-06-02 15:05:47.740956', null, null, '1999-12-08', 'Lucas', 'MASCULINO', 'donizeti', '40028922', 'FIXO');

INSERT INTO _user_person (id, active, creation_date, deleted_date, updated_date, email, password, role, user_type, person_id) VALUES (1, true, '2025-06-02 14:30:00.865461', null, null, 'marcioEmail22', '$2a$10$UDWk/qbNNewAv.fwHlGNn.HkoSzSK1UTjf8sR7TNs.yWq4ucg8g96', 0, null, 1);
INSERT INTO _user_person (id, active, creation_date, deleted_date, updated_date, email, password, role, user_type, person_id) VALUES (2, true, '2025-06-02 15:05:47.740956', null, null, 'lucas123', '$2a$10$P.GsctXAyjQisw5Wg6iBCO/XO/PIIW3NqFLsQdAK858y.9lalcGPe', 1, null, 2);

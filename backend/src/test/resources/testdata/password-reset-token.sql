INSERT INTO public.regular_user (id, active, email, first_name,last_name, password_hash, phone_nr, country, city, street, house_nr) VALUES (1, true, 'test@test.com', 'Testname','TestLastName', '$2a$10$XAUwohCpy/u4KC3X91srC.9yV0BNea.s9NFajNeSKjS6hLwJflNAm', '123', 'Austria', 'Vienna', 'Resselgasse', 1);
INSERT INTO public.regular_user (id, active, email, first_name,last_name, password_hash, phone_nr, country, city, street, house_nr) VALUES (2, true, 'test2@test.com', 'Testname2','TestLastName2', '$2a$10$XAUwohCpy/u4KC3X91srC.9yV0BNea.s9NFajNeSKjS6hLwJflNAm', '123', 'Austria', 'Vienna', 'Resselgasse', 1);

INSERT INTO public.password_reset_token (id, expiry_date, token, user_id) VALUES (1,  current_timestamp + (10 * interval '1 minute'),666666, 1);
INSERT INTO public.password_reset_token (id, expiry_date, token, user_id) VALUES (2, '2020-12-12 22:27:33.445',666666, 2);

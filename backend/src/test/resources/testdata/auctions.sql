INSERT INTO public.auction_house (id, active, email, name, password_hash, phone_nr, country, city, street, house_nr, tid) VALUES (1, true, 'test@test.com', 'Testname', '9999', '123', 'Austria', 'Vienna', 'Resselgasse', 1, '1234567');
INSERT INTO public.auction_post (id, category, name, end_time, min_price, start_time, status, creator, country, city, street, house_nr) VALUES (2, 'ELECTRONICS', 'Samsung A5', '2020-12-31 00:00:00', 3, '2020-12-31 00:00:00', 'ACTIVE', 1, 'Austria', 'Vienna', 'Resselgasse', 1);
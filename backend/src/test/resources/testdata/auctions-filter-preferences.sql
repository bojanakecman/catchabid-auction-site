INSERT INTO public.regular_user (id, active, email, first_name, last_name, password_hash, phone_nr, country, city, street, house_nr) VALUES (1, true, 'testRegular@test.com', 'Robert', 'Richard', '9999', '123', 'Austria', 'Vienna', 'Resselgasse', 1);
INSERT INTO public.regular_user (id, active, email, first_name, last_name, password_hash, phone_nr, country, city, street, house_nr) VALUES (2, true, 'testRegular1@test.com', 'Bob', 'Richard', '9999', '123', 'Austria', 'Vienna', 'Resselgasse', 2);

INSERT INTO public.auction_house (id, active, email, name, password_hash, phone_nr, country, city, street, house_nr, tid) VALUES (3, true, 'test2@test.com', 'Bob', '3333', '312', 'Austria', 'Vienna', 'Resselgasse', 1, '1234567');

INSERT INTO public.regular_user_preferences(user_id, preferences) VALUES (1,'ELECTRONICS');
INSERT INTO public.regular_user_preferences(user_id, preferences) VALUES (2,'MUSIC');
INSERT INTO public.auction_post (id, city, country, house_nr, street, category, description, start_time, min_price, name, end_time, status, creator, bid_id) VALUES ((10000), null, 'Germany', null, null, 'ELECTRONICS', 'Desktop PC - Intel i7, AMD RX 580 8GB', current_timestamp - INTERVAL '5 minute', 3, 'Item1', current_timestamp + INTERVAL '15 minute', 'ACTIVE', 3, null);
INSERT INTO public.auction_post (id, city, country, house_nr, street, category, description, start_time, min_price, name, end_time, status, creator, bid_id) VALUES (10001, null, 'Austria', null, null, 'MUSIC', 'Bob Marley Tickets', current_timestamp - INTERVAL '5 minute', 6, 'Item2', current_timestamp + INTERVAL '15 minute', 'ACTIVE', 3, null);
INSERT INTO public.auction_post (id, city, country, house_nr, street, category, description, start_time, min_price, name, end_time, status, creator, bid_id) VALUES (10002, null, 'Austria', null, null, 'OTHER', 'Ticket to Paradise CD', current_timestamp - INTERVAL '5 minute', 99, 'Item3',current_timestamp + INTERVAL '15 minute', 'UPCOMING', 3, null);

INSERT INTO public.auction_post (id, city, country, house_nr, street, category, description, start_time, min_price, name, end_time, status, creator, bid_id) VALUES ((10003), null, 'Germany', null, null, 'ELECTRONICS', 'Desktop PC - Intel i7, AMD RX 580 8GB', current_timestamp + INTERVAL '5 minute', 3, 'Item1', current_timestamp + INTERVAL '25 minute', 'ACTIVE', 3, null);
INSERT INTO public.auction_post (id, city, country, house_nr, street, category, description, start_time, min_price, name, end_time, status, creator, bid_id) VALUES (10004, null, 'Austria', null, null, 'MUSIC', 'Bob Marley Tickets', current_timestamp + INTERVAL '15 minute', 6, 'Item2', current_timestamp + INTERVAL '35 minute', 'ACTIVE', 3, null);
INSERT INTO public.auction_post (id, city, country, house_nr, street, category, description, start_time, min_price, name, end_time, status, creator, bid_id) VALUES (10005, null, 'Austria', null, null, 'OTHER', 'Ticket to Paradise CD', current_timestamp + INTERVAL '15 minute', 99, 'Item3',current_timestamp + INTERVAL '35 minute', 'UPCOMING', 3, null);



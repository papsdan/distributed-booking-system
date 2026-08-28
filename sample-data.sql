-- locations
INSERT INTO public.locations (id, area, city) VALUES (1, 'Tottenham Hale', 'London');
INSERT INTO public.locations (id, area, city) VALUES (2, 'Finsbury Park', 'London');
INSERT INTO public.locations (id, area, city) VALUES (3, 'Clapham', 'London');
INSERT INTO public.locations (id, area, city) VALUES (4, 'Shoreditch', 'London');
INSERT INTO public.locations (id, area, city) VALUES (5, 'Salford', 'Manchester');
INSERT INTO public.locations (id, area, city) VALUES (6, 'Northern Quarter', 'Manchester');
INSERT INTO public.locations (id, area, city) VALUES (7, 'Digbeth', 'Birmingham');
INSERT INTO public.locations (id, area, city) VALUES (8, 'Headingley', 'Leeds');
INSERT INTO public.locations (id, area, city) VALUES (9, 'Bedminster', 'Bristol');
INSERT INTO public.locations (id, area, city) VALUES (10, 'Partick', 'Glasgow');

-- pitches
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (1, true, 22, '2026-06-01 09:00:00', 'Pitch A', 1);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (2, true, 12, '2026-06-01 09:00:00', 'Pitch B', 1);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (3, true, 14, '2026-06-01 09:00:00', 'Pitch C', 2);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (4, false, 10, '2026-06-01 09:00:00', 'Pitch D', 2);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (5, true, 16, '2026-06-01 09:00:00', 'Pitch E', 3);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (6, true, 10, '2026-06-01 09:00:00', 'Pitch F', 3);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (7, true, 20, '2026-06-01 09:00:00', 'Pitch G', 4);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (8, true, 10, '2026-06-01 09:00:00', 'Pitch H', 5);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (9, true, 12, '2026-06-01 09:00:00', 'Pitch I', 5);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (10, true, 18, '2026-06-01 09:00:00', 'Pitch J', 6);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (11, true, 14, '2026-06-01 09:00:00', 'Pitch K', 7);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (12, true, 12, '2026-06-01 09:00:00', 'Pitch L', 8);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (13, true, 10, '2026-06-01 09:00:00', 'Pitch M', 9);
INSERT INTO public.pitches (id, active, capacity, created_at, name, location_id) VALUES (14, true, 16, '2026-06-01 09:00:00', 'Pitch N', 10);

-- users (password for all: password)
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (1, true, '2026-06-15 09:00:00', NULL, 'sam@example.com', 'Sam', 'Smith', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'ADMIN', 'samsmith');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (2, true, '2026-06-15 09:00:00', NULL, 'jon@example.com', 'Jon', 'Smith', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'ADMIN', 'jonsmith');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (3, true, '2026-06-15 09:00:00', NULL, 'daniel.papier@example.com', 'Daniel', 'Papier', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'ADMIN', 'danpapier');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (4, true, '2026-06-15 09:00:00', NULL, 'jane.doe@example.com', 'Jane', 'Doe', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'janedoe');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (5, true, '2026-06-15 09:00:00', NULL, 'ali.gold@example.com', 'Ali', 'Gold', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'aligold');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (6, true, '2026-06-15 09:00:00', NULL, 'mo.farah@example.com', 'Mo', 'Farah', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'mofarah');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (7, true, '2026-06-15 09:00:00', NULL, 'lucy.brown@example.com', 'Lucy', 'Brown', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'lucybrown');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (8, true, '2026-06-15 09:00:00', NULL, 'tom.evans@example.com', 'Tom', 'Evans', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'tomevans');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (9, true, '2026-06-15 09:00:00', NULL, 'priya.patel@example.com', 'Priya', 'Patel', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'priyapatel');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (10, true, '2026-06-15 09:00:00', NULL, 'chidi.okafor@example.com', 'Chidi', 'Okafor', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'chidiokafor');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (11, true, '2026-06-15 09:00:00', NULL, 'emma.wilson@example.com', 'Emma', 'Wilson', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'emmawilson');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (12, true, '2026-06-15 09:00:00', NULL, 'ryan.oconnor@example.com', 'Ryan', 'OConnor', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'ryanoconnor');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (13, true, '2026-06-15 09:00:00', NULL, 'fatima.hussain@example.com', 'Fatima', 'Hussain', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'fatimahussain');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (14, true, '2026-06-15 09:00:00', NULL, 'jake.turner@example.com', 'Jake', 'Turner', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'jaketurner');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (15, true, '2026-06-15 09:00:00', NULL, 'sofia.martinez@example.com', 'Sofia', 'Martinez', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'sofiamartinez');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (16, true, '2026-06-15 09:00:00', NULL, 'daniel.kim@example.com', 'Daniel', 'Kim', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'danielkim');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (17, true, '2026-06-15 09:00:00', NULL, 'grace.mensah@example.com', 'Grace', 'Mensah', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'gracemensah');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (18, true, '2026-06-15 09:00:00', NULL, 'noah.walker@example.com', 'Noah', 'Walker', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'noahwalker');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (19, true, '2026-06-15 09:00:00', NULL, 'alex.rivers@example.com', 'Alex', 'Rivers', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'alexrivers');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (20, true, '2026-06-15 09:00:00', NULL, 'maya.singh@example.com', 'Maya', 'Singh', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'mayasingh');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (21, true, '2026-06-15 09:00:00', NULL, 'leo.romano@example.com', 'Leo', 'Romano', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'leoromano');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (22, true, '2026-06-15 09:00:00', NULL, 'zara.ahmed@example.com', 'Zara', 'Ahmed', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'zaraahmed');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (23, true, '2026-06-15 09:00:00', NULL, 'ben.foster@example.com', 'Ben', 'Foster', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'benfoster');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (24, true, '2026-06-15 09:00:00', NULL, 'olivia.clarke@example.com', 'Olivia', 'Clarke', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'oliviaclarke');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (25, true, '2026-06-15 09:00:00', NULL, 'sam.taylor@example.com', 'Sam', 'Taylor', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'samtaylor');
INSERT INTO public.users (id, active, created_at, deactivated_at, email, first_name, last_name, password, role, username) VALUES (26, true, '2026-06-15 09:00:00', NULL, 'jordan.lee@example.com', 'Jordan', 'Lee', '$2a$10$Y29aQ/cU8pVIlJgDeH3xJew5xDirvNIcobhXRWmSwzI2G.MH.d1xe', 'PLAYER', 'jordanlee');

-- profiles
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (1, 'MALE', 1, 1);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (2, 'FEMALE', 2, 2);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (3, 'FEMALE', 3, 3);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (4, 'NON_BINARY', 4, 4);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (5, 'PREFER_NOT_TO_SAY', 5, 5);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (6, 'MALE', 6, 6);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (7, 'MALE', 7, 7);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (8, 'FEMALE', 8, 8);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (9, 'FEMALE', 9, 9);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (10, 'NON_BINARY', 10, 10);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (11, 'PREFER_NOT_TO_SAY', 1, 11);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (12, 'MALE', 2, 12);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (13, 'MALE', 3, 13);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (14, 'FEMALE', 4, 14);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (15, 'FEMALE', 5, 15);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (16, 'NON_BINARY', 6, 16);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (17, 'PREFER_NOT_TO_SAY', 7, 17);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (18, 'MALE', 8, 18);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (19, 'MALE', 9, 19);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (20, 'FEMALE', 10, 20);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (21, 'FEMALE', 1, 21);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (22, 'NON_BINARY', 2, 22);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (23, 'PREFER_NOT_TO_SAY', 3, 23);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (24, 'MALE', 4, 24);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (25, 'MALE', 5, 25);
INSERT INTO public.profiles (id, gender, preferred_location_id, user_id) VALUES (26, 'FEMALE', 6, 26);

-- credits (signup bonus for all users)
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (1, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 1);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (2, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 2);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (3, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (4, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 4);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (5, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 5);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (6, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 6);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (7, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 7);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (8, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (9, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 9);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (10, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 10);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (11, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 11);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (12, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 12);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (13, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 13);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (14, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 14);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (15, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 15);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (16, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 16);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (17, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 17);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (18, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 18);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (19, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 19);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (20, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 20);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (21, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 21);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (22, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 22);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (23, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (24, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 24);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (25, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 25);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (26, 100.00, '2026-06-15 09:05:00', 'Signup Credits', 26);

-- games
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (1, '2026-07-20 09:00:00', 'Casual weekly kickabout, all welcome', 60, (CURRENT_DATE + INTERVAL '4 days'), '18:00:00', 'FIVE_A_SIDE', 'MIXED', 10, 'FREE', 0.00, 'NO_REFUND', 'OPEN', 'Tuesday 5-a-side', 4, 2);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (2, '2026-07-20 09:00:00', 'Competitive but friendly', 90, (CURRENT_DATE + INTERVAL '5 days'), '19:00:00', 'SEVEN_A_SIDE', 'MEN', 14, 'PAID_ONLINE', 5.00, 'HOURS_24', 'OPEN', 'Thursday Mens 7s', 5, 1);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (3, '2026-07-20 09:00:00', 'Beginners welcome', 60, (CURRENT_DATE + INTERVAL '6 days'), '17:30:00', 'SIX_A_SIDE', 'WOMEN', 12, 'CASH', 3.00, 'NO_REFUND', 'OPEN', 'Womens 6-a-side', 8, 3);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (4, '2026-07-20 09:00:00', 'Full pitch match', 90, (CURRENT_DATE + INTERVAL '8 days'), '10:00:00', 'ELEVEN_A_SIDE', 'MEN', 22, 'PAID_ONLINE', 8.00, 'HOURS_48', 'OPEN', 'Sunday League 11s', 6, 1);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (5, '2026-07-20 09:00:00', 'Fully booked, get in early next time', 60, (CURRENT_DATE + INTERVAL '9 days'), '20:00:00', 'FIVE_A_SIDE', 'MIXED', 10, 'FREE', 0.00, 'NO_REFUND', 'FULL', 'Manchester Mixed 5s', 9, 6);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (6, '2026-07-20 09:00:00', 'Regular midweek game', 75, (CURRENT_DATE + INTERVAL '11 days'), '18:30:00', 'EIGHT_A_SIDE', 'MEN', 16, 'PAID_ONLINE', 6.00, 'HOURS_24', 'OPEN', '8-a-side Salford', 10, 5);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (7, '2026-07-20 09:00:00', 'New group forming', 60, (CURRENT_DATE + INTERVAL '12 days'), '19:00:00', 'FIVE_A_SIDE', 'WOMEN', 10, 'CASH', 4.00, 'NO_REFUND', 'OPEN', 'Bristol Womens 5s', 11, 9);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (8, '2026-07-20 09:00:00', 'Social football', 60, (CURRENT_DATE + INTERVAL '13 days'), '18:00:00', 'SIX_A_SIDE', 'MIXED', 12, 'FREE', 0.00, 'NO_REFUND', 'OPEN', 'Shoreditch Mixed 6s', 12, 4);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (9, '2026-07-20 09:00:00', 'Weekend league', 90, (CURRENT_DATE + INTERVAL '15 days'), '16:00:00', 'SEVEN_A_SIDE', 'MIXED', 14, 'PAID_ONLINE', 5.50, 'HOURS_24', 'OPEN', 'Birmingham 7s', 13, 11);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (10, '2026-07-20 09:00:00', 'Big pitch action', 90, (CURRENT_DATE + INTERVAL '17 days'), '19:30:00', 'TEN_A_SIDE', 'MEN', 20, 'PAID_ONLINE', 7.00, 'HOURS_48', 'OPEN', 'Shoreditch 10-a-side', 14, 7);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (11, '2026-07-20 09:00:00', 'Headingley regulars', 60, (CURRENT_DATE + INTERVAL '18 days'), '18:00:00', 'FIVE_A_SIDE', 'MIXED', 10, 'CASH', 3.50, 'NO_REFUND', 'OPEN', 'Leeds Mixed 5s', 15, 12);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (12, '2026-07-20 09:00:00', 'Beginner friendly', 60, (CURRENT_DATE + INTERVAL '19 days'), '17:00:00', 'SIX_A_SIDE', 'WOMEN', 12, 'FREE', 0.00, 'NO_REFUND', 'OPEN', 'Glasgow Womens 6s', 16, 14);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (13, '2026-07-20 09:00:00', 'Evening game under lights', 75, (CURRENT_DATE + INTERVAL '20 days'), '18:30:00', 'EIGHT_A_SIDE', 'MIXED', 16, 'PAID_ONLINE', 6.50, 'HOURS_24', 'OPEN', 'Northern Quarter 8s', 17, 10);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (14, '2026-07-20 09:00:00', 'Pitch maintenance, sorry all', 60, (CURRENT_DATE + INTERVAL '22 days'), '20:00:00', 'FIVE_A_SIDE', 'MEN', 10, 'PAID_ONLINE', 4.50, 'NO_REFUND', 'CANCELLED', 'Cancelled Friday 5s', 18, 2);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (15, '2026-07-20 09:00:00', 'Long-running Sunday group', 90, (CURRENT_DATE + INTERVAL '24 days'), '19:00:00', 'SEVEN_A_SIDE', 'MEN', 14, 'CASH', 5.00, 'HOURS_24', 'OPEN', 'Salford Sunday 7s', 19, 5);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (16, '2026-07-20 09:00:00', 'Chilled weekday game', 60, (CURRENT_DATE + INTERVAL '26 days'), '18:00:00', 'SIX_A_SIDE', 'MIXED', 12, 'FREE', 0.00, 'NO_REFUND', 'OPEN', 'Bedminster Mixed 6s', 20, 13);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (17, '2026-07-20 09:00:00', 'Big monthly game', 90, (CURRENT_DATE + INTERVAL '31 days'), '11:00:00', 'ELEVEN_A_SIDE', 'MIXED', 22, 'PAID_ONLINE', 9.00, 'HOURS_48', 'OPEN', 'Full-Pitch Mixed 11s', 21, 7);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (18, '2026-07-20 09:00:00', 'Park pitch, bring water', 60, (CURRENT_DATE + INTERVAL '30 days'), '18:00:00', 'FIVE_A_SIDE', 'MIXED', 10, 'FREE', 0.00, 'NO_REFUND', 'OPEN', 'Clapham Common 5s', 22, 3);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (19, '2026-07-20 09:00:00', 'Great turnout last week', 60, (CURRENT_DATE - INTERVAL '3 days'), '18:00:00', 'FIVE_A_SIDE', 'MIXED', 10, 'FREE', 0.00, 'NO_REFUND', 'COMPLETED', 'Tuesday 5-a-side (last week)', 4, 2);
INSERT INTO public.games (id, created_at, description, duration_minutes, game_date, game_time, game_type, gender_option, max_players, payment_type, price, refund_policy, status, title, organiser_id, pitch_id) VALUES (20, '2026-07-20 09:00:00', 'Finished 4-3', 90, (CURRENT_DATE - INTERVAL '2 days'), '19:00:00', 'SEVEN_A_SIDE', 'MEN', 14, 'PAID_ONLINE', 5.00, 'HOURS_24', 'COMPLETED', 'Thursday Mens 7s (last week)', 5, 1);

-- game_slots + bookings
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (1, '2026-07-20 09:00:01', 'BOOKED', 1, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (1, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 1, 22);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (2, '2026-07-20 09:00:01', 'BOOKED', 1, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (2, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 2, 5);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (3, '2026-07-20 09:00:01', 'BOOKED', 1, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (3, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 3, 1);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (4, '2026-07-20 09:00:01', 'BOOKED', 1, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (4, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 4, 10);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (5, '2026-07-20 09:00:01', 'BOOKED', 1, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (5, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 5, 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (6, '2026-07-20 09:00:01', 'BOOKED', 1, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (6, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 6, 26);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (7, '2026-07-20 09:00:01', 'AVAILABLE', 1, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (8, '2026-07-20 09:00:01', 'AVAILABLE', 1, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (9, '2026-07-20 09:00:01', 'AVAILABLE', 1, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (10, '2026-07-20 09:00:01', 'AVAILABLE', 1, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (11, '2026-07-20 09:00:01', 'BOOKED', 2, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (7, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 11, 6);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (27, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s', 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (12, '2026-07-20 09:00:01', 'BOOKED', 2, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (8, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 12, 25);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (28, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s', 25);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (13, '2026-07-20 09:00:01', 'BOOKED', 2, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (9, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 13, 4);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (29, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s', 4);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (14, '2026-07-20 09:00:01', 'BOOKED', 2, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (10, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 14, 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (30, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s', 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (15, '2026-07-20 09:00:01', 'BOOKED', 2, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (11, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 15, 19);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (31, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s', 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (16, '2026-07-20 09:00:01', 'BOOKED', 2, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (12, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 16, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (32, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (17, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (18, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (19, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (20, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (21, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (22, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (23, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (24, '2026-07-20 09:00:01', 'AVAILABLE', 2, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (25, '2026-07-20 09:00:01', 'BOOKED', 3, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (13, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 25, 20);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (33, -3.00, '2026-07-20 09:05:00', 'Booking payment for Womens 6-a-side', 20);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (26, '2026-07-20 09:00:01', 'BOOKED', 3, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (14, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 26, 15);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (34, -3.00, '2026-07-20 09:05:00', 'Booking payment for Womens 6-a-side', 15);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (27, '2026-07-20 09:00:01', 'BOOKED', 3, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (15, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 27, 2);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (35, -3.00, '2026-07-20 09:05:00', 'Booking payment for Womens 6-a-side', 2);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (28, '2026-07-20 09:00:01', 'BOOKED', 3, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (16, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 28, 1);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (36, -3.00, '2026-07-20 09:05:00', 'Booking payment for Womens 6-a-side', 1);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (29, '2026-07-20 09:00:01', 'BOOKED', 3, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (17, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 29, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (37, -3.00, '2026-07-20 09:05:00', 'Booking payment for Womens 6-a-side', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (30, '2026-07-20 09:00:01', 'BOOKED', 3, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (18, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 30, 7);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (38, -3.00, '2026-07-20 09:05:00', 'Booking payment for Womens 6-a-side', 7);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (31, '2026-07-20 09:00:01', 'AVAILABLE', 3, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (32, '2026-07-20 09:00:01', 'AVAILABLE', 3, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (33, '2026-07-20 09:00:01', 'AVAILABLE', 3, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (34, '2026-07-20 09:00:01', 'AVAILABLE', 3, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (35, '2026-07-20 09:00:01', 'AVAILABLE', 3, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (36, '2026-07-20 09:00:01', 'AVAILABLE', 3, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (37, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (19, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 37, 9);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (39, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (38, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (20, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 38, 18);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (40, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 18);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (39, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (21, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 39, 21);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (41, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 21);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (40, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (22, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 40, 1);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (42, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 1);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (41, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (23, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 41, 19);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (43, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (42, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (24, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 42, 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (44, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (43, '2026-07-20 09:00:01', 'BOOKED', 4, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (25, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 43, 22);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (45, -8.00, '2026-07-20 09:05:00', 'Booking payment for Sunday League 11s', 22);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (44, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (45, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (46, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (47, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (48, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (49, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (50, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (51, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (52, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (53, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (54, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (55, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (56, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (57, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (58, '2026-07-20 09:00:01', 'AVAILABLE', 4, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (59, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (26, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 59, 15);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (60, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (27, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 60, 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (61, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (28, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 61, 16);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (62, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (29, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 62, 20);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (63, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (30, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 63, 10);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (64, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (31, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 64, 1);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (65, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (32, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 65, 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (66, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (33, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 66, 26);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (67, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (34, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 67, 12);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (68, '2026-07-20 09:00:01', 'BOOKED', 5, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (35, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 68, 22);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (69, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (36, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 69, 5);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (46, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 5);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (70, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (37, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 70, 7);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (47, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 7);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (71, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (38, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 71, 12);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (48, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 12);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (72, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (39, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 72, 4);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (49, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 4);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (73, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (40, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 73, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (50, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (74, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (41, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 74, 14);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (51, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 14);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (75, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (42, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 75, 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (52, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (76, '2026-07-20 09:00:01', 'BOOKED', 6, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (43, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 76, 13);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (53, -6.00, '2026-07-20 09:05:00', 'Booking payment for 8-a-side Salford', 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (77, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (78, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (79, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (80, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (81, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (82, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (83, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (84, '2026-07-20 09:00:01', 'AVAILABLE', 6, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (85, '2026-07-20 09:00:01', 'BOOKED', 7, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (44, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 85, 13);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (54, -4.00, '2026-07-20 09:05:00', 'Booking payment for Bristol Womens 5s', 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (86, '2026-07-20 09:00:01', 'BOOKED', 7, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (45, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 86, 21);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (55, -4.00, '2026-07-20 09:05:00', 'Booking payment for Bristol Womens 5s', 21);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (87, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (88, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (89, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (90, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (91, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (92, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (93, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (94, '2026-07-20 09:00:01', 'AVAILABLE', 7, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (95, '2026-07-20 09:00:01', 'BOOKED', 8, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (46, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 95, 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (96, '2026-07-20 09:00:01', 'BOOKED', 8, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (47, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 96, 2);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (97, '2026-07-20 09:00:01', 'BOOKED', 8, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (48, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 97, 25);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (98, '2026-07-20 09:00:01', 'BOOKED', 8, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (49, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 98, 16);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (99, '2026-07-20 09:00:01', 'BOOKED', 8, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (50, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 99, 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (100, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (101, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (102, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (103, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (104, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (105, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (106, '2026-07-20 09:00:01', 'AVAILABLE', 8, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (107, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (51, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 107, 4);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (56, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 4);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (108, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (52, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 108, 14);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (57, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 14);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (109, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (53, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 109, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (58, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (110, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (54, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 110, 19);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (59, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (111, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (55, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 111, 10);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (60, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 10);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (112, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (56, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 112, 21);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (61, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 21);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (113, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (57, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 113, 12);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (62, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 12);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (114, '2026-07-20 09:00:01', 'BOOKED', 9, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (58, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 114, 7);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (63, -5.50, '2026-07-20 09:05:00', 'Booking payment for Birmingham 7s', 7);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (115, '2026-07-20 09:00:01', 'AVAILABLE', 9, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (116, '2026-07-20 09:00:01', 'AVAILABLE', 9, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (117, '2026-07-20 09:00:01', 'AVAILABLE', 9, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (118, '2026-07-20 09:00:01', 'AVAILABLE', 9, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (119, '2026-07-20 09:00:01', 'AVAILABLE', 9, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (120, '2026-07-20 09:00:01', 'AVAILABLE', 9, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (121, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (59, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 121, 24);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (64, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 24);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (122, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (60, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 122, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (65, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (123, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (61, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 123, 2);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (66, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 2);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (124, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (62, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 124, 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (67, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (125, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (63, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 125, 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (68, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (126, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (64, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 126, 10);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (69, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 10);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (127, '2026-07-20 09:00:01', 'BOOKED', 10, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (65, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 127, 25);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (70, -7.00, '2026-07-20 09:05:00', 'Booking payment for Shoreditch 10-a-side', 25);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (128, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (129, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (130, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (131, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (132, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (133, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (134, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (135, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (136, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (137, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (138, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (139, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (140, '2026-07-20 09:00:01', 'AVAILABLE', 10, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (141, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (66, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 141, 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (71, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (142, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (67, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 142, 4);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (72, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 4);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (143, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (68, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 143, 13);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (73, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (144, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (69, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 144, 9);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (74, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (145, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (70, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 145, 16);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (75, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 16);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (146, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (71, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 146, 12);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (76, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 12);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (147, '2026-07-20 09:00:01', 'BOOKED', 11, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (72, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 147, 6);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (77, -3.50, '2026-07-20 09:05:00', 'Booking payment for Leeds Mixed 5s', 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (148, '2026-07-20 09:00:01', 'AVAILABLE', 11, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (149, '2026-07-20 09:00:01', 'AVAILABLE', 11, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (150, '2026-07-20 09:00:01', 'AVAILABLE', 11, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (151, '2026-07-20 09:00:01', 'BOOKED', 12, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (73, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 151, 12);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (152, '2026-07-20 09:00:01', 'BOOKED', 12, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (74, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 152, 7);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (153, '2026-07-20 09:00:01', 'BOOKED', 12, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (75, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 153, 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (154, '2026-07-20 09:00:01', 'BOOKED', 12, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (76, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 154, 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (155, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (156, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (157, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (158, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (159, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (160, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (161, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (162, '2026-07-20 09:00:01', 'AVAILABLE', 12, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (163, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (77, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 163, 24);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (78, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 24);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (164, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (78, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 164, 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (79, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (165, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (79, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 165, 22);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (80, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 22);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (166, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (80, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 166, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (81, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (167, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (81, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 167, 21);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (82, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 21);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (168, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (82, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 168, 6);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (83, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (169, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (83, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 169, 19);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (84, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (170, '2026-07-20 09:00:01', 'BOOKED', 13, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (84, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 170, 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (85, -6.50, '2026-07-20 09:05:00', 'Booking payment for Northern Quarter 8s', 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (171, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (172, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (173, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (174, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (175, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (176, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (177, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (178, '2026-07-20 09:00:01', 'AVAILABLE', 13, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (179, '2026-07-20 09:00:01', 'BOOKED', 14, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (85, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 179, 6);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (86, -4.50, '2026-07-20 09:05:00', 'Booking payment for Cancelled Friday 5s', 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (180, '2026-07-20 09:00:01', 'BOOKED', 14, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (86, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 180, 15);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (87, -4.50, '2026-07-20 09:05:00', 'Booking payment for Cancelled Friday 5s', 15);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (181, '2026-07-20 09:00:01', 'BOOKED', 14, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (87, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 181, 13);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (88, -4.50, '2026-07-20 09:05:00', 'Booking payment for Cancelled Friday 5s', 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (182, '2026-07-20 09:00:01', 'BOOKED', 14, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (88, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 182, 9);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (89, -4.50, '2026-07-20 09:05:00', 'Booking payment for Cancelled Friday 5s', 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (183, '2026-07-20 09:00:01', 'AVAILABLE', 14, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (184, '2026-07-20 09:00:01', 'AVAILABLE', 14, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (185, '2026-07-20 09:00:01', 'AVAILABLE', 14, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (186, '2026-07-20 09:00:01', 'AVAILABLE', 14, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (187, '2026-07-20 09:00:01', 'AVAILABLE', 14, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (188, '2026-07-20 09:00:01', 'AVAILABLE', 14, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (189, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (89, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 189, 22);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (90, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 22);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (190, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (90, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 190, 24);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (91, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 24);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (191, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (91, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 191, 18);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (92, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 18);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (192, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (92, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 192, 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (93, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (193, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (93, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 193, 11);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (94, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 11);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (194, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (94, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 194, 2);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (95, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 2);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (195, '2026-07-20 09:00:01', 'BOOKED', 15, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (95, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 195, 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (96, -5.00, '2026-07-20 09:05:00', 'Booking payment for Salford Sunday 7s', 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (196, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (197, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (198, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (199, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (200, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (201, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (202, '2026-07-20 09:00:01', 'AVAILABLE', 15, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (203, '2026-07-20 09:00:01', 'BOOKED', 16, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (96, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 203, 2);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (204, '2026-07-20 09:00:01', 'BOOKED', 16, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (97, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 204, 11);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (205, '2026-07-20 09:00:01', 'BOOKED', 16, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (98, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 205, 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (206, '2026-07-20 09:00:01', 'BOOKED', 16, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (99, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 206, 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (207, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (208, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (209, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (210, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (211, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (212, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (213, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (214, '2026-07-20 09:00:01', 'AVAILABLE', 16, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (215, '2026-07-20 09:00:01', 'BOOKED', 17, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (100, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 215, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (97, -9.00, '2026-07-20 09:05:00', 'Booking payment for Full-Pitch Mixed 11s', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (216, '2026-07-20 09:00:01', 'BOOKED', 17, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (101, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 216, 7);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (98, -9.00, '2026-07-20 09:05:00', 'Booking payment for Full-Pitch Mixed 11s', 7);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (217, '2026-07-20 09:00:01', 'BOOKED', 17, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (102, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 217, 19);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (99, -9.00, '2026-07-20 09:05:00', 'Booking payment for Full-Pitch Mixed 11s', 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (218, '2026-07-20 09:00:01', 'BOOKED', 17, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (103, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 218, 11);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (100, -9.00, '2026-07-20 09:05:00', 'Booking payment for Full-Pitch Mixed 11s', 11);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (219, '2026-07-20 09:00:01', 'BOOKED', 17, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (104, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 219, 25);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (101, -9.00, '2026-07-20 09:05:00', 'Booking payment for Full-Pitch Mixed 11s', 25);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (220, '2026-07-20 09:00:01', 'BOOKED', 17, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (105, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 220, 16);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (102, -9.00, '2026-07-20 09:05:00', 'Booking payment for Full-Pitch Mixed 11s', 16);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (221, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (222, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (223, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (224, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (225, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (226, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (227, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (228, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (229, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (230, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (231, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (232, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (233, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (234, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (235, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (236, '2026-07-20 09:00:01', 'AVAILABLE', 17, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (237, '2026-07-20 09:00:01', 'BOOKED', 18, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (106, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 237, 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (238, '2026-07-20 09:00:01', 'BOOKED', 18, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (107, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 238, 21);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (239, '2026-07-20 09:00:01', 'BOOKED', 18, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (108, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 239, 15);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (240, '2026-07-20 09:00:01', 'BOOKED', 18, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (109, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 240, 5);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (241, '2026-07-20 09:00:01', 'BOOKED', 18, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (110, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 241, 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (242, '2026-07-20 09:00:01', 'AVAILABLE', 18, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (243, '2026-07-20 09:00:01', 'AVAILABLE', 18, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (244, '2026-07-20 09:00:01', 'AVAILABLE', 18, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (245, '2026-07-20 09:00:01', 'AVAILABLE', 18, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (246, '2026-07-20 09:00:01', 'AVAILABLE', 18, 0);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (247, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (111, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 247, 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (248, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (112, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 248, 9);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (249, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (113, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 249, 19);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (250, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (114, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 250, 24);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (251, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (115, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 251, 10);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (252, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (116, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 252, 20);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (253, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (117, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 253, 15);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (254, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (118, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 254, 14);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (255, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (119, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 255, 13);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (256, '2026-07-20 09:00:01', 'BOOKED', 19, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (120, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 256, 25);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (257, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (121, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 257, 6);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (103, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 6);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (258, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (122, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 258, 18);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (104, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 18);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (259, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (123, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 259, 17);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (105, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 17);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (260, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (124, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 260, 3);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (106, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 3);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (261, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (125, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 261, 2);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (107, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 2);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (262, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (126, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 262, 4);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (108, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 4);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (263, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (127, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 263, 26);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (109, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 26);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (264, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (128, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 264, 7);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (110, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 7);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (265, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (129, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 265, 15);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (111, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 15);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (266, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (130, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 266, 23);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (112, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 23);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (267, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (131, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 267, 8);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (113, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 8);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (268, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (132, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 268, 16);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (114, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 16);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (269, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (133, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 269, 11);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (115, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 11);
INSERT INTO public.game_slots (id, created_at, status, game_id, version) VALUES (270, '2026-07-20 09:00:01', 'BOOKED', 20, 1);
INSERT INTO public.bookings (id, confirmed_at, created_at, expires_at, status, withdrawn_at, slot_id, user_id) VALUES (134, '2026-07-20 09:05:00', '2026-07-20 09:03:00', '2026-07-20 09:06:00', 'CONFIRMED', NULL, 270, 9);
INSERT INTO public.credits (id, amount, created_at, reason, user_id) VALUES (116, -5.00, '2026-07-20 09:05:00', 'Booking payment for Thursday Mens 7s (last week)', 9);

-- sequences
SELECT pg_catalog.setval('public.locations_id_seq', 10, true);
SELECT pg_catalog.setval('public.pitches_id_seq', 14, true);
SELECT pg_catalog.setval('public.users_id_seq', 26, true);
SELECT pg_catalog.setval('public.profiles_id_seq', 26, true);
SELECT pg_catalog.setval('public.credits_id_seq', 116, true);
SELECT pg_catalog.setval('public.games_id_seq', 20, true);
SELECT pg_catalog.setval('public.game_slots_id_seq', 270, true);
SELECT pg_catalog.setval('public.bookings_id_seq', 134, true);


-- A game with 9 of its 10 slots already booked (1 available slot for 2 users to compete for) to test the booking/hold behaviour under contention
-- ============================================================
WITH new_game AS (
INSERT INTO public.games
(pitch_id, organiser_id, title, description, game_date, game_time,
 duration_minutes, game_type, gender_option, max_players, price,
 payment_type, refund_policy, status, created_at)
SELECT p.id, u.id, 'Tuesday Evening 5-a-side',
       'Friendly weekly kickabout, one spot left!', (CURRENT_DATE + INTERVAL '66 days'), TIME '18:00:00',
       60, 'FIVE_A_SIDE', 'MIXED', 10, 0.00, 'FREE', 'NO_REFUND', 'OPEN', now()
FROM public.pitches p
         JOIN public.users u ON u.email = 'jon@example.com'
WHERE p.name = 'Pitch A'
    RETURNING id
),
filler AS (
SELECT id AS user_id, row_number() OVER () AS rn
FROM public.users
WHERE email IN (
    'sam.taylor@example.com','jordan.lee@example.com','priya.patel@example.com',
    'chidi.okafor@example.com','emma.wilson@example.com','ryan.oconnor@example.com',
    'fatima.hussain@example.com','jake.turner@example.com','sofia.martinez@example.com'
    )
    ),
    booked_slots AS (
INSERT INTO public.game_slots (game_id, status, created_at, version)
SELECT ng.id, 'BOOKED', now(), 1 FROM new_game ng CROSS JOIN filler
    RETURNING id
    ),
    booked_slots_ranked AS (
SELECT id AS slot_id, row_number() OVER () AS rn FROM booked_slots
    ),
    new_bookings AS (
INSERT INTO public.bookings (slot_id, user_id, status, created_at, confirmed_at, amount_paid)
SELECT bsr.slot_id, f.user_id, 'CONFIRMED', now(), now(), 0.00
FROM booked_slots_ranked bsr JOIN filler f ON f.rn = bsr.rn
    RETURNING id
    )
INSERT INTO public.game_slots (game_id, status, created_at, version)
SELECT id, 'AVAILABLE', now(), 0 FROM new_game;

-- A women-only game to test the gender validation matrix
-- ============================================================
WITH new_game AS (
INSERT INTO public.games
(pitch_id, organiser_id, title, description, game_date, game_time,
 duration_minutes, game_type, gender_option, max_players, price,
 payment_type, refund_policy, status, created_at)
SELECT p.id, u.id, 'Saturday Women''s 6-a-side',
       'Womens-only game, beginners welcome', (CURRENT_DATE + INTERVAL '67 days'), TIME '19:30:00',
       60, 'SIX_A_SIDE', 'WOMEN', 12, 0.00, 'FREE', 'NO_REFUND', 'OPEN', now()
FROM public.pitches p
         JOIN public.users u ON u.email = 'tom.evans@example.com'
WHERE p.name = 'Pitch C'
    RETURNING id
)
INSERT INTO public.game_slots (game_id, status, created_at, version)
SELECT id, 'AVAILABLE', now(), 0 FROM new_game, generate_series(1, 12);


-- A paid-online game with a 24-hour refund policy to test the credit refunds when withdrawing from a game.
-- ============================================================
WITH new_game AS (
INSERT INTO public.games
(pitch_id, organiser_id, title, description, game_date, game_time,
 duration_minutes, game_type, gender_option, max_players, price,
 payment_type, refund_policy, status, created_at)
SELECT p.id, u.id, 'Sunday 7-a-side League',
       'Competitive but friendly, pay online to book your spot', (CURRENT_DATE + INTERVAL '69 days'), TIME '18:00:00',
       90, 'SEVEN_A_SIDE', 'MIXED', 14, 6.00, 'PAID_ONLINE', 'HOURS_24', 'OPEN', now()
FROM public.pitches p
         JOIN public.users u ON u.email = 'sam@example.com'
WHERE p.name = 'Pitch G'
    RETURNING id
)
INSERT INTO public.game_slots (game_id, status, created_at, version)
SELECT id, 'AVAILABLE', now(), 0 FROM new_game, generate_series(1, 14);
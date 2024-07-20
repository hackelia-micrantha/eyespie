SET session_replication_role = replica;

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1 (Ubuntu 15.1-1.pgdg20.04+1)
-- Dumped by pg_dump version 15.7 (Ubuntu 15.7-1.pgdg20.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


--
-- Data for Name: users; Type: TABLE DATA; Schema: auth; Owner: supabase_auth_admin
--

INSERT INTO "auth"."users" ("instance_id", "id", "aud", "role", "email", "encrypted_password", "email_confirmed_at", "invited_at", "confirmation_token", "confirmation_sent_at", "recovery_token", "recovery_sent_at", "email_change_token_new", "email_change", "email_change_sent_at", "last_sign_in_at", "raw_app_meta_data", "raw_user_meta_data", "is_super_admin", "created_at", "updated_at", "phone", "phone_confirmed_at", "phone_change", "phone_change_token", "phone_change_sent_at", "email_change_token_current", "email_change_confirm_status", "banned_until", "reauthentication_token", "reauthentication_sent_at", "is_sso_user", "deleted_at", "is_anonymous") VALUES
	('00000000-0000-0000-0000-000000000000', '8958a62d-991f-4ed4-b51a-b68a2f35a65c', 'authenticated', 'authenticated', 'ryjen@duck.com', '$2a$10$dJblugDRyH4i1RnHCo2t5utp2kzmd07iVswMtDTlAXewpviiew1DO', '2024-07-19 04:32:39.259357+00', NULL, '', NULL, '', NULL, '', '', NULL, '2024-07-20 06:01:15.522401+00', '{"provider": "email", "providers": ["email"]}', '{}', NULL, '2024-07-19 04:32:39.253271+00', '2024-07-20 06:01:15.525789+00', NULL, NULL, '', '', NULL, '', 0, NULL, '', NULL, false, NULL, false);


--
-- Data for Name: identities; Type: TABLE DATA; Schema: auth; Owner: supabase_auth_admin
--

INSERT INTO "auth"."identities" ("provider_id", "user_id", "identity_data", "provider", "last_sign_in_at", "created_at", "updated_at", "id") VALUES
	('8958a62d-991f-4ed4-b51a-b68a2f35a65c', '8958a62d-991f-4ed4-b51a-b68a2f35a65c', '{"sub": "8958a62d-991f-4ed4-b51a-b68a2f35a65c", "email": "ryjen@duck.com", "email_verified": false, "phone_verified": false}', 'email', '2024-07-19 04:32:39.256421+00', '2024-07-19 04:32:39.256473+00', '2024-07-19 04:32:39.256473+00', 'f9efa59f-e7f6-4fc8-b666-c16c03e8f7c8');


--
-- Data for Name: Game; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "public"."Game" ("id", "created_at", "name", "expires", "min_players", "max_players", "min_things", "turn_duration", "max_things") VALUES
	('10444bc1-abd6-46d9-bac8-bb47dc38130a', '2023-04-21 07:57:41.065292+00', 'Test Game', '2023-07-04 00:05:05+00', 1, 10, 3, '8h', 10);


--
-- Data for Name: Player; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "public"."Player" ("id", "created_at", "first_name", "last_name", "nick_name", "total_score", "user_id", "last_location") VALUES
	('a3f8a59a-33a4-4e79-bccb-0a008df43cb4', '2023-04-21 07:58:23+00', 'Ryan', 'Jennings', 'ryjen', 0, '8958a62d-991f-4ed4-b51a-b68a2f35a65c', '(49.2608724,-123.113952)');


--
-- Data for Name: GamePlayer; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "public"."GamePlayer" ("created_at", "player_id", "game_id", "score") VALUES
	('2023-04-29 07:53:34.607337+00', 'a3f8a59a-33a4-4e79-bccb-0a008df43cb4', '10444bc1-abd6-46d9-bac8-bb47dc38130a', NULL);


--
-- Data for Name: Thing; Type: TABLE DATA; Schema: public; Owner: postgres
--

--
-- Data for Name: GameThing; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: Guess; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: PlayerFriend; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: buckets; Type: TABLE DATA; Schema: storage; Owner: supabase_storage_admin
--


--
-- Data for Name: objects; Type: TABLE DATA; Schema: storage; Owner: supabase_storage_admin
--


--
-- Data for Name: s3_multipart_uploads; Type: TABLE DATA; Schema: storage; Owner: supabase_storage_admin
--



--
-- Data for Name: s3_multipart_uploads_parts; Type: TABLE DATA; Schema: storage; Owner: supabase_storage_admin
--



--
-- Data for Name: hooks; Type: TABLE DATA; Schema: supabase_functions; Owner: supabase_functions_admin
--



--
-- Data for Name: secrets; Type: TABLE DATA; Schema: vault; Owner: supabase_admin
--



--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE SET; Schema: auth; Owner: supabase_auth_admin
--


--
-- Name: key_key_id_seq; Type: SEQUENCE SET; Schema: pgsodium; Owner: supabase_admin
--



--
-- Name: hooks_id_seq; Type: SEQUENCE SET; Schema: supabase_functions; Owner: supabase_functions_admin
--


--
-- PostgreSQL database dump complete
--

RESET ALL;


INSERT INTO "auth"."users"
    ---("instance_id", "id", "aud", "role", "email", "encrypted_password", "email_confirmed_at", "invited_at", "confirmation_token", "confirmation_sent_at", "recovery_token", "recovery_sent_at", "email_change_token_new", "email_change", "email_change_sent_at", "last_sign_in_at", "raw_app_meta_data", "raw_user_meta_data", "is_super_admin", "created_at", "updated_at", "phone", "phone_confirmed_at", "phone_change", "phone_change_token", "phone_change_sent_at", "email_change_token_current", "email_change_confirm_status", "banned_until", "reauthentication_token", "reauthentication_sent_at", "is_sso_user", "deleted_at", "is_anonymous")
	VALUES ('00000000-0000-0000-0000-000000000000', 'f77c3a35-1236-49bc-add5-dad3b806da83', 'authenticated', 'authenticated', 'eyespie@micrantha.test', '$2a$10$Ks8JtoZsWkz.3JgTUKrmLOvTsF2eUe1R53hD1fUR7UTyWLWZEOzUS', '2025-05-03 06:16:09.071787+00', NULL, '', NULL, '', NULL, '', '', NULL, '2025-05-03 07:20:09.199931+00', '{"provider": "email", "providers": ["email"]}', '{"email_verified": true}', NULL, '2025-05-03 06:16:09.068173+00', '2025-05-03 07:20:09.201562+00', NULL, NULL, '', '', NULL, DEFAULT, '', 0, NULL, '', NULL, false, NULL, false);

INSERT INTO "auth"."identities"
    ---("provider_id", "user_id", "identity_data", "provider", "last_sign_in_at", "created_at", "updated_at", "id")
	VALUES ('f77c3a35-1236-49bc-add5-dad3b806da83', 'f77c3a35-1236-49bc-add5-dad3b806da83', '{"sub": "f77c3a35-1236-49bc-add5-dad3b806da83", "email": "eyespie@micrantha.test", "email_verified": false, "phone_verified": false}', 'email', '2025-05-03 06:16:09.069826+00', '2025-05-03 06:16:09.069859+00', '2025-05-03 06:16:09.069859+00', DEFAULT, '35fdc362-024e-4599-a03a-29e68799cd41');

INSERT INTO "public"."Game" ("id", "created_at", "name", "expires", "min_players", "max_players", "min_things", "turn_duration", "max_things") VALUES
	('10444bc1-abd6-46d9-bac8-bb47dc38130a', '2023-04-21 07:57:41.065292+00', 'Test Game', '2023-07-04 00:05:05+00', 1, 10, 3, '8h', 10);

INSERT INTO "public"."Player" ("id", "created_at", "first_name", "last_name", "nick_name", "total_score", "user_id", "last_location") VALUES
	('a3f8a59a-33a4-4e79-bccb-0a008df43cb4', '2023-04-21 07:58:23+00', 'Test', 'Testerson', 'testicle', 0, 'f77c3a35-1236-49bc-add5-dad3b806da83', '(49.2608724,-123.113952)');

INSERT INTO "public"."GamePlayer" ("created_at", "player_id", "game_id", "score") VALUES
	('2023-04-29 07:53:34.607337+00', 'a3f8a59a-33a4-4e79-bccb-0a008df43cb4', '10444bc1-abd6-46d9-bac8-bb47dc38130a', NULL);

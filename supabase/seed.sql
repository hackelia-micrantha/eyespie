--
-- Data for Name: Game; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Game" (id, created_at, name, expires, min_players, max_players, min_things, turn_duration, max_things) FROM stdin;
10444bc1-abd6-46d9-bac8-bb47dc38130a	'2023-04-21 07:57:41.065292+00'	Test Game	'2023-07-04 00:05:05+00'	1	10	3	8h	10
\.


--
-- Data for Name: Player; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Player" (id, created_at, first_name, last_name, nick_name, total_score, user_id, last_location) FROM stdin;
a3f8a59a-33a4-4e79-bccb-0a008df43cb4	'2023-04-21 07:58:23+00'	Ryan	Jennings	ryjen	0	29e952a9-57db-4ddd-a22d-447caa64fdc3	(49.2608724,-123.113952)
\.


--
-- Data for Name: GamePlayer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."GamePlayer" (created_at, player_id, game_id, score) FROM stdin;
'2023-04-29 07:53:34.607337+00'	a3f8a59a-33a4-4e79-bccb-0a008df43cb4	'10444bc1-abd6-46d9-bac8-bb47dc38130a'	0
\.


--
-- Data for Name: Thing; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Thing" (id, created_at, name, proof, guessed, created_by, location, "imageUrl", embedding) FROM stdin;
092b1bcd-d4ae-4a4f-a294-1c3837ea8fda	'2023-04-27 22:34:50+00'	Test Thing	''	''	a3f8a59a-33a4-4e79-bccb-0a008df43cb4	(49.319981,-123.072411)	https://ipvxwipixdtyemipfogb.supabase.co/storage/v1/render/image/authenticated/images/a3f8a59a-33a4-4e79-bccb-0a008df43cb4/d519293b-d6c8-4696-973d-ff0a261db44d.jpg	''
1a4ce05d-82f4-4d1c-bf79-8b3d71d21127	'2023-06-10 03:29:37.284001+00'	arstrsta	{"labels": [{"data": "Room", "confidence": 0.8609293}, {"data": "Shelf", "confidence": 0.78567314}], "location": {}}	''	a3f8a59a-33a4-4e79-bccb-0a008df43cb4	(49.2608724,-123.113952)	https://ipvxwipixdtyemipfogb.supabase.co/storage/v1/render/image/authenticated/images/a3f8a59a-33a4-4e79-bccb-0a008df43cb4/arstrsta.jpg	''
\.


--
-- Data for Name: GameThing; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."GameThing" (created_at, thing_id, game_id) FROM stdin;
'2023-04-27 22:35:16.242493+00'	092b1bcd-d4ae-4a4f-a294-1c3837ea8fda	10444bc1-abd6-46d9-bac8-bb47dc38130a
\.


--
-- Data for Name: Guess; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Guess" (created_at, created_by, thing_id, correct) FROM stdin;
\.

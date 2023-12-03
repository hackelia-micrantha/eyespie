--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1
-- Dumped by pg_dump version 15.1 (Debian 15.1-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', 'tiger', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pg_net; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pg_net" WITH SCHEMA "extensions";


--
-- Name: pgsodium; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pgsodium" WITH SCHEMA "pgsodium";


--
-- Name: address_standardizer; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "address_standardizer" WITH SCHEMA "extensions";


--
-- Name: cube; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "cube" WITH SCHEMA "extensions";


--
-- Name: earthdistance; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "earthdistance" WITH SCHEMA "extensions";

CREATE SCHEMA IF NOT EXISTS "tiger";


--
-- Name: fuzzystrmatch; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "fuzzystrmatch" WITH SCHEMA "tiger";


--
-- Name: pg_graphql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pg_graphql" WITH SCHEMA "graphql";


--
-- Name: pg_hashids; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pg_hashids" WITH SCHEMA "extensions";


--
-- Name: pg_stat_statements; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pg_stat_statements" WITH SCHEMA "extensions";


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pgcrypto" WITH SCHEMA "extensions";


--
-- Name: pgjwt; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "pgjwt" WITH SCHEMA "extensions";


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "postgis" WITH SCHEMA "tiger";


--
-- Name: postgis_tiger_geocoder; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "postgis_tiger_geocoder" WITH SCHEMA "tiger";


--
-- Name: supabase_vault; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "supabase_vault" WITH SCHEMA "vault";


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA "extensions";


--
-- Name: vector; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "vector" WITH SCHEMA "extensions";


SET default_tablespace = '';

SET default_table_access_method = "heap";

--
-- Name: Thing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."Thing" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"(),
    "name" character varying,
    "proof" "jsonb",
    "guessed" boolean,
    "created_by" "uuid" NOT NULL,
    "location" "point",
    "imageUrl" "text",
    "embedding" "extensions"."vector"(1536)
);


ALTER TABLE "public"."Thing" OWNER TO "postgres";

--
-- Name: thingsnearby(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION "public"."thingsnearby"("distance" double precision, "latitude" double precision, "longitude" double precision) RETURNS SETOF "public"."Thing"
    LANGUAGE "sql" STABLE
    AS $$
  SELECT * FROM "Thing"
  WHERE (location <@> point(latitude, longitude)) <= distance
$$;


ALTER FUNCTION "public"."thingsnearby"("distance" double precision, "latitude" double precision, "longitude" double precision) OWNER TO "postgres";

--
-- Name: Game; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."Game" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "name" character varying NOT NULL,
    "expires" timestamp with time zone NOT NULL,
    "min_players" integer DEFAULT 1 NOT NULL,
    "max_players" integer DEFAULT 1 NOT NULL,
    "min_things" integer DEFAULT 3 NOT NULL,
    "turn_duration" "text" DEFAULT '8h'::"text" NOT NULL,
    "max_things" integer DEFAULT 10 NOT NULL
);


ALTER TABLE "public"."Game" OWNER TO "postgres";

--
-- Name: GamePlayer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."GamePlayer" (
    "created_at" timestamp with time zone DEFAULT "now"(),
    "player_id" "uuid" NOT NULL,
    "game_id" "uuid" NOT NULL,
    "score" integer
);


ALTER TABLE "public"."GamePlayer" OWNER TO "postgres";

--
-- Name: GameThing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."GameThing" (
    "created_at" timestamp with time zone DEFAULT "now"(),
    "thing_id" "uuid" NOT NULL,
    "game_id" "uuid" NOT NULL
);


ALTER TABLE "public"."GameThing" OWNER TO "postgres";

--
-- Name: Guess; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."Guess" (
    "created_at" timestamp with time zone DEFAULT "now"(),
    "created_by" "uuid" NOT NULL,
    "thing_id" "uuid" NOT NULL,
    "correct" boolean
);


ALTER TABLE "public"."Guess" OWNER TO "postgres";

--
-- Name: Player; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."Player" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"(),
    "first_name" character varying,
    "last_name" character varying,
    "nick_name" character varying,
    "total_score" bigint DEFAULT '0'::bigint,
    "user_id" "uuid",
    "last_location" "point"
);


ALTER TABLE "public"."Player" OWNER TO "postgres";

--
-- Name: PlayerFriend; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."PlayerFriend" (
    "id" "uuid" NOT NULL,
    "player_id" "uuid" NOT NULL,
    "friend_id" "uuid" NOT NULL
);


ALTER TABLE "public"."PlayerFriend" OWNER TO "postgres";

--
-- Name: PlayerFriend PlayerFriend_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."PlayerFriend"
    ADD CONSTRAINT "PlayerFriend_pkey" PRIMARY KEY ("id");


--
-- Name: GamePlayer gamePlayers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."GamePlayer"
    ADD CONSTRAINT "gamePlayers_pkey" PRIMARY KEY ("player_id", "game_id");


--
-- Name: GameThing gameThings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."GameThing"
    ADD CONSTRAINT "gameThings_pkey" PRIMARY KEY ("thing_id", "game_id");


--
-- Name: Game games_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Game"
    ADD CONSTRAINT "games_pkey" PRIMARY KEY ("id");


--
-- Name: Guess guesses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Guess"
    ADD CONSTRAINT "guesses_pkey" PRIMARY KEY ("thing_id", "created_by");


--
-- Name: Player players_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Player"
    ADD CONSTRAINT "players_pkey" PRIMARY KEY ("id");


--
-- Name: Thing things_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Thing"
    ADD CONSTRAINT "things_pkey" PRIMARY KEY ("id");


--
-- Name: GamePlayer GamePlayer_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."GamePlayer"
    ADD CONSTRAINT "GamePlayer_game_id_fkey" FOREIGN KEY ("game_id") REFERENCES "public"."Game"("id") ON DELETE CASCADE;


--
-- Name: GamePlayer GamePlayer_player_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."GamePlayer"
    ADD CONSTRAINT "GamePlayer_player_id_fkey" FOREIGN KEY ("player_id") REFERENCES "public"."Player"("id") ON DELETE CASCADE;


--
-- Name: GameThing GameThing_game_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."GameThing"
    ADD CONSTRAINT "GameThing_game_id_fkey" FOREIGN KEY ("game_id") REFERENCES "public"."Game"("id") ON DELETE CASCADE;


--
-- Name: GameThing GameThing_thing_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."GameThing"
    ADD CONSTRAINT "GameThing_thing_id_fkey" FOREIGN KEY ("thing_id") REFERENCES "public"."Thing"("id") ON DELETE CASCADE;


--
-- Name: Guess Guess_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Guess"
    ADD CONSTRAINT "Guess_created_by_fkey" FOREIGN KEY ("created_by") REFERENCES "public"."Player"("id") ON DELETE SET NULL;


--
-- Name: Guess Guess_thing_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Guess"
    ADD CONSTRAINT "Guess_thing_id_fkey" FOREIGN KEY ("thing_id") REFERENCES "public"."Thing"("id") ON DELETE CASCADE;


--
-- Name: PlayerFriend PlayerFriend_friend_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."PlayerFriend"
    ADD CONSTRAINT "PlayerFriend_friend_id_fkey" FOREIGN KEY ("friend_id") REFERENCES "public"."Player"("id") ON DELETE CASCADE;


--
-- Name: PlayerFriend PlayerFriend_player_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."PlayerFriend"
    ADD CONSTRAINT "PlayerFriend_player_id_fkey" FOREIGN KEY ("player_id") REFERENCES "public"."Player"("id") ON DELETE CASCADE;


--
-- Name: Thing Thing_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Thing"
    ADD CONSTRAINT "Thing_created_by_fkey" FOREIGN KEY ("created_by") REFERENCES "public"."Player"("id") ON DELETE CASCADE;


--
-- Name: Game; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."Game" ENABLE ROW LEVEL SECURITY;

--
-- Name: GamePlayer; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."GamePlayer" ENABLE ROW LEVEL SECURITY;

--
-- Name: GameThing; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."GameThing" ENABLE ROW LEVEL SECURITY;

--
-- Name: Guess; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."Guess" ENABLE ROW LEVEL SECURITY;

--
-- Name: Player; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."Player" ENABLE ROW LEVEL SECURITY;

--
-- Name: PlayerFriend; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."PlayerFriend" ENABLE ROW LEVEL SECURITY;

--
-- Name: Thing; Type: ROW SECURITY; Schema: public; Owner: postgres
--

ALTER TABLE "public"."Thing" ENABLE ROW LEVEL SECURITY;

--
-- Name: Game user_policy; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "user_policy" ON "public"."Game" TO "authenticated" USING (true);


--
-- Name: GamePlayer user_policy; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "user_policy" ON "public"."GamePlayer" TO "authenticated" USING (true);


--
-- Name: GameThing user_policy; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "user_policy" ON "public"."GameThing" TO "authenticated" USING (true);


--
-- Name: Guess user_policy; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "user_policy" ON "public"."Guess" TO "authenticated" USING (true);


--
-- Name: Player user_policy; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "user_policy" ON "public"."Player" TO "authenticated" USING (true);


--
-- Name: Thing user_policy; Type: POLICY; Schema: public; Owner: postgres
--

CREATE POLICY "user_policy" ON "public"."Thing" TO "authenticated" USING (true);


--
-- Name: SCHEMA "net"; Type: ACL; Schema: -; Owner: supabase_admin
--

-- GRANT USAGE ON SCHEMA "net" TO "supabase_functions_admin";
-- GRANT USAGE ON SCHEMA "net" TO "anon";
-- GRANT USAGE ON SCHEMA "net" TO "authenticated";
-- GRANT USAGE ON SCHEMA "net" TO "service_role";


--
-- Name: SCHEMA "public"; Type: ACL; Schema: -; Owner: pg_database_owner
--

GRANT USAGE ON SCHEMA "public" TO "postgres";
GRANT USAGE ON SCHEMA "public" TO "anon";
GRANT USAGE ON SCHEMA "public" TO "authenticated";
GRANT USAGE ON SCHEMA "public" TO "service_role";


--
-- Name: FUNCTION "cube_in"("cstring"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_out"("extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_out"("extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_recv"("internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_recv"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_send"("extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_send"("extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_in"("cstring", "oid", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_in"("cstring", "oid", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_out"("extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_out"("extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_recv"("internal", "oid", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_recv"("internal", "oid", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_send"("extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_send"("extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_typmod_in"("cstring"[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_typmod_in"("cstring"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box2d_in"("cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box2d_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box2d_out"("tiger"."box2d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box2d_out"("tiger"."box2d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box2df_in"("cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box2df_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box2df_out"("tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box2df_out"("tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box3d_in"("cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box3d_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box3d_out"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box3d_out"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_analyze"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_analyze"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_in"("cstring", "oid", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_in"("cstring", "oid", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_out"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_out"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_recv"("internal", "oid", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_recv"("internal", "oid", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_send"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_send"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_typmod_in"("cstring"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_typmod_in"("cstring"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_typmod_out"(integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_typmod_out"(integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_analyze"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_analyze"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_in"("cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_out"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_out"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_recv"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_recv"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_send"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_send"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_typmod_in"("cstring"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_typmod_in"("cstring"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_typmod_out"(integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_typmod_out"(integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gidx_in"("cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gidx_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gidx_out"("tiger"."gidx"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gidx_out"("tiger"."gidx") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "spheroid_in"("cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."spheroid_in"("cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "spheroid_out"("tiger"."spheroid"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."spheroid_out"("tiger"."spheroid") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "array_to_vector"(real[], integer, boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."array_to_vector"(real[], integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "array_to_vector"(double precision[], integer, boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."array_to_vector"(double precision[], integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "array_to_vector"(integer[], integer, boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."array_to_vector"(integer[], integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "array_to_vector"(numeric[], integer, boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."array_to_vector"(numeric[], integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box3d"("tiger"."box2d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box3d"("tiger"."box2d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("tiger"."box2d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("tiger"."box2d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box2d"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box2d"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "bytea"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."bytea"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography"("tiger"."geography", integer, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography"("tiger"."geography", integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box2d"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box2d"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box3d"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box3d"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "bytea"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."bytea"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("tiger"."geometry", integer, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("tiger"."geometry", integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "json"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."json"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "jsonb"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."jsonb"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "path"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."path"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "point"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."point"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "polygon"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."polygon"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "text"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."text"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("path"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("path") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("point"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("point") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("polygon"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("polygon") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_to_float4"("extensions"."vector", integer, boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_to_float4"("extensions"."vector", integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector"("extensions"."vector", integer, boolean); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector"("extensions"."vector", integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "algorithm_sign"("signables" "text", "secret" "text", "algorithm" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."algorithm_sign"("signables" "text", "secret" "text", "algorithm" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."algorithm_sign"("signables" "text", "secret" "text", "algorithm" "text") TO "dashboard_user";


--
-- Name: FUNCTION "armor"("bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."armor"("bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."armor"("bytea") TO "dashboard_user";


--
-- Name: FUNCTION "armor"("bytea", "text"[], "text"[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."armor"("bytea", "text"[], "text"[]) TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."armor"("bytea", "text"[], "text"[]) TO "dashboard_user";


--
-- Name: FUNCTION "cosine_distance"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cosine_distance"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "crypt"("text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."crypt"("text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."crypt"("text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "cube"(double precision[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube"(double precision[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube"(double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube"(double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube"("extensions"."cube", double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube"("extensions"."cube", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube"(double precision[], double precision[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube"(double precision[], double precision[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube"(double precision, double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube"(double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube"("extensions"."cube", double precision, double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube"("extensions"."cube", double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_cmp"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_cmp"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_contained"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_contained"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_contains"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_contains"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_coord"("extensions"."cube", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_coord"("extensions"."cube", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_coord_llur"("extensions"."cube", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_coord_llur"("extensions"."cube", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_dim"("extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_dim"("extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_distance"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_distance"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_enlarge"("extensions"."cube", double precision, integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_enlarge"("extensions"."cube", double precision, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_eq"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_eq"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_ge"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_ge"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_gt"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_gt"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_inter"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_inter"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_is_point"("extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_is_point"("extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_le"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_le"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_ll_coord"("extensions"."cube", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_ll_coord"("extensions"."cube", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_lt"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_lt"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_ne"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_ne"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_overlap"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_overlap"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_size"("extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_size"("extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_subset"("extensions"."cube", integer[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_subset"("extensions"."cube", integer[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_union"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_union"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cube_ur_coord"("extensions"."cube", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."cube_ur_coord"("extensions"."cube", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dearmor"("text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."dearmor"("text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."dearmor"("text") TO "dashboard_user";


--
-- Name: FUNCTION "decrypt"("bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."decrypt"("bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."decrypt"("bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "decrypt_iv"("bytea", "bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."decrypt_iv"("bytea", "bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."decrypt_iv"("bytea", "bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "digest"("bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."digest"("bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."digest"("bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "digest"("text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."digest"("text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."digest"("text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "distance_chebyshev"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."distance_chebyshev"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "distance_taxicab"("extensions"."cube", "extensions"."cube"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."distance_taxicab"("extensions"."cube", "extensions"."cube") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "earth"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."earth"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "earth_box"("extensions"."earth", double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."earth_box"("extensions"."earth", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "earth_distance"("extensions"."earth", "extensions"."earth"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."earth_distance"("extensions"."earth", "extensions"."earth") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "encrypt"("bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."encrypt"("bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."encrypt"("bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "encrypt_iv"("bytea", "bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."encrypt_iv"("bytea", "bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."encrypt_iv"("bytea", "bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "g_cube_consistent"("internal", "extensions"."cube", smallint, "oid", "internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."g_cube_consistent"("internal", "extensions"."cube", smallint, "oid", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "g_cube_distance"("internal", "extensions"."cube", smallint, "oid", "internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."g_cube_distance"("internal", "extensions"."cube", smallint, "oid", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "g_cube_penalty"("internal", "internal", "internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."g_cube_penalty"("internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "g_cube_picksplit"("internal", "internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."g_cube_picksplit"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "g_cube_same"("extensions"."cube", "extensions"."cube", "internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."g_cube_same"("extensions"."cube", "extensions"."cube", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "g_cube_union"("internal", "internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."g_cube_union"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gc_to_sec"(double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."gc_to_sec"(double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gen_random_bytes"(integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."gen_random_bytes"(integer) TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."gen_random_bytes"(integer) TO "dashboard_user";


--
-- Name: FUNCTION "gen_random_uuid"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."gen_random_uuid"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."gen_random_uuid"() TO "dashboard_user";


--
-- Name: FUNCTION "gen_salt"("text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."gen_salt"("text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."gen_salt"("text") TO "dashboard_user";


--
-- Name: FUNCTION "gen_salt"("text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."gen_salt"("text", integer) TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."gen_salt"("text", integer) TO "dashboard_user";


--
-- Name: FUNCTION "geo_distance"("point", "point"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."geo_distance"("point", "point") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "hash_decode"("text", "text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."hash_decode"("text", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "hash_encode"(bigint); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."hash_encode"(bigint) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "hash_encode"(bigint, "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."hash_encode"(bigint, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "hash_encode"(bigint, "text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."hash_encode"(bigint, "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "hmac"("bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."hmac"("bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."hmac"("bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "hmac"("text", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."hmac"("text", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."hmac"("text", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "id_decode"("text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode"("text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode"("text", "text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode"("text", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode"("text", "text", integer, "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode"("text", "text", integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode_once"("text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode_once"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode_once"("text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode_once"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode_once"("text", "text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode_once"("text", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_decode_once"("text", "text", integer, "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_decode_once"("text", "text", integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint[], "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint[], "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint, "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint[], "text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint[], "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint, "text", integer); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint, "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint[], "text", integer, "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint[], "text", integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "id_encode"(bigint, "text", integer, "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."id_encode"(bigint, "text", integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "inner_product"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."inner_product"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "ivfflathandler"("internal"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."ivfflathandler"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "l2_distance"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."l2_distance"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "latitude"("extensions"."earth"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."latitude"("extensions"."earth") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "ll_to_earth"(double precision, double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."ll_to_earth"(double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "longitude"("extensions"."earth"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."longitude"("extensions"."earth") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "parse_address"("text", OUT "num" "text", OUT "street" "text", OUT "street2" "text", OUT "address1" "text", OUT "city" "text", OUT "state" "text", OUT "zip" "text", OUT "zipplus" "text", OUT "country" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."parse_address"("text", OUT "num" "text", OUT "street" "text", OUT "street2" "text", OUT "address1" "text", OUT "city" "text", OUT "state" "text", OUT "zip" "text", OUT "zipplus" "text", OUT "country" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pg_stat_statements"("showtext" boolean, OUT "userid" "oid", OUT "dbid" "oid", OUT "toplevel" boolean, OUT "queryid" bigint, OUT "query" "text", OUT "plans" bigint, OUT "total_plan_time" double precision, OUT "min_plan_time" double precision, OUT "max_plan_time" double precision, OUT "mean_plan_time" double precision, OUT "stddev_plan_time" double precision, OUT "calls" bigint, OUT "total_exec_time" double precision, OUT "min_exec_time" double precision, OUT "max_exec_time" double precision, OUT "mean_exec_time" double precision, OUT "stddev_exec_time" double precision, OUT "rows" bigint, OUT "shared_blks_hit" bigint, OUT "shared_blks_read" bigint, OUT "shared_blks_dirtied" bigint, OUT "shared_blks_written" bigint, OUT "local_blks_hit" bigint, OUT "local_blks_read" bigint, OUT "local_blks_dirtied" bigint, OUT "local_blks_written" bigint, OUT "temp_blks_read" bigint, OUT "temp_blks_written" bigint, OUT "blk_read_time" double precision, OUT "blk_write_time" double precision, OUT "temp_blk_read_time" double precision, OUT "temp_blk_write_time" double precision, OUT "wal_records" bigint, OUT "wal_fpi" bigint, OUT "wal_bytes" numeric, OUT "jit_functions" bigint, OUT "jit_generation_time" double precision, OUT "jit_inlining_count" bigint, OUT "jit_inlining_time" double precision, OUT "jit_optimization_count" bigint, OUT "jit_optimization_time" double precision, OUT "jit_emission_count" bigint, OUT "jit_emission_time" double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pg_stat_statements"("showtext" boolean, OUT "userid" "oid", OUT "dbid" "oid", OUT "toplevel" boolean, OUT "queryid" bigint, OUT "query" "text", OUT "plans" bigint, OUT "total_plan_time" double precision, OUT "min_plan_time" double precision, OUT "max_plan_time" double precision, OUT "mean_plan_time" double precision, OUT "stddev_plan_time" double precision, OUT "calls" bigint, OUT "total_exec_time" double precision, OUT "min_exec_time" double precision, OUT "max_exec_time" double precision, OUT "mean_exec_time" double precision, OUT "stddev_exec_time" double precision, OUT "rows" bigint, OUT "shared_blks_hit" bigint, OUT "shared_blks_read" bigint, OUT "shared_blks_dirtied" bigint, OUT "shared_blks_written" bigint, OUT "local_blks_hit" bigint, OUT "local_blks_read" bigint, OUT "local_blks_dirtied" bigint, OUT "local_blks_written" bigint, OUT "temp_blks_read" bigint, OUT "temp_blks_written" bigint, OUT "blk_read_time" double precision, OUT "blk_write_time" double precision, OUT "temp_blk_read_time" double precision, OUT "temp_blk_write_time" double precision, OUT "wal_records" bigint, OUT "wal_fpi" bigint, OUT "wal_bytes" numeric, OUT "jit_functions" bigint, OUT "jit_generation_time" double precision, OUT "jit_inlining_count" bigint, OUT "jit_inlining_time" double precision, OUT "jit_optimization_count" bigint, OUT "jit_optimization_time" double precision, OUT "jit_emission_count" bigint, OUT "jit_emission_time" double precision) TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pg_stat_statements"("showtext" boolean, OUT "userid" "oid", OUT "dbid" "oid", OUT "toplevel" boolean, OUT "queryid" bigint, OUT "query" "text", OUT "plans" bigint, OUT "total_plan_time" double precision, OUT "min_plan_time" double precision, OUT "max_plan_time" double precision, OUT "mean_plan_time" double precision, OUT "stddev_plan_time" double precision, OUT "calls" bigint, OUT "total_exec_time" double precision, OUT "min_exec_time" double precision, OUT "max_exec_time" double precision, OUT "mean_exec_time" double precision, OUT "stddev_exec_time" double precision, OUT "rows" bigint, OUT "shared_blks_hit" bigint, OUT "shared_blks_read" bigint, OUT "shared_blks_dirtied" bigint, OUT "shared_blks_written" bigint, OUT "local_blks_hit" bigint, OUT "local_blks_read" bigint, OUT "local_blks_dirtied" bigint, OUT "local_blks_written" bigint, OUT "temp_blks_read" bigint, OUT "temp_blks_written" bigint, OUT "blk_read_time" double precision, OUT "blk_write_time" double precision, OUT "temp_blk_read_time" double precision, OUT "temp_blk_write_time" double precision, OUT "wal_records" bigint, OUT "wal_fpi" bigint, OUT "wal_bytes" numeric, OUT "jit_functions" bigint, OUT "jit_generation_time" double precision, OUT "jit_inlining_count" bigint, OUT "jit_inlining_time" double precision, OUT "jit_optimization_count" bigint, OUT "jit_optimization_time" double precision, OUT "jit_emission_count" bigint, OUT "jit_emission_time" double precision) TO "dashboard_user";


--
-- Name: FUNCTION "pg_stat_statements_info"(OUT "dealloc" bigint, OUT "stats_reset" timestamp with time zone); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pg_stat_statements_info"(OUT "dealloc" bigint, OUT "stats_reset" timestamp with time zone) TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pg_stat_statements_info"(OUT "dealloc" bigint, OUT "stats_reset" timestamp with time zone) TO "dashboard_user";


--
-- Name: FUNCTION "pg_stat_statements_reset"("userid" "oid", "dbid" "oid", "queryid" bigint); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pg_stat_statements_reset"("userid" "oid", "dbid" "oid", "queryid" bigint) TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pg_stat_statements_reset"("userid" "oid", "dbid" "oid", "queryid" bigint) TO "dashboard_user";


--
-- Name: FUNCTION "pgp_armor_headers"("text", OUT "key" "text", OUT "value" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_armor_headers"("text", OUT "key" "text", OUT "value" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_armor_headers"("text", OUT "key" "text", OUT "value" "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_key_id"("bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_key_id"("bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_key_id"("bytea") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_decrypt"("bytea", "bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt"("bytea", "bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt"("bytea", "bytea") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_decrypt"("bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt"("bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt"("bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_decrypt"("bytea", "bytea", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt"("bytea", "bytea", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt"("bytea", "bytea", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_decrypt_bytea"("bytea", "bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt_bytea"("bytea", "bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt_bytea"("bytea", "bytea") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_decrypt_bytea"("bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt_bytea"("bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt_bytea"("bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_decrypt_bytea"("bytea", "bytea", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt_bytea"("bytea", "bytea", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_decrypt_bytea"("bytea", "bytea", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_encrypt"("text", "bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt"("text", "bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt"("text", "bytea") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_encrypt"("text", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt"("text", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt"("text", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_encrypt_bytea"("bytea", "bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt_bytea"("bytea", "bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt_bytea"("bytea", "bytea") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_pub_encrypt_bytea"("bytea", "bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt_bytea"("bytea", "bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_pub_encrypt_bytea"("bytea", "bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_decrypt"("bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt"("bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt"("bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_decrypt"("bytea", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt"("bytea", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt"("bytea", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_decrypt_bytea"("bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt_bytea"("bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt_bytea"("bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_decrypt_bytea"("bytea", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt_bytea"("bytea", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_decrypt_bytea"("bytea", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_encrypt"("text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt"("text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt"("text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_encrypt"("text", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt"("text", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt"("text", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_encrypt_bytea"("bytea", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt_bytea"("bytea", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt_bytea"("bytea", "text") TO "dashboard_user";


--
-- Name: FUNCTION "pgp_sym_encrypt_bytea"("bytea", "text", "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt_bytea"("bytea", "text", "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."pgp_sym_encrypt_bytea"("bytea", "text", "text") TO "dashboard_user";


--
-- Name: FUNCTION "sec_to_gc"(double precision); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."sec_to_gc"(double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "sign"("payload" "json", "secret" "text", "algorithm" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."sign"("payload" "json", "secret" "text", "algorithm" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."sign"("payload" "json", "secret" "text", "algorithm" "text") TO "dashboard_user";


--
-- Name: FUNCTION "standardize_address"("lextab" "text", "gaztab" "text", "rultab" "text", "address" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."standardize_address"("lextab" "text", "gaztab" "text", "rultab" "text", "address" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "standardize_address"("lextab" "text", "gaztab" "text", "rultab" "text", "micro" "text", "macro" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."standardize_address"("lextab" "text", "gaztab" "text", "rultab" "text", "micro" "text", "macro" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "try_cast_double"("inp" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."try_cast_double"("inp" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."try_cast_double"("inp" "text") TO "dashboard_user";


--
-- Name: FUNCTION "url_decode"("data" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."url_decode"("data" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."url_decode"("data" "text") TO "dashboard_user";


--
-- Name: FUNCTION "url_encode"("data" "bytea"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."url_encode"("data" "bytea") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."url_encode"("data" "bytea") TO "dashboard_user";


--
-- Name: FUNCTION "uuid_generate_v1"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v1"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v1"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_generate_v1mc"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v1mc"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v1mc"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_generate_v3"("namespace" "uuid", "name" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v3"("namespace" "uuid", "name" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v3"("namespace" "uuid", "name" "text") TO "dashboard_user";


--
-- Name: FUNCTION "uuid_generate_v4"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v4"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v4"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_generate_v5"("namespace" "uuid", "name" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v5"("namespace" "uuid", "name" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_generate_v5"("namespace" "uuid", "name" "text") TO "dashboard_user";


--
-- Name: FUNCTION "uuid_nil"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_nil"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_nil"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_ns_dns"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_dns"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_dns"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_ns_oid"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_oid"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_oid"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_ns_url"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_url"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_url"() TO "dashboard_user";


--
-- Name: FUNCTION "uuid_ns_x500"(); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_x500"() TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."uuid_ns_x500"() TO "dashboard_user";


--
-- Name: FUNCTION "vector_accum"(double precision[], "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_accum"(double precision[], "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_add"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_add"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_avg"(double precision[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_avg"(double precision[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_cmp"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_cmp"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_combine"(double precision[], double precision[]); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_combine"(double precision[], double precision[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_dims"("extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_dims"("extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_eq"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_eq"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_ge"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_ge"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_gt"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_gt"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_l2_squared_distance"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_l2_squared_distance"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_le"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_le"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_lt"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_lt"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_ne"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_ne"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_negative_inner_product"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_negative_inner_product"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_norm"("extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_norm"("extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_spherical_distance"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_spherical_distance"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "vector_sub"("extensions"."vector", "extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."vector_sub"("extensions"."vector", "extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "verify"("token" "text", "secret" "text", "algorithm" "text"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."verify"("token" "text", "secret" "text", "algorithm" "text") TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON FUNCTION "extensions"."verify"("token" "text", "secret" "text", "algorithm" "text") TO "dashboard_user";


--
-- Name: FUNCTION "comment_directive"("comment_" "text"); Type: ACL; Schema: graphql; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "graphql"."comment_directive"("comment_" "text") TO "postgres";
-- GRANT ALL ON FUNCTION "graphql"."comment_directive"("comment_" "text") TO "anon";
-- GRANT ALL ON FUNCTION "graphql"."comment_directive"("comment_" "text") TO "authenticated";
-- GRANT ALL ON FUNCTION "graphql"."comment_directive"("comment_" "text") TO "service_role";


--
-- Name: FUNCTION "exception"("message" "text"); Type: ACL; Schema: graphql; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "graphql"."exception"("message" "text") TO "postgres";
-- GRANT ALL ON FUNCTION "graphql"."exception"("message" "text") TO "anon";
-- GRANT ALL ON FUNCTION "graphql"."exception"("message" "text") TO "authenticated";
-- GRANT ALL ON FUNCTION "graphql"."exception"("message" "text") TO "service_role";


--
-- Name: FUNCTION "get_schema_version"(); Type: ACL; Schema: graphql; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "graphql"."get_schema_version"() TO "postgres";
-- GRANT ALL ON FUNCTION "graphql"."get_schema_version"() TO "anon";
-- GRANT ALL ON FUNCTION "graphql"."get_schema_version"() TO "authenticated";
-- GRANT ALL ON FUNCTION "graphql"."get_schema_version"() TO "service_role";


--
-- Name: FUNCTION "increment_schema_version"(); Type: ACL; Schema: graphql; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "graphql"."increment_schema_version"() TO "postgres";
-- GRANT ALL ON FUNCTION "graphql"."increment_schema_version"() TO "anon";
-- GRANT ALL ON FUNCTION "graphql"."increment_schema_version"() TO "authenticated";
-- GRANT ALL ON FUNCTION "graphql"."increment_schema_version"() TO "service_role";


--
-- Name: FUNCTION "graphql"("operationName" "text", "query" "text", "variables" "jsonb", "extensions" "jsonb"); Type: ACL; Schema: graphql_public; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "graphql_public"."graphql"("operationName" "text", "query" "text", "variables" "jsonb", "extensions" "jsonb") TO "postgres";
-- GRANT ALL ON FUNCTION "graphql_public"."graphql"("operationName" "text", "query" "text", "variables" "jsonb", "extensions" "jsonb") TO "anon";
-- GRANT ALL ON FUNCTION "graphql_public"."graphql"("operationName" "text", "query" "text", "variables" "jsonb", "extensions" "jsonb") TO "authenticated";
-- GRANT ALL ON FUNCTION "graphql_public"."graphql"("operationName" "text", "query" "text", "variables" "jsonb", "extensions" "jsonb") TO "service_role";


--
-- Name: FUNCTION "http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer); Type: ACL; Schema: net; Owner: supabase_admin
--

-- REVOKE ALL ON FUNCTION "net"."http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) FROM PUBLIC;
-- GRANT ALL ON FUNCTION "net"."http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "supabase_functions_admin";
-- GRANT ALL ON FUNCTION "net"."http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "postgres";
-- GRANT ALL ON FUNCTION "net"."http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "anon";
-- GRANT ALL ON FUNCTION "net"."http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "authenticated";
-- GRANT ALL ON FUNCTION "net"."http_get"("url" "text", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "service_role";


--
-- Name: FUNCTION "http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer); Type: ACL; Schema: net; Owner: supabase_admin
--

-- REVOKE ALL ON FUNCTION "net"."http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) FROM PUBLIC;
-- GRANT ALL ON FUNCTION "net"."http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "supabase_functions_admin";
-- GRANT ALL ON FUNCTION "net"."http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "postgres";
-- GRANT ALL ON FUNCTION "net"."http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "anon";
-- GRANT ALL ON FUNCTION "net"."http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "authenticated";
-- GRANT ALL ON FUNCTION "net"."http_post"("url" "text", "body" "jsonb", "params" "jsonb", "headers" "jsonb", "timeout_milliseconds" integer) TO "service_role";


--
-- Name: FUNCTION "crypto_aead_det_decrypt"("message" "bytea", "additional" "bytea", "key_uuid" "uuid", "nonce" "bytea"); Type: ACL; Schema: pgsodium; Owner: pgsodium_keymaker
--

-- GRANT ALL ON FUNCTION "pgsodium"."crypto_aead_det_decrypt"("message" "bytea", "additional" "bytea", "key_uuid" "uuid", "nonce" "bytea") TO "service_role";


--
-- Name: FUNCTION "crypto_aead_det_encrypt"("message" "bytea", "additional" "bytea", "key_uuid" "uuid", "nonce" "bytea"); Type: ACL; Schema: pgsodium; Owner: pgsodium_keymaker
--

-- GRANT ALL ON FUNCTION "pgsodium"."crypto_aead_det_encrypt"("message" "bytea", "additional" "bytea", "key_uuid" "uuid", "nonce" "bytea") TO "service_role";


--
-- Name: FUNCTION "crypto_aead_det_keygen"(); Type: ACL; Schema: pgsodium; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "pgsodium"."crypto_aead_det_keygen"() TO "service_role";


--
-- Name: TABLE "Thing"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."Thing" TO "anon";
GRANT ALL ON TABLE "public"."Thing" TO "authenticated";
GRANT ALL ON TABLE "public"."Thing" TO "service_role";


--
-- Name: FUNCTION "thingsnearby"("distance" double precision, "latitude" double precision, "longitude" double precision); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION "public"."thingsnearby"("distance" double precision, "latitude" double precision, "longitude" double precision) TO "anon";
GRANT ALL ON FUNCTION "public"."thingsnearby"("distance" double precision, "latitude" double precision, "longitude" double precision) TO "authenticated";
GRANT ALL ON FUNCTION "public"."thingsnearby"("distance" double precision, "latitude" double precision, "longitude" double precision) TO "service_role";


--
-- Name: FUNCTION "_postgis_deprecate"("oldname" "text", "newname" "text", "version" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_deprecate"("oldname" "text", "newname" "text", "version" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_postgis_index_extent"("tbl" "regclass", "col" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_index_extent"("tbl" "regclass", "col" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_postgis_join_selectivity"("regclass", "text", "regclass", "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_join_selectivity"("regclass", "text", "regclass", "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_postgis_pgsql_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_pgsql_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_postgis_scripts_pgsql_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_scripts_pgsql_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_postgis_selectivity"("tbl" "regclass", "att_name" "text", "geom" "tiger"."geometry", "mode" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_selectivity"("tbl" "regclass", "att_name" "text", "geom" "tiger"."geometry", "mode" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_postgis_stats"("tbl" "regclass", "att_name" "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_postgis_stats"("tbl" "regclass", "att_name" "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_3ddfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_3ddfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_3ddwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_3ddwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_3dintersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_3dintersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_asgml"(integer, "tiger"."geometry", integer, integer, "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_asgml"(integer, "tiger"."geometry", integer, integer, "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_asx3d"(integer, "tiger"."geometry", integer, integer, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_asx3d"(integer, "tiger"."geometry", integer, integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_bestsrid"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_bestsrid"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_bestsrid"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_bestsrid"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_concavehull"("param_inputgeom" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_concavehull"("param_inputgeom" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_contains"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_contains"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_containsproperly"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_containsproperly"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_coveredby"("geog1" "tiger"."geography", "geog2" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_coveredby"("geog1" "tiger"."geography", "geog2" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_coveredby"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_coveredby"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_covers"("geog1" "tiger"."geography", "geog2" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_covers"("geog1" "tiger"."geography", "geog2" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_covers"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_covers"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_crosses"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_crosses"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_dfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_dfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_distancetree"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_distancetree"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_distancetree"("tiger"."geography", "tiger"."geography", double precision, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_distancetree"("tiger"."geography", "tiger"."geography", double precision, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_distanceuncached"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_distanceuncached"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_distanceuncached"("tiger"."geography", "tiger"."geography", boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_distanceuncached"("tiger"."geography", "tiger"."geography", boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_distanceuncached"("tiger"."geography", "tiger"."geography", double precision, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_distanceuncached"("tiger"."geography", "tiger"."geography", double precision, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_dwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_dwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_dwithin"("geog1" "tiger"."geography", "geog2" "tiger"."geography", "tolerance" double precision, "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_dwithin"("geog1" "tiger"."geography", "geog2" "tiger"."geography", "tolerance" double precision, "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_dwithinuncached"("tiger"."geography", "tiger"."geography", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_dwithinuncached"("tiger"."geography", "tiger"."geography", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_dwithinuncached"("tiger"."geography", "tiger"."geography", double precision, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_dwithinuncached"("tiger"."geography", "tiger"."geography", double precision, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_equals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_equals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_expand"("tiger"."geography", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_expand"("tiger"."geography", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_geomfromgml"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_geomfromgml"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_intersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_intersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_linecrossingdirection"("line1" "tiger"."geometry", "line2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_linecrossingdirection"("line1" "tiger"."geometry", "line2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_longestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_longestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_maxdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_maxdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_orderingequals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_orderingequals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_overlaps"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_overlaps"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_pointoutside"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_pointoutside"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_sortablehash"("geom" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_sortablehash"("geom" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_touches"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_touches"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_voronoi"("g1" "tiger"."geometry", "clip" "tiger"."geometry", "tolerance" double precision, "return_polygons" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_voronoi"("g1" "tiger"."geometry", "clip" "tiger"."geometry", "tolerance" double precision, "return_polygons" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "_st_within"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."_st_within"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "addauth"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."addauth"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "addgeometrycolumn"("table_name" character varying, "column_name" character varying, "new_srid" integer, "new_type" character varying, "new_dim" integer, "use_typmod" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."addgeometrycolumn"("table_name" character varying, "column_name" character varying, "new_srid" integer, "new_type" character varying, "new_dim" integer, "use_typmod" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "addgeometrycolumn"("schema_name" character varying, "table_name" character varying, "column_name" character varying, "new_srid" integer, "new_type" character varying, "new_dim" integer, "use_typmod" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."addgeometrycolumn"("schema_name" character varying, "table_name" character varying, "column_name" character varying, "new_srid" integer, "new_type" character varying, "new_dim" integer, "use_typmod" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "addgeometrycolumn"("catalog_name" character varying, "schema_name" character varying, "table_name" character varying, "column_name" character varying, "new_srid_in" integer, "new_type" character varying, "new_dim" integer, "use_typmod" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."addgeometrycolumn"("catalog_name" character varying, "schema_name" character varying, "table_name" character varying, "column_name" character varying, "new_srid_in" integer, "new_type" character varying, "new_dim" integer, "use_typmod" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "box3dtobox"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."box3dtobox"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "checkauth"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."checkauth"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "checkauth"("text", "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."checkauth"("text", "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "checkauthtrigger"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."checkauthtrigger"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "contains_2d"("tiger"."box2df", "tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."contains_2d"("tiger"."box2df", "tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "contains_2d"("tiger"."box2df", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."contains_2d"("tiger"."box2df", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "contains_2d"("tiger"."geometry", "tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."contains_2d"("tiger"."geometry", "tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "count_words"(character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."count_words"(character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "create_census_base_tables"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."create_census_base_tables"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "cull_null"(character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."cull_null"(character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "diff_zip"("zip1" character varying, "zip2" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."diff_zip"("zip1" character varying, "zip2" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "difference"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."difference"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "disablelongtransactions"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."disablelongtransactions"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dmetaphone"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dmetaphone"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dmetaphone_alt"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dmetaphone_alt"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "drop_dupe_featnames_generate_script"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."drop_dupe_featnames_generate_script"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "drop_indexes_generate_script"("tiger_data_schema" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."drop_indexes_generate_script"("tiger_data_schema" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "drop_nation_tables_generate_script"("param_schema" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."drop_nation_tables_generate_script"("param_schema" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "drop_state_tables_generate_script"("param_state" "text", "param_schema" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."drop_state_tables_generate_script"("param_state" "text", "param_schema" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dropgeometrycolumn"("table_name" character varying, "column_name" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dropgeometrycolumn"("table_name" character varying, "column_name" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dropgeometrycolumn"("schema_name" character varying, "table_name" character varying, "column_name" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dropgeometrycolumn"("schema_name" character varying, "table_name" character varying, "column_name" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dropgeometrycolumn"("catalog_name" character varying, "schema_name" character varying, "table_name" character varying, "column_name" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dropgeometrycolumn"("catalog_name" character varying, "schema_name" character varying, "table_name" character varying, "column_name" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dropgeometrytable"("table_name" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dropgeometrytable"("table_name" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dropgeometrytable"("schema_name" character varying, "table_name" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dropgeometrytable"("schema_name" character varying, "table_name" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "dropgeometrytable"("catalog_name" character varying, "schema_name" character varying, "table_name" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."dropgeometrytable"("catalog_name" character varying, "schema_name" character varying, "table_name" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "enablelongtransactions"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."enablelongtransactions"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "end_soundex"(character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."end_soundex"(character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "equals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."equals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "find_srid"(character varying, character varying, character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."find_srid"(character varying, character varying, character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geocode"("input" character varying, "max_results" integer, "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geocode"("input" character varying, "max_results" integer, "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geocode"("in_addy" "tiger"."norm_addy", "max_results" integer, "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geocode"("in_addy" "tiger"."norm_addy", "max_results" integer, "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geocode_address"("parsed" "tiger"."norm_addy", "max_results" integer, "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geocode_address"("parsed" "tiger"."norm_addy", "max_results" integer, "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geocode_intersection"("roadway1" "text", "roadway2" "text", "in_state" "text", "in_city" "text", "in_zip" "text", "num_results" integer, OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geocode_intersection"("roadway1" "text", "roadway2" "text", "in_state" "text", "in_city" "text", "in_zip" "text", "num_results" integer, OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geocode_location"("parsed" "tiger"."norm_addy", "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geocode_location"("parsed" "tiger"."norm_addy", "restrict_geom" "tiger"."geometry", OUT "addy" "tiger"."norm_addy", OUT "geomout" "tiger"."geometry", OUT "rating" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geog_brin_inclusion_add_value"("internal", "internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geog_brin_inclusion_add_value"("internal", "internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_cmp"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_cmp"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_distance_knn"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_distance_knn"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_eq"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_eq"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_ge"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_ge"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_compress"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_compress"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_consistent"("internal", "tiger"."geography", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_consistent"("internal", "tiger"."geography", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_decompress"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_decompress"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_distance"("internal", "tiger"."geography", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_distance"("internal", "tiger"."geography", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_penalty"("internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_penalty"("internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_picksplit"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_picksplit"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_same"("tiger"."box2d", "tiger"."box2d", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_same"("tiger"."box2d", "tiger"."box2d", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gist_union"("bytea", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gist_union"("bytea", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_gt"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_gt"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_le"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_le"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_lt"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_lt"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_overlaps"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_overlaps"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_spgist_choose_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_spgist_choose_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_spgist_compress_nd"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_spgist_compress_nd"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_spgist_config_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_spgist_config_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_spgist_inner_consistent_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_spgist_inner_consistent_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_spgist_leaf_consistent_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_spgist_leaf_consistent_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geography_spgist_picksplit_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geography_spgist_picksplit_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geom2d_brin_inclusion_add_value"("internal", "internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geom2d_brin_inclusion_add_value"("internal", "internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geom3d_brin_inclusion_add_value"("internal", "internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geom3d_brin_inclusion_add_value"("internal", "internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geom4d_brin_inclusion_add_value"("internal", "internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geom4d_brin_inclusion_add_value"("internal", "internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_above"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_above"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_below"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_below"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_cmp"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_cmp"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_contained_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_contained_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_contains"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_contains"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_contains_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_contains_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_contains_nd"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_contains_nd"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_distance_box"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_distance_box"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_distance_centroid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_distance_centroid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_distance_centroid_nd"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_distance_centroid_nd"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_distance_cpa"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_distance_cpa"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_eq"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_eq"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_ge"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_ge"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_compress_2d"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_compress_2d"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_compress_nd"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_compress_nd"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_consistent_2d"("internal", "tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_consistent_2d"("internal", "tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_consistent_nd"("internal", "tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_consistent_nd"("internal", "tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_decompress_2d"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_decompress_2d"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_decompress_nd"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_decompress_nd"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_distance_2d"("internal", "tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_distance_2d"("internal", "tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_distance_nd"("internal", "tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_distance_nd"("internal", "tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_penalty_2d"("internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_penalty_2d"("internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_penalty_nd"("internal", "internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_penalty_nd"("internal", "internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_picksplit_2d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_picksplit_2d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_picksplit_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_picksplit_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_same_2d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_same_2d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_same_nd"("tiger"."geometry", "tiger"."geometry", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_same_nd"("tiger"."geometry", "tiger"."geometry", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_sortsupport_2d"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_sortsupport_2d"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_union_2d"("bytea", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_union_2d"("bytea", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gist_union_nd"("bytea", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gist_union_nd"("bytea", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_gt"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_gt"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_hash"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_hash"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_le"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_le"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_left"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_left"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_lt"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_lt"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overabove"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overabove"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overbelow"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overbelow"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overlaps"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overlaps"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overlaps_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overlaps_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overlaps_nd"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overlaps_nd"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overleft"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overleft"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_overright"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_overright"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_right"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_right"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_same"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_same"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_same_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_same_3d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_same_nd"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_same_nd"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_sortsupport"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_sortsupport"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_choose_2d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_choose_2d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_choose_3d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_choose_3d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_choose_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_choose_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_compress_2d"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_compress_2d"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_compress_3d"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_compress_3d"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_compress_nd"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_compress_nd"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_config_2d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_config_2d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_config_3d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_config_3d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_config_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_config_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_inner_consistent_2d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_inner_consistent_2d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_inner_consistent_3d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_inner_consistent_3d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_inner_consistent_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_inner_consistent_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_leaf_consistent_2d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_leaf_consistent_2d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_leaf_consistent_3d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_leaf_consistent_3d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_leaf_consistent_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_leaf_consistent_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_picksplit_2d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_picksplit_2d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_picksplit_3d"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_picksplit_3d"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_spgist_picksplit_nd"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_spgist_picksplit_nd"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_within"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_within"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometry_within_nd"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometry_within_nd"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometrytype"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometrytype"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geometrytype"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geometrytype"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geomfromewkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geomfromewkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "geomfromewkt"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."geomfromewkt"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "get_geocode_setting"("setting_name" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."get_geocode_setting"("setting_name" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "get_last_words"("inputstring" character varying, "count" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."get_last_words"("inputstring" character varying, "count" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "get_proj4_from_srid"(integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."get_proj4_from_srid"(integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "get_tract"("loc_geom" "tiger"."geometry", "output_field" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."get_tract"("loc_geom" "tiger"."geometry", "output_field" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gettransactionid"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gettransactionid"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "greatest_hn"("fromhn" character varying, "tohn" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."greatest_hn"("fromhn" character varying, "tohn" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gserialized_gist_joinsel_2d"("internal", "oid", "internal", smallint); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gserialized_gist_joinsel_2d"("internal", "oid", "internal", smallint) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gserialized_gist_joinsel_nd"("internal", "oid", "internal", smallint); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gserialized_gist_joinsel_nd"("internal", "oid", "internal", smallint) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gserialized_gist_sel_2d"("internal", "oid", "internal", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gserialized_gist_sel_2d"("internal", "oid", "internal", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "gserialized_gist_sel_nd"("internal", "oid", "internal", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."gserialized_gist_sel_nd"("internal", "oid", "internal", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "includes_address"("given_address" integer, "addr1" integer, "addr2" integer, "addr3" integer, "addr4" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."includes_address"("given_address" integer, "addr1" integer, "addr2" integer, "addr3" integer, "addr4" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "install_geocode_settings"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."install_geocode_settings"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "install_missing_indexes"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."install_missing_indexes"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "install_pagc_tables"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."install_pagc_tables"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "interpolate_from_address"("given_address" integer, "in_addr1" character varying, "in_addr2" character varying, "in_road" "tiger"."geometry", "in_side" character varying, "in_offset_m" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."interpolate_from_address"("given_address" integer, "in_addr1" character varying, "in_addr2" character varying, "in_road" "tiger"."geometry", "in_side" character varying, "in_offset_m" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "is_contained_2d"("tiger"."box2df", "tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."is_contained_2d"("tiger"."box2df", "tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "is_contained_2d"("tiger"."box2df", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."is_contained_2d"("tiger"."box2df", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "is_contained_2d"("tiger"."geometry", "tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."is_contained_2d"("tiger"."geometry", "tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "is_pretype"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."is_pretype"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "least_hn"("fromhn" character varying, "tohn" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."least_hn"("fromhn" character varying, "tohn" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "levenshtein"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."levenshtein"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "levenshtein"("text", "text", integer, integer, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."levenshtein"("text", "text", integer, integer, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "levenshtein_ignore_case"(character varying, character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."levenshtein_ignore_case"(character varying, character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "levenshtein_less_equal"("text", "text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."levenshtein_less_equal"("text", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "levenshtein_less_equal"("text", "text", integer, integer, integer, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."levenshtein_less_equal"("text", "text", integer, integer, integer, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "loader_generate_census_script"("param_states" "text"[], "os" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."loader_generate_census_script"("param_states" "text"[], "os" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "loader_generate_nation_script"("os" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."loader_generate_nation_script"("os" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "loader_generate_script"("param_states" "text"[], "os" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."loader_generate_script"("param_states" "text"[], "os" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "loader_load_staged_data"("param_staging_table" "text", "param_target_table" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."loader_load_staged_data"("param_staging_table" "text", "param_target_table" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "loader_load_staged_data"("param_staging_table" "text", "param_target_table" "text", "param_columns_exclude" "text"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."loader_load_staged_data"("param_staging_table" "text", "param_target_table" "text", "param_columns_exclude" "text"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "loader_macro_replace"("param_input" "text", "param_keys" "text"[], "param_values" "text"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."loader_macro_replace"("param_input" "text", "param_keys" "text"[], "param_values" "text"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "location_extract"("fullstreet" character varying, "stateabbrev" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."location_extract"("fullstreet" character varying, "stateabbrev" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "location_extract_countysub_exact"("fullstreet" character varying, "stateabbrev" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."location_extract_countysub_exact"("fullstreet" character varying, "stateabbrev" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "location_extract_countysub_fuzzy"("fullstreet" character varying, "stateabbrev" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."location_extract_countysub_fuzzy"("fullstreet" character varying, "stateabbrev" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "location_extract_place_exact"("fullstreet" character varying, "stateabbrev" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."location_extract_place_exact"("fullstreet" character varying, "stateabbrev" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "location_extract_place_fuzzy"("fullstreet" character varying, "stateabbrev" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."location_extract_place_fuzzy"("fullstreet" character varying, "stateabbrev" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "lockrow"("text", "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."lockrow"("text", "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "lockrow"("text", "text", "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."lockrow"("text", "text", "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "lockrow"("text", "text", "text", timestamp without time zone); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."lockrow"("text", "text", "text", timestamp without time zone) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "lockrow"("text", "text", "text", "text", timestamp without time zone); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."lockrow"("text", "text", "text", "text", timestamp without time zone) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "longtransactionsenabled"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."longtransactionsenabled"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "metaphone"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."metaphone"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "missing_indexes_generate_script"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."missing_indexes_generate_script"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "normalize_address"("in_rawinput" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."normalize_address"("in_rawinput" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "nullable_levenshtein"(character varying, character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."nullable_levenshtein"(character varying, character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "numeric_streets_equal"("input_street" character varying, "output_street" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."numeric_streets_equal"("input_street" character varying, "output_street" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_2d"("tiger"."box2df", "tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_2d"("tiger"."box2df", "tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_2d"("tiger"."box2df", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_2d"("tiger"."box2df", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_2d"("tiger"."geometry", "tiger"."box2df"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_2d"("tiger"."geometry", "tiger"."box2df") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_geog"("tiger"."geography", "tiger"."gidx"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_geog"("tiger"."geography", "tiger"."gidx") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_geog"("tiger"."gidx", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_geog"("tiger"."gidx", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_geog"("tiger"."gidx", "tiger"."gidx"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_geog"("tiger"."gidx", "tiger"."gidx") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_nd"("tiger"."geometry", "tiger"."gidx"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_nd"("tiger"."geometry", "tiger"."gidx") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_nd"("tiger"."gidx", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_nd"("tiger"."gidx", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "overlaps_nd"("tiger"."gidx", "tiger"."gidx"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."overlaps_nd"("tiger"."gidx", "tiger"."gidx") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pagc_normalize_address"("in_rawinput" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pagc_normalize_address"("in_rawinput" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asflatgeobuf_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asflatgeobuf_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asflatgeobuf_transfn"("internal", "anyelement"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asflatgeobuf_transfn"("internal", "anyelement") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asflatgeobuf_transfn"("internal", "anyelement", boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asflatgeobuf_transfn"("internal", "anyelement", boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asflatgeobuf_transfn"("internal", "anyelement", boolean, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asflatgeobuf_transfn"("internal", "anyelement", boolean, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asgeobuf_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asgeobuf_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asgeobuf_transfn"("internal", "anyelement"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asgeobuf_transfn"("internal", "anyelement") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asgeobuf_transfn"("internal", "anyelement", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asgeobuf_transfn"("internal", "anyelement", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_combinefn"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_combinefn"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_deserialfn"("bytea", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_deserialfn"("bytea", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_serialfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_serialfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_transfn"("internal", "anyelement"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_transfn"("internal", "anyelement") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_transfn"("internal", "anyelement", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_transfn"("internal", "anyelement", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_transfn"("internal", "anyelement", "text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_transfn"("internal", "anyelement", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_transfn"("internal", "anyelement", "text", integer, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_transfn"("internal", "anyelement", "text", integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_asmvt_transfn"("internal", "anyelement", "text", integer, "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_asmvt_transfn"("internal", "anyelement", "text", integer, "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_accum_transfn"("internal", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_accum_transfn"("internal", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_accum_transfn"("internal", "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_accum_transfn"("internal", "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_accum_transfn"("internal", "tiger"."geometry", double precision, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_accum_transfn"("internal", "tiger"."geometry", double precision, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_clusterintersecting_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_clusterintersecting_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_clusterwithin_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_clusterwithin_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_collect_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_collect_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_makeline_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_makeline_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_polygonize_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_polygonize_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_union_parallel_combinefn"("internal", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_union_parallel_combinefn"("internal", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_union_parallel_deserialfn"("bytea", "internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_union_parallel_deserialfn"("bytea", "internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_union_parallel_finalfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_union_parallel_finalfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_union_parallel_serialfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_union_parallel_serialfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_union_parallel_transfn"("internal", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_union_parallel_transfn"("internal", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pgis_geometry_union_parallel_transfn"("internal", "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pgis_geometry_union_parallel_transfn"("internal", "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "populate_geometry_columns"("use_typmod" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."populate_geometry_columns"("use_typmod" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "populate_geometry_columns"("tbl_oid" "oid", "use_typmod" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."populate_geometry_columns"("tbl_oid" "oid", "use_typmod" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_addbbox"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_addbbox"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_cache_bbox"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_cache_bbox"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_constraint_dims"("geomschema" "text", "geomtable" "text", "geomcolumn" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_constraint_dims"("geomschema" "text", "geomtable" "text", "geomcolumn" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_constraint_srid"("geomschema" "text", "geomtable" "text", "geomcolumn" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_constraint_srid"("geomschema" "text", "geomtable" "text", "geomcolumn" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_constraint_type"("geomschema" "text", "geomtable" "text", "geomcolumn" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_constraint_type"("geomschema" "text", "geomtable" "text", "geomcolumn" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_dropbbox"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_dropbbox"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_extensions_upgrade"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_extensions_upgrade"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_full_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_full_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_geos_noop"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_geos_noop"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_geos_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_geos_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_getbbox"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_getbbox"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_hasbbox"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_hasbbox"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_index_supportfn"("internal"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_index_supportfn"("internal") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_lib_build_date"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_lib_build_date"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_lib_revision"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_lib_revision"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_lib_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_lib_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_libjson_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_libjson_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_liblwgeom_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_liblwgeom_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_libprotobuf_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_libprotobuf_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_libxml_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_libxml_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_noop"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_noop"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_proj_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_proj_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_scripts_build_date"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_scripts_build_date"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_scripts_installed"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_scripts_installed"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_scripts_released"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_scripts_released"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_svn_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_svn_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_transform_geometry"("geom" "tiger"."geometry", "text", "text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_transform_geometry"("geom" "tiger"."geometry", "text", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_type_name"("geomname" character varying, "coord_dimension" integer, "use_new_name" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_type_name"("geomname" character varying, "coord_dimension" integer, "use_new_name" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_typmod_dims"(integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_typmod_dims"(integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_typmod_srid"(integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_typmod_srid"(integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_typmod_type"(integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_typmod_type"(integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "postgis_wagyu_version"(); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."postgis_wagyu_version"() TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "pprint_addy"("input" "tiger"."norm_addy"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."pprint_addy"("input" "tiger"."norm_addy") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "rate_attributes"("dirpa" character varying, "dirpb" character varying, "streetnamea" character varying, "streetnameb" character varying, "streettypea" character varying, "streettypeb" character varying, "dirsa" character varying, "dirsb" character varying, "prequalabr" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."rate_attributes"("dirpa" character varying, "dirpb" character varying, "streetnamea" character varying, "streetnameb" character varying, "streettypea" character varying, "streettypeb" character varying, "dirsa" character varying, "dirsb" character varying, "prequalabr" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "rate_attributes"("dirpa" character varying, "dirpb" character varying, "streetnamea" character varying, "streetnameb" character varying, "streettypea" character varying, "streettypeb" character varying, "dirsa" character varying, "dirsb" character varying, "locationa" character varying, "locationb" character varying, "prequalabr" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."rate_attributes"("dirpa" character varying, "dirpb" character varying, "streetnamea" character varying, "streetnameb" character varying, "streettypea" character varying, "streettypeb" character varying, "dirsa" character varying, "dirsb" character varying, "locationa" character varying, "locationb" character varying, "prequalabr" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "reverse_geocode"("pt" "tiger"."geometry", "include_strnum_range" boolean, OUT "intpt" "tiger"."geometry"[], OUT "addy" "tiger"."norm_addy"[], OUT "street" character varying[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."reverse_geocode"("pt" "tiger"."geometry", "include_strnum_range" boolean, OUT "intpt" "tiger"."geometry"[], OUT "addy" "tiger"."norm_addy"[], OUT "street" character varying[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "set_geocode_setting"("setting_name" "text", "setting_value" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."set_geocode_setting"("setting_name" "text", "setting_value" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "setsearchpathforinstall"("a_schema_name" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."setsearchpathforinstall"("a_schema_name" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "soundex"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."soundex"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dclosestpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dclosestpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3ddfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3ddfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3ddistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3ddistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3ddwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3ddwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dintersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dintersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dlength"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dlength"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dlineinterpolatepoint"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dlineinterpolatepoint"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dlongestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dlongestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dmakebox"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dmakebox"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dmaxdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dmaxdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dperimeter"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dperimeter"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dshortestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dshortestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_addmeasure"("tiger"."geometry", double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_addmeasure"("tiger"."geometry", double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_addpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_addpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_addpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_addpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_affine"("tiger"."geometry", double precision, double precision, double precision, double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_affine"("tiger"."geometry", double precision, double precision, double precision, double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_affine"("tiger"."geometry", double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_affine"("tiger"."geometry", double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_angle"("line1" "tiger"."geometry", "line2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_angle"("line1" "tiger"."geometry", "line2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_angle"("pt1" "tiger"."geometry", "pt2" "tiger"."geometry", "pt3" "tiger"."geometry", "pt4" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_angle"("pt1" "tiger"."geometry", "pt2" "tiger"."geometry", "pt3" "tiger"."geometry", "pt4" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_area"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_area"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_area"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_area"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_area"("geog" "tiger"."geography", "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_area"("geog" "tiger"."geography", "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_area2d"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_area2d"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asbinary"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asbinary"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asbinary"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asbinary"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asbinary"("tiger"."geography", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asbinary"("tiger"."geography", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asbinary"("tiger"."geometry", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asbinary"("tiger"."geometry", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asencodedpolyline"("geom" "tiger"."geometry", "nprecision" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asencodedpolyline"("geom" "tiger"."geometry", "nprecision" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkb"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkb"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkb"("tiger"."geometry", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkb"("tiger"."geometry", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkt"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkt"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkt"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkt"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkt"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkt"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkt"("tiger"."geography", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkt"("tiger"."geography", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asewkt"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asewkt"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgeojson"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgeojson"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgeojson"("geog" "tiger"."geography", "maxdecimaldigits" integer, "options" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgeojson"("geog" "tiger"."geography", "maxdecimaldigits" integer, "options" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgeojson"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgeojson"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgeojson"("r" "record", "geom_column" "text", "maxdecimaldigits" integer, "pretty_bool" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgeojson"("r" "record", "geom_column" "text", "maxdecimaldigits" integer, "pretty_bool" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgml"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgml"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgml"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgml"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgml"("geog" "tiger"."geography", "maxdecimaldigits" integer, "options" integer, "nprefix" "text", "id" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgml"("geog" "tiger"."geography", "maxdecimaldigits" integer, "options" integer, "nprefix" "text", "id" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgml"("version" integer, "geog" "tiger"."geography", "maxdecimaldigits" integer, "options" integer, "nprefix" "text", "id" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgml"("version" integer, "geog" "tiger"."geography", "maxdecimaldigits" integer, "options" integer, "nprefix" "text", "id" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgml"("version" integer, "geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer, "nprefix" "text", "id" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgml"("version" integer, "geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer, "nprefix" "text", "id" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ashexewkb"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ashexewkb"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ashexewkb"("tiger"."geometry", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ashexewkb"("tiger"."geometry", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_askml"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_askml"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_askml"("geog" "tiger"."geography", "maxdecimaldigits" integer, "nprefix" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_askml"("geog" "tiger"."geography", "maxdecimaldigits" integer, "nprefix" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_askml"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "nprefix" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_askml"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "nprefix" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_aslatlontext"("geom" "tiger"."geometry", "tmpl" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_aslatlontext"("geom" "tiger"."geometry", "tmpl" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmarc21"("geom" "tiger"."geometry", "format" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmarc21"("geom" "tiger"."geometry", "format" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmvtgeom"("geom" "tiger"."geometry", "bounds" "tiger"."box2d", "extent" integer, "buffer" integer, "clip_geom" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmvtgeom"("geom" "tiger"."geometry", "bounds" "tiger"."box2d", "extent" integer, "buffer" integer, "clip_geom" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_assvg"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_assvg"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_assvg"("geog" "tiger"."geography", "rel" integer, "maxdecimaldigits" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_assvg"("geog" "tiger"."geography", "rel" integer, "maxdecimaldigits" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_assvg"("geom" "tiger"."geometry", "rel" integer, "maxdecimaldigits" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_assvg"("geom" "tiger"."geometry", "rel" integer, "maxdecimaldigits" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astext"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astext"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astext"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astext"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astext"("tiger"."geography", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astext"("tiger"."geography", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astext"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astext"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astwkb"("geom" "tiger"."geometry", "prec" integer, "prec_z" integer, "prec_m" integer, "with_sizes" boolean, "with_boxes" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astwkb"("geom" "tiger"."geometry", "prec" integer, "prec_z" integer, "prec_m" integer, "with_sizes" boolean, "with_boxes" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_astwkb"("geom" "tiger"."geometry"[], "ids" bigint[], "prec" integer, "prec_z" integer, "prec_m" integer, "with_sizes" boolean, "with_boxes" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_astwkb"("geom" "tiger"."geometry"[], "ids" bigint[], "prec" integer, "prec_z" integer, "prec_m" integer, "with_sizes" boolean, "with_boxes" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asx3d"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asx3d"("geom" "tiger"."geometry", "maxdecimaldigits" integer, "options" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_azimuth"("geog1" "tiger"."geography", "geog2" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_azimuth"("geog1" "tiger"."geography", "geog2" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_azimuth"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_azimuth"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_bdmpolyfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_bdmpolyfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_bdpolyfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_bdpolyfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_boundary"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_boundary"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_boundingdiagonal"("geom" "tiger"."geometry", "fits" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_boundingdiagonal"("geom" "tiger"."geometry", "fits" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_box2dfromgeohash"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_box2dfromgeohash"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("text", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("text", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("tiger"."geography", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("tiger"."geography", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("text", double precision, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("text", double precision, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("text", double precision, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("text", double precision, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("tiger"."geography", double precision, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("tiger"."geography", double precision, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("tiger"."geography", double precision, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("tiger"."geography", double precision, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("geom" "tiger"."geometry", "radius" double precision, "quadsegs" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("geom" "tiger"."geometry", "radius" double precision, "quadsegs" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buffer"("geom" "tiger"."geometry", "radius" double precision, "options" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buffer"("geom" "tiger"."geometry", "radius" double precision, "options" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_buildarea"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_buildarea"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_centroid"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_centroid"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_centroid"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_centroid"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_centroid"("tiger"."geography", "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_centroid"("tiger"."geography", "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_chaikinsmoothing"("tiger"."geometry", integer, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_chaikinsmoothing"("tiger"."geometry", integer, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_cleangeometry"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_cleangeometry"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clipbybox2d"("geom" "tiger"."geometry", "box" "tiger"."box2d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clipbybox2d"("geom" "tiger"."geometry", "box" "tiger"."box2d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_closestpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_closestpoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_closestpointofapproach"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_closestpointofapproach"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clusterdbscan"("tiger"."geometry", "eps" double precision, "minpoints" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clusterdbscan"("tiger"."geometry", "eps" double precision, "minpoints" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clusterintersecting"("tiger"."geometry"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clusterintersecting"("tiger"."geometry"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clusterkmeans"("geom" "tiger"."geometry", "k" integer, "max_radius" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clusterkmeans"("geom" "tiger"."geometry", "k" integer, "max_radius" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clusterwithin"("tiger"."geometry"[], double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clusterwithin"("tiger"."geometry"[], double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_collect"("tiger"."geometry"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_collect"("tiger"."geometry"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_collect"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_collect"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_collectionextract"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_collectionextract"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_collectionextract"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_collectionextract"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_collectionhomogenize"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_collectionhomogenize"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_combinebbox"("tiger"."box2d", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_combinebbox"("tiger"."box2d", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_combinebbox"("tiger"."box3d", "tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_combinebbox"("tiger"."box3d", "tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_combinebbox"("tiger"."box3d", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_combinebbox"("tiger"."box3d", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_concavehull"("param_geom" "tiger"."geometry", "param_pctconvex" double precision, "param_allow_holes" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_concavehull"("param_geom" "tiger"."geometry", "param_pctconvex" double precision, "param_allow_holes" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_contains"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_contains"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_containsproperly"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_containsproperly"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_convexhull"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_convexhull"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_coorddim"("geometry" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_coorddim"("geometry" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_coveredby"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_coveredby"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_coveredby"("geog1" "tiger"."geography", "geog2" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_coveredby"("geog1" "tiger"."geography", "geog2" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_coveredby"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_coveredby"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_covers"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_covers"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_covers"("geog1" "tiger"."geography", "geog2" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_covers"("geog1" "tiger"."geography", "geog2" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_covers"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_covers"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_cpawithin"("tiger"."geometry", "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_cpawithin"("tiger"."geometry", "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_crosses"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_crosses"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_curvetoline"("geom" "tiger"."geometry", "tol" double precision, "toltype" integer, "flags" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_curvetoline"("geom" "tiger"."geometry", "tol" double precision, "toltype" integer, "flags" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_delaunaytriangles"("g1" "tiger"."geometry", "tolerance" double precision, "flags" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_delaunaytriangles"("g1" "tiger"."geometry", "tolerance" double precision, "flags" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dfullywithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_difference"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_difference"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dimension"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dimension"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_disjoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_disjoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distance"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distance"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distance"("geog1" "tiger"."geography", "geog2" "tiger"."geography", "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distance"("geog1" "tiger"."geography", "geog2" "tiger"."geography", "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distancecpa"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distancecpa"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distancesphere"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distancesphere"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distancesphere"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "radius" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distancesphere"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "radius" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distancespheroid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distancespheroid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_distancespheroid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "tiger"."spheroid"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_distancespheroid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "tiger"."spheroid") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dump"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dump"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dumppoints"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dumppoints"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dumprings"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dumprings"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dumpsegments"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dumpsegments"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dwithin"("text", "text", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dwithin"("text", "text", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dwithin"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_dwithin"("geog1" "tiger"."geography", "geog2" "tiger"."geography", "tolerance" double precision, "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_dwithin"("geog1" "tiger"."geography", "geog2" "tiger"."geography", "tolerance" double precision, "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_endpoint"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_endpoint"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_envelope"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_envelope"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_equals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_equals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_estimatedextent"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_estimatedextent"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_estimatedextent"("text", "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_estimatedextent"("text", "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_estimatedextent"("text", "text", "text", boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_estimatedextent"("text", "text", "text", boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_expand"("tiger"."box2d", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_expand"("tiger"."box2d", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_expand"("tiger"."box3d", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_expand"("tiger"."box3d", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_expand"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_expand"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_expand"("box" "tiger"."box2d", "dx" double precision, "dy" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_expand"("box" "tiger"."box2d", "dx" double precision, "dy" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_expand"("box" "tiger"."box3d", "dx" double precision, "dy" double precision, "dz" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_expand"("box" "tiger"."box3d", "dx" double precision, "dy" double precision, "dz" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_expand"("geom" "tiger"."geometry", "dx" double precision, "dy" double precision, "dz" double precision, "dm" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_expand"("geom" "tiger"."geometry", "dx" double precision, "dy" double precision, "dz" double precision, "dm" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_exteriorring"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_exteriorring"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_filterbym"("tiger"."geometry", double precision, double precision, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_filterbym"("tiger"."geometry", double precision, double precision, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_findextent"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_findextent"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_findextent"("text", "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_findextent"("text", "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_flipcoordinates"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_flipcoordinates"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_force2d"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_force2d"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_force3d"("geom" "tiger"."geometry", "zvalue" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_force3d"("geom" "tiger"."geometry", "zvalue" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_force3dm"("geom" "tiger"."geometry", "mvalue" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_force3dm"("geom" "tiger"."geometry", "mvalue" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_force3dz"("geom" "tiger"."geometry", "zvalue" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_force3dz"("geom" "tiger"."geometry", "zvalue" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_force4d"("geom" "tiger"."geometry", "zvalue" double precision, "mvalue" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_force4d"("geom" "tiger"."geometry", "zvalue" double precision, "mvalue" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcecollection"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcecollection"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcecurve"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcecurve"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcepolygonccw"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcepolygonccw"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcepolygoncw"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcepolygoncw"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcerhr"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcerhr"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcesfs"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcesfs"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_forcesfs"("tiger"."geometry", "version" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_forcesfs"("tiger"."geometry", "version" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_frechetdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_frechetdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_fromflatgeobuf"("anyelement", "bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_fromflatgeobuf"("anyelement", "bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_fromflatgeobuftotable"("text", "text", "bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_fromflatgeobuftotable"("text", "text", "bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_generatepoints"("area" "tiger"."geometry", "npoints" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_generatepoints"("area" "tiger"."geometry", "npoints" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_generatepoints"("area" "tiger"."geometry", "npoints" integer, "seed" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_generatepoints"("area" "tiger"."geometry", "npoints" integer, "seed" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geogfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geogfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geogfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geogfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geographyfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geographyfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geohash"("geog" "tiger"."geography", "maxchars" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geohash"("geog" "tiger"."geography", "maxchars" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geohash"("geom" "tiger"."geometry", "maxchars" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geohash"("geom" "tiger"."geometry", "maxchars" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomcollfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomcollfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomcollfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomcollfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomcollfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomcollfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomcollfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomcollfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geometricmedian"("g" "tiger"."geometry", "tolerance" double precision, "max_iter" integer, "fail_if_not_converged" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geometricmedian"("g" "tiger"."geometry", "tolerance" double precision, "max_iter" integer, "fail_if_not_converged" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geometryfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geometryfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geometryfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geometryfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geometryn"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geometryn"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geometrytype"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geometrytype"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromewkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromewkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromewkt"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromewkt"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromgeohash"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromgeohash"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromgeojson"("json"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromgeojson"("json") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromgeojson"("jsonb"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromgeojson"("jsonb") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromgeojson"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromgeojson"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromgml"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromgml"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromgml"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromgml"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromkml"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromkml"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfrommarc21"("marc21xml" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfrommarc21"("marc21xml" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromtwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromtwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_geomfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_geomfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_gmltosql"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_gmltosql"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_gmltosql"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_gmltosql"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_hasarc"("geometry" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_hasarc"("geometry" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_hausdorffdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_hausdorffdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_hausdorffdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_hausdorffdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_hexagon"("size" double precision, "cell_i" integer, "cell_j" integer, "origin" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_hexagon"("size" double precision, "cell_i" integer, "cell_j" integer, "origin" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_hexagongrid"("size" double precision, "bounds" "tiger"."geometry", OUT "geom" "tiger"."geometry", OUT "i" integer, OUT "j" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_hexagongrid"("size" double precision, "bounds" "tiger"."geometry", OUT "geom" "tiger"."geometry", OUT "i" integer, OUT "j" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_interiorringn"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_interiorringn"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_interpolatepoint"("line" "tiger"."geometry", "point" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_interpolatepoint"("line" "tiger"."geometry", "point" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_intersection"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_intersection"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_intersection"("tiger"."geography", "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_intersection"("tiger"."geography", "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_intersection"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_intersection"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_intersects"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_intersects"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_intersects"("geog1" "tiger"."geography", "geog2" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_intersects"("geog1" "tiger"."geography", "geog2" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_intersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_intersects"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isclosed"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isclosed"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_iscollection"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_iscollection"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isempty"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isempty"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ispolygonccw"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ispolygonccw"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ispolygoncw"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ispolygoncw"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isring"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isring"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_issimple"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_issimple"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isvalid"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isvalid"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isvalid"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isvalid"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isvaliddetail"("geom" "tiger"."geometry", "flags" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isvaliddetail"("geom" "tiger"."geometry", "flags" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isvalidreason"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isvalidreason"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isvalidreason"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isvalidreason"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_isvalidtrajectory"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_isvalidtrajectory"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_length"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_length"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_length"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_length"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_length"("geog" "tiger"."geography", "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_length"("geog" "tiger"."geography", "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_length2d"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_length2d"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_length2dspheroid"("tiger"."geometry", "tiger"."spheroid"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_length2dspheroid"("tiger"."geometry", "tiger"."spheroid") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_lengthspheroid"("tiger"."geometry", "tiger"."spheroid"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_lengthspheroid"("tiger"."geometry", "tiger"."spheroid") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_letters"("letters" "text", "font" "json"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_letters"("letters" "text", "font" "json") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linecrossingdirection"("line1" "tiger"."geometry", "line2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linecrossingdirection"("line1" "tiger"."geometry", "line2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linefromencodedpolyline"("txtin" "text", "nprecision" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linefromencodedpolyline"("txtin" "text", "nprecision" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linefrommultipoint"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linefrommultipoint"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linefromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linefromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linefromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linefromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linefromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linefromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linefromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linefromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_lineinterpolatepoint"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_lineinterpolatepoint"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_lineinterpolatepoints"("tiger"."geometry", double precision, "repeat" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_lineinterpolatepoints"("tiger"."geometry", double precision, "repeat" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linelocatepoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linelocatepoint"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linemerge"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linemerge"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linemerge"("tiger"."geometry", boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linemerge"("tiger"."geometry", boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linestringfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linestringfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linestringfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linestringfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linesubstring"("tiger"."geometry", double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linesubstring"("tiger"."geometry", double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_linetocurve"("geometry" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_linetocurve"("geometry" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_locatealong"("geometry" "tiger"."geometry", "measure" double precision, "leftrightoffset" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_locatealong"("geometry" "tiger"."geometry", "measure" double precision, "leftrightoffset" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_locatebetween"("geometry" "tiger"."geometry", "frommeasure" double precision, "tomeasure" double precision, "leftrightoffset" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_locatebetween"("geometry" "tiger"."geometry", "frommeasure" double precision, "tomeasure" double precision, "leftrightoffset" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_locatebetweenelevations"("geometry" "tiger"."geometry", "fromelevation" double precision, "toelevation" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_locatebetweenelevations"("geometry" "tiger"."geometry", "fromelevation" double precision, "toelevation" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_longestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_longestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_m"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_m"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makebox2d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makebox2d"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makeenvelope"(double precision, double precision, double precision, double precision, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makeenvelope"(double precision, double precision, double precision, double precision, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makeline"("tiger"."geometry"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makeline"("tiger"."geometry"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makeline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makeline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makepoint"(double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makepoint"(double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makepoint"(double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makepoint"(double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makepoint"(double precision, double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makepoint"(double precision, double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makepointm"(double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makepointm"(double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makepolygon"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makepolygon"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makepolygon"("tiger"."geometry", "tiger"."geometry"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makepolygon"("tiger"."geometry", "tiger"."geometry"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makevalid"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makevalid"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makevalid"("geom" "tiger"."geometry", "params" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makevalid"("geom" "tiger"."geometry", "params" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_maxdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_maxdistance"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_maximuminscribedcircle"("tiger"."geometry", OUT "center" "tiger"."geometry", OUT "nearest" "tiger"."geometry", OUT "radius" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_maximuminscribedcircle"("tiger"."geometry", OUT "center" "tiger"."geometry", OUT "nearest" "tiger"."geometry", OUT "radius" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_memsize"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_memsize"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_minimumboundingcircle"("inputgeom" "tiger"."geometry", "segs_per_quarter" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_minimumboundingcircle"("inputgeom" "tiger"."geometry", "segs_per_quarter" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_minimumboundingradius"("tiger"."geometry", OUT "center" "tiger"."geometry", OUT "radius" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_minimumboundingradius"("tiger"."geometry", OUT "center" "tiger"."geometry", OUT "radius" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_minimumclearance"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_minimumclearance"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_minimumclearanceline"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_minimumclearanceline"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mlinefromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mlinefromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mlinefromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mlinefromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mlinefromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mlinefromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mlinefromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mlinefromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpointfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpointfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpointfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpointfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpointfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpointfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpointfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpointfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpolyfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpolyfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpolyfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpolyfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpolyfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpolyfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_mpolyfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_mpolyfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multi"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multi"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multilinefromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multilinefromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multilinestringfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multilinestringfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multilinestringfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multilinestringfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipointfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipointfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipointfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipointfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipointfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipointfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipolyfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipolyfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipolyfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipolyfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipolygonfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipolygonfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_multipolygonfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_multipolygonfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ndims"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ndims"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_node"("g" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_node"("g" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_normalize"("geom" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_normalize"("geom" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_npoints"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_npoints"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_nrings"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_nrings"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_numgeometries"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_numgeometries"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_numinteriorring"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_numinteriorring"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_numinteriorrings"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_numinteriorrings"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_numpatches"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_numpatches"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_numpoints"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_numpoints"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_offsetcurve"("line" "tiger"."geometry", "distance" double precision, "params" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_offsetcurve"("line" "tiger"."geometry", "distance" double precision, "params" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_orderingequals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_orderingequals"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_orientedenvelope"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_orientedenvelope"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_overlaps"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_overlaps"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_patchn"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_patchn"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_perimeter"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_perimeter"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_perimeter"("geog" "tiger"."geography", "use_spheroid" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_perimeter"("geog" "tiger"."geography", "use_spheroid" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_perimeter2d"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_perimeter2d"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_point"(double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_point"(double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_point"(double precision, double precision, "srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_point"(double precision, double precision, "srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointfromgeohash"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointfromgeohash"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointinsidecircle"("tiger"."geometry", double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointinsidecircle"("tiger"."geometry", double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointm"("xcoordinate" double precision, "ycoordinate" double precision, "mcoordinate" double precision, "srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointm"("xcoordinate" double precision, "ycoordinate" double precision, "mcoordinate" double precision, "srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointn"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointn"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointonsurface"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointonsurface"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_points"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_points"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointz"("xcoordinate" double precision, "ycoordinate" double precision, "zcoordinate" double precision, "srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointz"("xcoordinate" double precision, "ycoordinate" double precision, "zcoordinate" double precision, "srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_pointzm"("xcoordinate" double precision, "ycoordinate" double precision, "zcoordinate" double precision, "mcoordinate" double precision, "srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_pointzm"("xcoordinate" double precision, "ycoordinate" double precision, "zcoordinate" double precision, "mcoordinate" double precision, "srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polyfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polyfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polyfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polyfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polyfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polyfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polyfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polyfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygon"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygon"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygonfromtext"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygonfromtext"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygonfromtext"("text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygonfromtext"("text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygonfromwkb"("bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygonfromwkb"("bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygonfromwkb"("bytea", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygonfromwkb"("bytea", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygonize"("tiger"."geometry"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygonize"("tiger"."geometry"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_project"("geog" "tiger"."geography", "distance" double precision, "azimuth" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_project"("geog" "tiger"."geography", "distance" double precision, "azimuth" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_quantizecoordinates"("g" "tiger"."geometry", "prec_x" integer, "prec_y" integer, "prec_z" integer, "prec_m" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_quantizecoordinates"("g" "tiger"."geometry", "prec_x" integer, "prec_y" integer, "prec_z" integer, "prec_m" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_reduceprecision"("geom" "tiger"."geometry", "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_reduceprecision"("geom" "tiger"."geometry", "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_relate"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_relate"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_relate"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_relate"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_relate"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_relate"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_relatematch"("text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_relatematch"("text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_removepoint"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_removepoint"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_removerepeatedpoints"("geom" "tiger"."geometry", "tolerance" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_removerepeatedpoints"("geom" "tiger"."geometry", "tolerance" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_reverse"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_reverse"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_rotate"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_rotate"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_rotate"("tiger"."geometry", double precision, "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_rotate"("tiger"."geometry", double precision, "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_rotate"("tiger"."geometry", double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_rotate"("tiger"."geometry", double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_rotatex"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_rotatex"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_rotatey"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_rotatey"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_rotatez"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_rotatez"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_scale"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_scale"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_scale"("tiger"."geometry", double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_scale"("tiger"."geometry", double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_scale"("tiger"."geometry", "tiger"."geometry", "origin" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_scale"("tiger"."geometry", "tiger"."geometry", "origin" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_scale"("tiger"."geometry", double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_scale"("tiger"."geometry", double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_scroll"("tiger"."geometry", "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_scroll"("tiger"."geometry", "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_segmentize"("geog" "tiger"."geography", "max_segment_length" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_segmentize"("geog" "tiger"."geography", "max_segment_length" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_segmentize"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_segmentize"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_seteffectivearea"("tiger"."geometry", double precision, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_seteffectivearea"("tiger"."geometry", double precision, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_setpoint"("tiger"."geometry", integer, "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_setpoint"("tiger"."geometry", integer, "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_setsrid"("geog" "tiger"."geography", "srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_setsrid"("geog" "tiger"."geography", "srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_setsrid"("geom" "tiger"."geometry", "srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_setsrid"("geom" "tiger"."geometry", "srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_sharedpaths"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_sharedpaths"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_shiftlongitude"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_shiftlongitude"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_shortestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_shortestline"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_simplify"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_simplify"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_simplify"("tiger"."geometry", double precision, boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_simplify"("tiger"."geometry", double precision, boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_simplifypolygonhull"("geom" "tiger"."geometry", "vertex_fraction" double precision, "is_outer" boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_simplifypolygonhull"("geom" "tiger"."geometry", "vertex_fraction" double precision, "is_outer" boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_simplifypreservetopology"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_simplifypreservetopology"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_simplifyvw"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_simplifyvw"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_snap"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_snap"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_snaptogrid"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_snaptogrid"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_snaptogrid"("tiger"."geometry", double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_snaptogrid"("tiger"."geometry", double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_snaptogrid"("tiger"."geometry", double precision, double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_snaptogrid"("tiger"."geometry", double precision, double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_snaptogrid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision, double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_snaptogrid"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", double precision, double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_split"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_split"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_square"("size" double precision, "cell_i" integer, "cell_j" integer, "origin" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_square"("size" double precision, "cell_i" integer, "cell_j" integer, "origin" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_squaregrid"("size" double precision, "bounds" "tiger"."geometry", OUT "geom" "tiger"."geometry", OUT "i" integer, OUT "j" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_squaregrid"("size" double precision, "bounds" "tiger"."geometry", OUT "geom" "tiger"."geometry", OUT "i" integer, OUT "j" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_srid"("geog" "tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_srid"("geog" "tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_srid"("geom" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_srid"("geom" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_startpoint"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_startpoint"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_subdivide"("geom" "tiger"."geometry", "maxvertices" integer, "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_subdivide"("geom" "tiger"."geometry", "maxvertices" integer, "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_summary"("tiger"."geography"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_summary"("tiger"."geography") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_summary"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_summary"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_swapordinates"("geom" "tiger"."geometry", "ords" "cstring"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_swapordinates"("geom" "tiger"."geometry", "ords" "cstring") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_symdifference"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_symdifference"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_symmetricdifference"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_symmetricdifference"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_tileenvelope"("zoom" integer, "x" integer, "y" integer, "bounds" "tiger"."geometry", "margin" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_tileenvelope"("zoom" integer, "x" integer, "y" integer, "bounds" "tiger"."geometry", "margin" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_touches"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_touches"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_transform"("tiger"."geometry", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_transform"("tiger"."geometry", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_transform"("geom" "tiger"."geometry", "to_proj" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_transform"("geom" "tiger"."geometry", "to_proj" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_transform"("geom" "tiger"."geometry", "from_proj" "text", "to_srid" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_transform"("geom" "tiger"."geometry", "from_proj" "text", "to_srid" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_transform"("geom" "tiger"."geometry", "from_proj" "text", "to_proj" "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_transform"("geom" "tiger"."geometry", "from_proj" "text", "to_proj" "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_translate"("tiger"."geometry", double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_translate"("tiger"."geometry", double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_translate"("tiger"."geometry", double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_translate"("tiger"."geometry", double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_transscale"("tiger"."geometry", double precision, double precision, double precision, double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_transscale"("tiger"."geometry", double precision, double precision, double precision, double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_triangulatepolygon"("g1" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_triangulatepolygon"("g1" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_unaryunion"("tiger"."geometry", "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_unaryunion"("tiger"."geometry", "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_union"("tiger"."geometry"[]); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_union"("tiger"."geometry"[]) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_union"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_union"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_union"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_union"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry", "gridsize" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_voronoilines"("g1" "tiger"."geometry", "tolerance" double precision, "extend_to" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_voronoilines"("g1" "tiger"."geometry", "tolerance" double precision, "extend_to" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_voronoipolygons"("g1" "tiger"."geometry", "tolerance" double precision, "extend_to" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_voronoipolygons"("g1" "tiger"."geometry", "tolerance" double precision, "extend_to" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_within"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_within"("geom1" "tiger"."geometry", "geom2" "tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_wkbtosql"("wkb" "bytea"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_wkbtosql"("wkb" "bytea") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_wkttosql"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_wkttosql"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_wrapx"("geom" "tiger"."geometry", "wrap" double precision, "move" double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_wrapx"("geom" "tiger"."geometry", "wrap" double precision, "move" double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_x"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_x"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_xmax"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_xmax"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_xmin"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_xmin"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_y"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_y"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ymax"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ymax"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_ymin"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_ymin"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_z"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_z"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_zmax"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_zmax"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_zmflag"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_zmflag"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_zmin"("tiger"."box3d"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_zmin"("tiger"."box3d") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "state_extract"("rawinput" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."state_extract"("rawinput" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "text_soundex"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."text_soundex"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "topology_load_tiger"("toponame" character varying, "region_type" character varying, "region_id" character varying); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."topology_load_tiger"("toponame" character varying, "region_type" character varying, "region_id" character varying) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "unlockrows"("text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."unlockrows"("text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "updategeometrysrid"(character varying, character varying, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."updategeometrysrid"(character varying, character varying, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "updategeometrysrid"(character varying, character varying, character varying, integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."updategeometrysrid"(character varying, character varying, character varying, integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "updategeometrysrid"("catalogn_name" character varying, "schema_name" character varying, "table_name" character varying, "column_name" character varying, "new_srid_in" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."updategeometrysrid"("catalogn_name" character varying, "schema_name" character varying, "table_name" character varying, "column_name" character varying, "new_srid_in" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "utmzone"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."utmzone"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "zip_range"("zip" "text", "range_start" integer, "range_end" integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."zip_range"("zip" "text", "range_start" integer, "range_end" integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "avg"("extensions"."vector"); Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "extensions"."avg"("extensions"."vector") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_3dextent"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_3dextent"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asflatgeobuf"("anyelement"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asflatgeobuf"("anyelement") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asflatgeobuf"("anyelement", boolean); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asflatgeobuf"("anyelement", boolean) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asflatgeobuf"("anyelement", boolean, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asflatgeobuf"("anyelement", boolean, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgeobuf"("anyelement"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgeobuf"("anyelement") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asgeobuf"("anyelement", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asgeobuf"("anyelement", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmvt"("anyelement"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmvt"("anyelement") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmvt"("anyelement", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmvt"("anyelement", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmvt"("anyelement", "text", integer); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmvt"("anyelement", "text", integer) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmvt"("anyelement", "text", integer, "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmvt"("anyelement", "text", integer, "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_asmvt"("anyelement", "text", integer, "text", "text"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_asmvt"("anyelement", "text", integer, "text", "text") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clusterintersecting"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clusterintersecting"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_clusterwithin"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_clusterwithin"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_collect"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_collect"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_extent"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_extent"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_makeline"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_makeline"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_memcollect"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_memcollect"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_memunion"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_memunion"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_polygonize"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_polygonize"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_union"("tiger"."geometry"); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_union"("tiger"."geometry") TO "postgres" WITH GRANT OPTION;


--
-- Name: FUNCTION "st_union"("tiger"."geometry", double precision); Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON FUNCTION "tiger"."st_union"("tiger"."geometry", double precision) TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "pg_stat_statements"; Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "extensions"."pg_stat_statements" TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON TABLE "extensions"."pg_stat_statements" TO "dashboard_user";


--
-- Name: TABLE "pg_stat_statements_info"; Type: ACL; Schema: extensions; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "extensions"."pg_stat_statements_info" TO "postgres" WITH GRANT OPTION;
-- GRANT ALL ON TABLE "extensions"."pg_stat_statements_info" TO "dashboard_user";


--
-- Name: SEQUENCE "seq_schema_version"; Type: ACL; Schema: graphql; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "graphql"."seq_schema_version" TO "postgres";
-- GRANT ALL ON SEQUENCE "graphql"."seq_schema_version" TO "anon";
-- GRANT ALL ON SEQUENCE "graphql"."seq_schema_version" TO "authenticated";
-- GRANT ALL ON SEQUENCE "graphql"."seq_schema_version" TO "service_role";


--
-- Name: TABLE "decrypted_key"; Type: ACL; Schema: pgsodium; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "pgsodium"."decrypted_key" TO "pgsodium_keyholder";


--
-- Name: TABLE "masking_rule"; Type: ACL; Schema: pgsodium; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "pgsodium"."masking_rule" TO "pgsodium_keyholder";


--
-- Name: TABLE "mask_columns"; Type: ACL; Schema: pgsodium; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "pgsodium"."mask_columns" TO "pgsodium_keyholder";


--
-- Name: TABLE "Game"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."Game" TO "anon";
GRANT ALL ON TABLE "public"."Game" TO "authenticated";
GRANT ALL ON TABLE "public"."Game" TO "service_role";


--
-- Name: TABLE "GamePlayer"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."GamePlayer" TO "anon";
GRANT ALL ON TABLE "public"."GamePlayer" TO "authenticated";
GRANT ALL ON TABLE "public"."GamePlayer" TO "service_role";


--
-- Name: TABLE "GameThing"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."GameThing" TO "anon";
GRANT ALL ON TABLE "public"."GameThing" TO "authenticated";
GRANT ALL ON TABLE "public"."GameThing" TO "service_role";


--
-- Name: TABLE "Guess"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."Guess" TO "anon";
GRANT ALL ON TABLE "public"."Guess" TO "authenticated";
GRANT ALL ON TABLE "public"."Guess" TO "service_role";


--
-- Name: TABLE "Player"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."Player" TO "anon";
GRANT ALL ON TABLE "public"."Player" TO "authenticated";
GRANT ALL ON TABLE "public"."Player" TO "service_role";


--
-- Name: TABLE "PlayerFriend"; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE "public"."PlayerFriend" TO "anon";
GRANT ALL ON TABLE "public"."PlayerFriend" TO "authenticated";
GRANT ALL ON TABLE "public"."PlayerFriend" TO "service_role";


--
-- Name: TABLE "addr"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."addr" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "addr_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."addr_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "addrfeat"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."addrfeat" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "addrfeat_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."addrfeat_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "bg"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."bg" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "bg_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."bg_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "county"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."county" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "county_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."county_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "county_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."county_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "countysub_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."countysub_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "cousub"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."cousub" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "cousub_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."cousub_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "direction_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."direction_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "edges"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."edges" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "edges_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."edges_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "faces"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."faces" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "faces_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."faces_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "featnames"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."featnames" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "featnames_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."featnames_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "geocode_settings"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."geocode_settings" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "geocode_settings_default"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."geocode_settings_default" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "geography_columns"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."geography_columns" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "geometry_columns"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."geometry_columns" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "loader_lookuptables"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."loader_lookuptables" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "loader_platform"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."loader_platform" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "loader_variables"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."loader_variables" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "pagc_gaz"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."pagc_gaz" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "pagc_gaz_id_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."pagc_gaz_id_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "pagc_lex"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."pagc_lex" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "pagc_lex_id_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."pagc_lex_id_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "pagc_rules"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."pagc_rules" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "pagc_rules_id_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."pagc_rules_id_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "place"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."place" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "place_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."place_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "place_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."place_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "secondary_unit_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."secondary_unit_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "spatial_ref_sys"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."spatial_ref_sys" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "state"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."state" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "state_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."state_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "state_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."state_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "street_type_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."street_type_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "tabblock"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."tabblock" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "tabblock20"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."tabblock20" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "tabblock_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."tabblock_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "tract"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."tract" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "tract_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."tract_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "zcta5"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."zcta5" TO "postgres" WITH GRANT OPTION;


--
-- Name: SEQUENCE "zcta5_gid_seq"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON SEQUENCE "tiger"."zcta5_gid_seq" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "zip_lookup"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."zip_lookup" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "zip_lookup_all"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."zip_lookup_all" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "zip_lookup_base"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."zip_lookup_base" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "zip_state"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."zip_state" TO "postgres" WITH GRANT OPTION;


--
-- Name: TABLE "zip_state_loc"; Type: ACL; Schema: tiger; Owner: supabase_admin
--

-- GRANT ALL ON TABLE "tiger"."zip_state_loc" TO "postgres" WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "service_role";


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: supabase_admin
--

-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "postgres";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "anon";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "authenticated";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "service_role";


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "service_role";


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: public; Owner: supabase_admin
--

-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "postgres";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "anon";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "authenticated";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "service_role";


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "service_role";


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: supabase_admin
--

-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON TABLES  TO "postgres";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON TABLES  TO "anon";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON TABLES  TO "authenticated";
-- ALTER DEFAULT PRIVILEGES FOR ROLE "supabase_admin" IN SCHEMA "public" GRANT ALL ON TABLES  TO "service_role";


--
-- PostgreSQL database dump complete
--

RESET ALL;

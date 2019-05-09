--
-- PostgreSQL database dump
--

-- Dumped from database version 11.2
-- Dumped by pg_dump version 11.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mydummy; Type: TABLE; Schema: public; Owner: tester
--

CREATE TABLE public.mydummy (
    id integer NOT NULL,
    name character varying(128),
    description character varying(512),
    bigno bigint,
    mydate date,
    mydouble double precision,
    myint integer,
    myjson json,
    mymoney money,
    mysmallint smallint,
    mytime time without time zone,
    mytimetz time with time zone,
    mytimestamptz timestamp with time zone,
    mytimestamp timestamp without time zone
);


ALTER TABLE public.mydummy OWNER TO tester;

--
-- Data for Name: mydummy; Type: TABLE DATA; Schema: public; Owner: tester
--

INSERT INTO public.mydummy (id, name, description, bigno, mydate, mydouble, myint, myjson, mymoney, mysmallint, mytime, mytimetz, mytimestamptz, mytimestamp) VALUES (1, 'firstentry', 'no data one might want to use', 9009095643568, '2019-03-28', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.mydummy (id, name, description, bigno, mydate, mydouble, myint, myjson, mymoney, mysmallint, mytime, mytimetz, mytimestamptz, mytimestamp) VALUES (3, 'thirdentry', 'more nonsense for testing', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.mydummy (id, name, description, bigno, mydate, mydouble, myint, myjson, mymoney, mysmallint, mytime, mytimetz, mytimestamptz, mytimestamp) VALUES (2, 'secondentry', 'zulu is also a nice character', 90090947545885, '1999-01-08', 397.970000000000027, 197, '{"affe":"hirsch"}', '89,98', 8, '22:59:37.487', '22:59:37.487+00', '2018-09-17 19:07:34+02', '2012-09-28 00:00:00');


--
-- Name: mydummy mydummy_pkey; Type: CONSTRAINT; Schema: public; Owner: tester
--

ALTER TABLE ONLY public.mydummy
    ADD CONSTRAINT mydummy_pkey PRIMARY KEY (id);


--
-- Name: mydummy_id; Type: INDEX; Schema: public; Owner: tester
--

CREATE INDEX mydummy_id ON public.mydummy USING btree (id);


--
-- Name: TABLE mydummy; Type: ACL; Schema: public; Owner: tester
--

GRANT SELECT ON TABLE public.mydummy TO tester;


--
-- PostgreSQL database dump complete
--


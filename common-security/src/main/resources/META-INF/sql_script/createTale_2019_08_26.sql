CREATE TABLE IF NOT EXISTS public.userinfo
(
    id SERIAL NOT NULL,
    blocked boolean,
    password character varying(255),
    username character varying(255)
);

CREATE EXTENSION pgcrypto;
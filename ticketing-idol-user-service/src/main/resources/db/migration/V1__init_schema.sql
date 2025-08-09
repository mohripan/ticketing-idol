create extension if not exists "uuid-ossp";

create table if not exists users (
                                     id uuid primary key,
                                     keycloak_id uuid not null unique,
                                     username text not null unique,
                                     profile_picture_id uuid,
                                     created_at timestamptz not null,
                                     updated_at timestamptz not null
);

create table if not exists organizers (
                                          user_id uuid primary key references users(id) on delete cascade,
                                          organization_name text not null,
                                          verified boolean not null default false,
                                          created_at timestamptz not null,
                                          updated_at timestamptz not null
);
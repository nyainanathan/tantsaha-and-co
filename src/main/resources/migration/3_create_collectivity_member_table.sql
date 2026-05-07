create table if not exists "collectivity_member"
(
    id              varchar primary key,
    member_id       varchar references "member" (id),
    collectivity_id varchar references "collectivity" (id)
);

alter table collectivity_member add column adhesion_date date default current_date;
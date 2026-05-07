create type activity_type as enum('MEETING', 'TRAINING', 'OTHER');

create type day_of_week as enum('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU');

create table if not exists "activity" (
    id varchar primary key,
    label varchar,
    type activity_type,
    executive_date date,
    week_ordinal int,
    day_of_week day_of_week
);

create table if not exists activity_member_concerned(
    id varchar primary key,
    id_activity varchar references "activity"(id),
    occupation member_occupation
);
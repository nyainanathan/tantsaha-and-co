do
$$
begin
        if not exists(select from pg_type where typname = 'activity_type') then
create type activity_type as enum ('MEETING', 'TRAINING', 'OTHER');
end if;
end
$$;

do
$$
begin
        if not exists(select from pg_type where typname = 'day_of_week') then
create type day_of_week as enum ('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU');
end if;
end
$$;

create table if not exists "collectivity_activity"
(
    id                varchar primary key,
    label             varchar,
    activity_type     activity_type,
    recurrence_week   integer,
    recurrence_day    day_of_week,
    executive_date    date,
    collectivity_id   varchar references "collectivity" (id)
    );


create table if not exists "activity_occupation"
(
    id          varchar primary key,
    activity_id varchar references "collectivity_activity" (id),
    occupation  member_occupation
    );
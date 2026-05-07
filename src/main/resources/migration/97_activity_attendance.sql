do
$$
begin
        if not exists(select from pg_type where typname = 'attendance_status') then
create type attendance_status as enum ('MISING', 'ATTENDED', 'UNDEFINED');
end if;
end
$$;

create table if not exists activity_attendance(
    id varchar primary key,
    member_id varchar references member(id),
    activity_id varchar references collectivity_activity(id),
    status attendance_status
);
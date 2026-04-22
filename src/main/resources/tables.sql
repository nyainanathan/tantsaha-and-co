alter table cotisation
drop column federation_share_pct;

alter table cotisation
drop column  cot_type;

create type fees_frequency as enum('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');

alter table cotisation
add column frequency fees_frequency default 'WEEKLY';

create type fees_status as enum('ACTIVE', 'INACTIVE');

alter table cotisation
add column status fees_status default 'ACTIVE';

alter table cotisation
drop column due_date;

alter table cotisation
add column eligibleFrom date default current_date;

alter table cotisation rename to fee;

alter table payment drop column  cotisation_id;

alter table fee alter column id type varchar(255);

alter table fee rename column eligiblefrom to eligible_from;

alter table payment
add column fee_id varchar(255) references fee(id);
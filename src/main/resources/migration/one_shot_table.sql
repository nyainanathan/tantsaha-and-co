do
$$
    begin
        if not exists(select from pg_type where typname = 'gender') then
            create type gender as enum ('MALE', 'FEMALE');
        end if;
    end
$$;

do
$$
    begin
        if not exists(select from pg_type where typname = 'member_occupation') then
            create type member_occupation as enum ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');
        end if;
    end
$$;

create table if not exists "member"
(
    id                    varchar primary key,
    first_name            varchar,
    last_name             varchar,
    birth_date            date,
    gender                gender,
    address               varchar,
    profession            varchar,
    phone_number          varchar,
    email                 varchar,
    occupation            member_occupation,
    registration_fee_paid boolean,
    membership_dues_paid  boolean
);

create table if not exists "collectivity"
(
    id                varchar primary key,
    name              varchar,
    number            integer,
    location          varchar,
    specialization    varchar,
    president_id      varchar references "member" (id),
    vice_president_id varchar references "member" (id),
    treasurer_id      varchar references "member" (id),
    secretary_id      varchar references "member" (id)
);

create table if not exists "collectivity_member"
(
    id              varchar primary key,
    member_id       varchar references "member" (id),
    collectivity_id varchar references "collectivity" (id)
);

alter table collectivity_member add column adhesion_date date default current_date;

create table if not exists "member_referee"
(
    id                 varchar primary key,
    member_refereed_id varchar references "member" (id),
    member_referee_id  varchar references "member" (id)
);

do
$$
    begin
        if not exists(select from pg_type where typname = 'frequency') then
            create type frequency as enum (
                'WEEKLY',
                'MONTHLY',
                'ANNUALLY',
                'PUNCTUALLY');
        end if;
    end
$$;

do
$$
    begin
        if not exists(select from pg_type where typname = 'activity_status') then
            create type activity_status as enum (
                'ACTIVE',
                'INACTIVE');
        end if;
    end
$$;

create table if not exists "membership_fee"
(
    id              varchar primary key,
    label           varchar,
    amount          numeric(12, 2),
    eligible_from   date,
    status          activity_status,
    frequency       frequency,
    collectivity_id varchar references "collectivity" (id)
);

create table if not exists cash_account
(
    id              varchar primary key,
    collectivity_id varchar references collectivity (id)
);

alter table cash_account add column amount numeric(10,2);

do
$$
    begin
        if not exists(select from pg_type where typname = 'bank_name') then
            create type bank_name as enum (
                'BRED',
                'MCB',
                'BMOI',
                'BOA',
                'BGFI',
                'AFG',
                'ACCES_BAQUE',
                'BAOBAB',
                'SIPEM');
        end if;
    end
$$;

create table if not exists "bank_account"
(
    id              varchar primary key,
    holder_name     varchar,
    bank_name       bank_name,
    bank_code       integer,
    branch_code     integer,
    account_number  integer,
    key             integer,
    collectivity_id varchar references "collectivity" (id)
);

alter table bank_account add column amount numeric(10,2);
do
$$
    begin
        if not exists(select from pg_type where typname = 'mobile_banking_service') then
            create type mobile_banking_service as enum (
                'ORANGE_MONEY',
                'MVOLA',
                'AIRTEL_MONEY');
        end if;
    end
$$;

create table if not exists "mobile_banking_account"
(
    id              varchar primary key,
    holder_name     varchar,
    service         mobile_banking_service,
    mobile_number   varchar,
    collectivity_id varchar references "collectivity" (id)
);

alter table mobile_banking_account add column amount numeric(10,2);

do
$$
    begin
        if not exists(select from pg_type where typname = 'transaction_type') then
            create type transaction_type as enum (
                'IN',
                'OUT');
        end if;
    end
$$;

create table if not exists "transaction"
(
    id                   varchar primary key,
    amount               numeric(12, 2),
    creation_date        date,
    transaction_type     transaction_type,
    financial_account_id varchar
);

alter table if exists "transaction"
    add column if not exists member_debited_id varchar references member ("id");


    do
$$
    begin
        if not exists(select from pg_type where typname = 'payment_mode') then
            create type payment_mode as enum (
                'BANK_TRANSFER',
                'MOBILE_BANKING',
                'CASH');
        end if;
    end
$$;

create table if not exists "member_payment"
(
    id                   varchar primary key,
    amount               numeric(12, 2),
    creation_date        date,
    member_debited_id    varchar references member ("id"),
    membership_fee_id    varchar references membership_fee ("id"),
    payment_mode         payment_mode,
    financial_account_id varchar
);

alter table bank_account
alter column account_number type bigint;
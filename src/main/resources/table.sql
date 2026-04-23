CREATE TABLE collectivity (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    location VARCHAR(255) NOT NULL,
    speciality VARCHAR(255) NOT NULL
);

CREATE TABLE member (
    id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    gender VARCHAR(6) CHECK (gender IN ('MALE', 'FEMALE')),
    address VARCHAR(255),
    occupation VARCHAR(14) CHECK (occupation IN ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT')),
    phone INT NOT NULL,
    email VARCHAR(255) UNIQUE,
    profession VARCHAR(255),
    collectivity_id VARCHAR(255) REFERENCES collectivity(id) ON DELETE SET NULL
);

CREATE TABLE mentor (
    id SERIAL PRIMARY KEY,
    mentor_member_id VARCHAR(255) NOT NULL REFERENCES member(id),
    mentee_member_id VARCHAR(255) NOT NULL REFERENCES member(id)
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    label VARCHAR(14) CHECK (label IN ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT')),
    is_unique_per_mandate BOOLEAN DEFAULT TRUE
);

CREATE TABLE role_attribution (
    id SERIAL PRIMARY KEY,
    member_id VARCHAR(255) REFERENCES member(id),
    collectivity_id VARCHAR(255) REFERENCES collectivity(id),
    role_id INT NOT NULL REFERENCES role(id),
    attributed_at DATE DEFAULT CURRENT_DATE,
    ended_date DATE
);

-- UPDATE mercedi 22 avril
alter table collectivity
add column number int;


create table fee(
    id varchar(255) primary key,
    collectivity_id varchar(255) references collectivity(id),
    amount numeric(10, 2),
    label varchar(255),
    frequency varchar(11) check (frequency in ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY')),
    status varchar(8) default 'ACTIVE' check (status in ('INACTIVE', 'ACTIVE')),
    eligible_from date default current_date
);





create table bank_account(
    id varchar(255) primary key,
    holder_name varchar(255),
    bank_name varchar(12) check (bank_name in ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BANQUE', 'BAOBAB', 'SIPEM')),
    bank_code varchar(5),
    bank_branch_code varchar(5),
    bank_account_number varchar(11),
    bank_account_key varchar(2),
    amount numeric(10, 2),

    id_collectivity varchar(255) references collectivity(id),
    is_federation boolean default false
);

create table cash_account(
    id varchar(255) primary key,
    amount numeric(10 , 2),

    id_collectivity varchar(255) references collectivity(id),
    is_federation boolean default false
);

create table mobile_banking_account(
    id varchar(255) primary key,
    holder_name varchar(255),
    mobile_banking_service varchar(12) check (mobile_banking_service in ('AIRTEL_MONEY', 'ORANGE_MONEY', 'MVOLA')),
    mobile_number varchar(20),
    amount numeric(10 , 2),

    id_collectivity varchar(255) references collectivity(id),
    is_federation boolean default false
);


create table transaction(
    id serial primary key,
    creation_date date default current_date,
    amount numeric(10, 2),
    payment_mode varchar(14) check (payment_mode in ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER')),
    collectivity_id varchar(255) references collectivity(id),

    account_credited_bank_id          varchar(255) references bank_account(id),
    account_credited_cash_id          varchar(255) references cash_account(id),
    account_credited_mobile_id        varchar(255) references mobile_banking_account(id),
    CHECK (
        (account_credited_bank_id IS NOT NULL)::int +
        (account_credited_cash_id IS NOT NULL)::int +
        (account_credited_mobile_id IS NOT NULL)::int = 1
    )

);

create table payment(
    id varchar(255) primary key,
    amount numeric(10 , 2),
    payment_mode varchar(14) check (payment_mode in ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER')),

    account_credited_bank_id          varchar(255) references bank_account(id),
    account_credited_cash_id          varchar(255) references cash_account(id),
    account_credited_mobile_id        varchar(255) references mobile_banking_account(id),
    CHECK (
        (account_credited_bank_id IS NOT NULL)::int +
        (account_credited_cash_id IS NOT NULL)::int +
        (account_credited_mobile_id IS NOT NULL)::int = 1
    )
,
    creation_date date default current_date,
    fee_id varchar(255) references fee(id)
);


-- UPDATE
alter table collectivity
alter column speciality drop not null;

alter table payment
add column member_id varchar(255) references member(id);


alter table transaction
    add column member_id varchar(255) references member(id);

alter table transaction
    alter column id type varchar(255);

--DATA TO TEST

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, occupation, phone, email, profession, collectivity_id)
VALUES
    ('MBR-001', 'Rakoto', 'Andriamaro', '1985-06-15', 'MALE', '12 Rue de l''Indépendance, Tana', 'SENIOR', 0341234567, 'rakoto.andriamaro@mail.mg', 'Agriculteur', 'COL-001'),
    ('MBR-002', 'Rasoa', 'Rakotondrabe', '1990-03-22', 'FEMALE', '45 Avenue de la Liberté, Tana', 'PRESIDENT', 0331234568, 'rasoa.rakotondrabe@mail.mg', 'Agronome', 'COL-001');

-- Referee link: MBR-002 is referee for MBR-001
INSERT INTO mentor (mentor_member_id, mentee_member_id)
VALUES ('MBR-002', 'MBR-001');


-- Cash account
INSERT INTO cash_account (id, amount, id_collectivity)
VALUES ('CASH-001', 0.00, 'COL-001');

-- Mobile banking account (MVola)
INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number, amount, id_collectivity)
VALUES ('MOB-001', 'Rasoa Rakotondrabe', 'MVOLA', '0331234568', 0.00, 'COL-001');

-- Bank account (BOA)
INSERT INTO bank_account (id, holder_name, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key, amount, id_collectivity)
VALUES ('BANK-001', 'Fédération Analamanga', 'BOA', '10005', '00001', '00012345678', '90', 0.00, 'COL-001');


-- ============================================================
-- 4. MEMBERSHIP FEE
-- ============================================================
INSERT INTO fee (id, collectivity_id, amount, label, frequency, status, eligible_from)
VALUES
    ('FEES-001', 'COL-001', 5000.00, 'Cotisation mensuelle', 'MONTHLY',  'ACTIVE', '2026-01-01'),
    ('FEES-002', 'COL-001', 2500.00, 'Frais d''inscription',  'PUNCTUALLY','ACTIVE', '2026-01-01');
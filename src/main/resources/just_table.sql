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

alter table transaction
    alter column id drop default ;
          -- ============================================================
-- 1. COLLECTIVITIES
-- ============================================================
INSERT INTO collectivity (id, name, location, number, speciality)
VALUES
    ('col-1', 'Mpanorina',      'Ambatondrazaka', 1, 'Riziculture'),
    ('col-2', 'Dobo voalohany', 'Ambatondrazaka', 2, 'Pisciculture'),
    ('col-3', 'Tantely mamy',   'Brickaville',    3, 'Apiculture');


-- ============================================================
-- 2. MEMBERS
-- ============================================================
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone, email, occupation, collectivity_id)
VALUES
    ('C1-M1', 'Prénom membre 1',  'Nom membre 1',  '1980-02-01', 'MALE',   'Lot II V M Ambato.',  'Riziculteur', 341234567, 'member.1@fed-agri.mg',  'PRESIDENT',      'col-1'),
    ('C1-M2', 'Prénom membre 2',  'Nom membre 2',  '1982-03-05', 'MALE',   'Lot II F Ambato.',    'Agriculteur', 321234567, 'member.2@fed-agri.mg',  'VICE_PRESIDENT', 'col-1'),
    ('C1-M3', 'Prénom membre 3',  'Nom membre 3',  '1992-03-10', 'MALE',   'Lot II J Ambato.',    'Collecteur',  331234567, 'member.3@fed-agri.mg',  'SECRETARY',      'col-1'),
    ('C1-M4', 'Prénom membre 4',  'Nom membre 4',  '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.',  'Distributeur',381234567, 'member.4@fed-agri.mg',  'TREASURER',      'col-1'),
    ('C1-M5', 'Prénom membre 5',  'Nom membre 5',  '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',   'Riziculteur', 373434567, 'member.5@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C1-M6', 'Prénom membre 6',  'Nom membre 6',  '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',    'Riziculteur', 372234567, 'member.6@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C1-M7', 'Prénom membre 7',  'Nom membre 7',  '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',    'Riziculteur', 374234567, 'member.7@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C1-M8', 'Prénom membre 8',  'Nom membre 8',  '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',    'Riziculteur', 370234567, 'member.8@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C3-M1', 'Prénom membre 9',  'Nom membre 9',  '1988-01-02', 'MALE',   'Lot 33 J Antsirabe',  'Apiculteur',  34034567,  'member.9@fed-agri.mg',  'PRESIDENT',      'col-3'),
    ('C3-M2', 'Prénom membre 10', 'Nom membre 10', '1982-03-05', 'MALE',   'Lot 2 J Antsirabe',   'Agriculteur', 338634567, 'member.10@fed-agri.mg', 'VICE_PRESIDENT', 'col-3'),
    ('C3-M3', 'Prénom membre 11', 'Nom membre 11', '1992-03-12', 'MALE',   'Lot 8 KM Antsirabe',  'Collecteur',  338234567, 'member.11@fed-agri.mg', 'SECRETARY',      'col-3'),
    ('C3-M4', 'Prénom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe','Distributeur',382334567, 'member.12@fed-agri.mg', 'TREASURER',      'col-3'),
    ('C3-M5', 'Prénom membre 13', 'Nom membre 13', '1999-08-11', 'MALE',   'Lot UV 80 Antsirabe', 'Apiculteur',  373365567, 'member.13@fed-agri.mg', 'SENIOR',         'col-3'),
    ('C3-M6', 'Prénom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe',  'Apiculteur',  378234567, 'member.14@fed-agri.mg', 'SENIOR',         'col-3'),
    ('C3-M7', 'Prénom membre 15', 'Nom membre 15', '1998-01-13', 'MALE',   'Lot UV 7 Antsirabe',  'Apiculteur',  374914567, 'member.15@fed-agri.mg', 'SENIOR',         'col-3'),
    ('C3-M8', 'Prénom membre 16', 'Nom membre 16', '1975-08-02', 'MALE',   'Lot UV 8 Antsirabe',  'Apiculteur',  370634567, 'member.16@fed-agri.mg', 'SENIOR',         'col-3');


-- ============================================================
-- 3. MENTORS (referees)
-- ============================================================

-- col-1 (C1-M1 and C1-M2 have no referees)
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C1-M3'), ('C1-M2', 'C1-M3');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C1-M4'), ('C1-M2', 'C1-M4');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C1-M5'), ('C1-M2', 'C1-M5');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C1-M6'), ('C1-M2', 'C1-M6');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C1-M7'), ('C1-M2', 'C1-M7');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M6', 'C1-M8'), ('C1-M7', 'C1-M8');

-- col-3 (C3-M1 and C3-M2 have no referees)
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C3-M1'), ('C1-M2', 'C3-M1');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C1-M1', 'C3-M2'), ('C1-M2', 'C3-M2');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C3-M1', 'C3-M3'), ('C3-M2', 'C3-M3');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C3-M1', 'C3-M4'), ('C3-M2', 'C3-M4');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C3-M1', 'C3-M5'), ('C3-M2', 'C3-M5');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C3-M1', 'C3-M6'), ('C3-M2', 'C3-M6');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C3-M1', 'C3-M7'), ('C3-M2', 'C3-M7');
INSERT INTO mentor (mentor_member_id, mentee_member_id) VALUES ('C3-M1', 'C3-M8'), ('C3-M2', 'C3-M8');


-- ============================================================
-- 4. FEES
-- ============================================================
INSERT INTO fee (id, collectivity_id, amount, label, frequency, status, eligible_from)
VALUES
    ('cot-1', 'col-1', 100000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE', '2026-01-01'),
    ('cot-2', 'col-2', 100000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE', '2026-01-01'),
    ('cot-3', 'col-3',  50000.00, 'Cotisation annuelle', 'ANNUALLY', 'ACTIVE', '2026-01-01');


-- ============================================================
-- 5. FINANCIAL ACCOUNTS
-- ============================================================

-- col-1
INSERT INTO cash_account (id, amount, id_collectivity)
VALUES ('C1-A-CASH', 0.00, 'col-1');

INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number, amount, id_collectivity)
VALUES ('C1-A-MOBILE-1', 'Mpanorina', 'ORANGE_MONEY', '0370489612', 0.00, 'col-1');

-- col-2
INSERT INTO cash_account (id, amount, id_collectivity)
VALUES ('C2-A-CASH', 0.00, 'col-2');

INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number, amount, id_collectivity)
VALUES ('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', '0320489612', 0.00, 'col-2');

-- col-3 (cash only)
INSERT INTO cash_account (id, amount, id_collectivity)
VALUES ('C3-A-CASH', 0.00, 'col-3');


-- ============================================================
-- 6. PAYMENTS
-- ============================================================

-- col-1 (all CASH)
INSERT INTO payment (id, amount, payment_mode, account_credited_cash_id, account_credited_bank_id, account_credited_mobile_id, creation_date, fee_id, member_id)
VALUES
    ('PAY-C1-M1', 100000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M1'),
    ('PAY-C1-M2', 100000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M2'),
    ('PAY-C1-M3', 100000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M3'),
    ('PAY-C1-M4', 100000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M4'),
    ('PAY-C1-M5', 100000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M5'),
    ('PAY-C1-M6', 100000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M6'),
    ('PAY-C1-M7',  60000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M7'),
    ('PAY-C1-M8',  90000.00, 'CASH', 'C1-A-CASH', NULL, NULL, '2026-01-01', 'cot-1', 'C1-M8');

-- col-2 (mixed CASH and MOBILE_BANKING)
INSERT INTO payment (id, amount, payment_mode, account_credited_cash_id, account_credited_bank_id, account_credited_mobile_id, creation_date, fee_id, member_id)
VALUES
    ('PAY-C2-M1',  60000.00, 'CASH',           'C2-A-CASH', NULL, NULL,            '2026-01-01', 'cot-2', 'C1-M1'),
    ('PAY-C2-M2',  90000.00, 'CASH',           'C2-A-CASH', NULL, NULL,            '2026-01-01', 'cot-2', 'C1-M2'),
    ('PAY-C2-M3', 100000.00, 'CASH',           'C2-A-CASH', NULL, NULL,            '2026-01-01', 'cot-2', 'C1-M3'),
    ('PAY-C2-M4', 100000.00, 'CASH',           'C2-A-CASH', NULL, NULL,            '2026-01-01', 'cot-2', 'C1-M4'),
    ('PAY-C2-M5', 100000.00, 'CASH',           'C2-A-CASH', NULL, NULL,            '2026-01-01', 'cot-2', 'C1-M5'),
    ('PAY-C2-M6', 100000.00, 'CASH',           'C2-A-CASH', NULL, NULL,            '2026-01-01', 'cot-2', 'C1-M6'),
    ('PAY-C2-M7',  40000.00, 'MOBILE_BANKING', NULL,        NULL, 'C2-A-MOBILE-1', '2026-01-01', 'cot-2', 'C1-M7'),
    ('PAY-C2-M8',  60000.00, 'MOBILE_BANKING', NULL,        NULL, 'C2-A-MOBILE-1', '2026-01-01', 'cot-2', 'C1-M8');

-- col-3: no payments


-- ============================================================
-- 7. TRANSACTIONS
-- ============================================================

-- col-1
INSERT INTO transaction (id, creation_date, amount, payment_mode, collectivity_id, account_credited_cash_id, account_credited_bank_id, account_credited_mobile_id, member_id)
VALUES
    ('TRX-C1-M1', '2026-01-01', 100000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M1'),
    ('TRX-C1-M2', '2026-01-01', 100000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M2'),
    ('TRX-C1-M3', '2026-01-01', 100000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M3'),
    ('TRX-C1-M4', '2026-01-01', 100000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M4'),
    ('TRX-C1-M5', '2026-01-01', 100000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M5'),
    ('TRX-C1-M6', '2026-01-01', 100000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M6'),
    ('TRX-C1-M7', '2026-01-01',  60000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M7'),
    ('TRX-C1-M8', '2026-01-01',  90000.00, 'CASH', 'col-1', 'C1-A-CASH', NULL, NULL, 'C1-M8');

-- col-2
INSERT INTO transaction (id, creation_date, amount, payment_mode, collectivity_id, account_credited_cash_id, account_credited_bank_id, account_credited_mobile_id, member_id)
VALUES
    ('TRX-C2-M1', '2026-01-01',  60000.00, 'CASH',           'col-2', 'C2-A-CASH', NULL, NULL,            'C1-M1'),
    ('TRX-C2-M2', '2026-01-01',  90000.00, 'CASH',           'col-2', 'C2-A-CASH', NULL, NULL,            'C1-M2'),
    ('TRX-C2-M3', '2026-01-01', 100000.00, 'CASH',           'col-2', 'C2-A-CASH', NULL, NULL,            'C1-M3'),
    ('TRX-C2-M4', '2026-01-01', 100000.00, 'CASH',           'col-2', 'C2-A-CASH', NULL, NULL,            'C1-M4'),
    ('TRX-C2-M5', '2026-01-01', 100000.00, 'CASH',           'col-2', 'C2-A-CASH', NULL, NULL,            'C1-M5'),
    ('TRX-C2-M6', '2026-01-01', 100000.00, 'CASH',           'col-2', 'C2-A-CASH', NULL, NULL,            'C1-M6'),
    ('TRX-C2-M7', '2026-01-01',  40000.00, 'MOBILE_BANKING', 'col-2', NULL,        NULL, 'C2-A-MOBILE-1', 'C1-M7'),
    ('TRX-C2-M8', '2026-01-01',  60000.00, 'MOBILE_BANKING', 'col-2', NULL,        NULL, 'C2-A-MOBILE-1', 'C1-M8');

-- col-3: no transactions
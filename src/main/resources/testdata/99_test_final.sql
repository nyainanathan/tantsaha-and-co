-- Cash account col-1
INSERT INTO cash_account (id, collectivity_id, amount)
VALUES ('C1-A-CASH', 'col-1', 0);

-- Mobile banking account col-1
INSERT INTO mobile_banking_account (id, holder_name, service, mobile_number, collectivity_id, amount)
VALUES ('C1-A-MOBILE-1', 'Mpanorina', 'ORANGE_MONEY', '0370489612', 'col-1', 0);

-- Cash account col-2
INSERT INTO cash_account (id, collectivity_id, amount)
VALUES ('C2-A-CASH', 'col-2', 0);

-- Mobile banking account col-2
INSERT INTO mobile_banking_account (id, holder_name, service, mobile_number, collectivity_id, amount)
VALUES ('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', '0320489612', 'col-2', 0);

-- Cash account col-3 (ancien)
INSERT INTO cash_account (id, collectivity_id, amount)
VALUES ('C3-A-CASH', 'col-3', 0);

-- Bank account 1 col-3 (NOUVEAU)
INSERT INTO bank_account (id, holder_name, bank_name, bank_code, branch_code, account_number, key, collectivity_id, amount)
VALUES ('C3-A-BANK-1', 'Koto', 'BMOI', 00004, 00001, 1234567890, 12, 'col-3', 0);

-- Bank account 2 col-3 (NOUVEAU)
INSERT INTO bank_account (id, holder_name, bank_name, bank_code, branch_code, account_number, key, collectivity_id, amount)
VALUES ('C3-A-BANK-2', 'Naivo', 'BRED', 00008, 00003, 4567890123, 58, 'col-3', 0);

-- Mobile banking account col-3 (NOUVEAU)
INSERT INTO mobile_banking_account (id, holder_name, service, mobile_number, collectivity_id, amount)
VALUES ('C3-A-MOBILE-1', 'Kolo', 'MVOLA', '0341889612', 'col-3', 0);

delete from mobile_banking_account;
delete from bank_account;
delete from cash_account;

INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, collectivity_id)
VALUES 
  ('cot-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 200000, 'col-1'),
  ('cot-2', 'Famangiana',          'ACTIVE', 'PUNCTUALLY', '2026-04-30', 20000,  'col-1');

  INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, collectivity_id)
VALUES 
  ('cot-3', 'Cotisation annuelle', 'ACTIVE',   'ANNUALLY', '2026-01-01', 200000, 'col-2'),
  ('cot-4', 'Cotisation 2025',     'INACTIVE', 'ANNUALLY', '2025-01-01', 100000, 'col-2');

  INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, collectivity_id)
VALUES 
  ('cot-5', 'Cotisation mensuelle', 'ACTIVE', 'MONTHLY', '2026-04-01', 25000, 'col-3');

delete from membership_fee;


-- Member payments col-1
INSERT INTO member_payment (id, amount, creation_date, member_debited_id, membership_fee_id, payment_mode, financial_account_id)
VALUES
  ('pay-c1-1', 200000, '2026-01-01', 'C1-M1', 'cot-1', 'CASH',           'C1-A-CASH'),
  ('pay-c1-2', 200000, '2026-01-01', 'C1-M2', 'cot-1', 'CASH',           'C1-A-CASH'),
  ('pay-c1-3', 200000, '2026-01-01', 'C1-M3', 'cot-1', 'MOBILE_BANKING', 'C1-A-MOBILE-1'),
  ('pay-c1-4', 200000, '2026-01-01', 'C1-M4', 'cot-1', 'MOBILE_BANKING', 'C1-A-MOBILE-1'),
  ('pay-c1-5', 150000, '2026-01-01', 'C1-M5', 'cot-1', 'MOBILE_BANKING', 'C1-A-MOBILE-1'),
  ('pay-c1-6', 100000, '2026-05-01', 'C1-M6', 'cot-2', 'CASH',           'C1-A-CASH'),
  ('pay-c1-7',  60000, '2026-05-01', 'C1-M7', 'cot-2', 'CASH',           'C1-A-CASH'),
  ('pay-c1-8',  90000, '2026-05-01', 'C1-M8', 'cot-2', 'CASH',           'C1-A-CASH');

-- Transactions correspondantes col-1
INSERT INTO transaction (id, amount, creation_date, transaction_type, financial_account_id, member_debited_id)
VALUES
  ('tx-c1-1', 200000, '2026-01-01', 'IN', 'C1-A-CASH',     'C1-M1'),
  ('tx-c1-2', 200000, '2026-01-01', 'IN', 'C1-A-CASH',     'C1-M2'),
  ('tx-c1-3', 200000, '2026-01-01', 'IN', 'C1-A-MOBILE-1', 'C1-M3'),
  ('tx-c1-4', 200000, '2026-01-01', 'IN', 'C1-A-MOBILE-1', 'C1-M4'),
  ('tx-c1-5', 150000, '2026-01-01', 'IN', 'C1-A-MOBILE-1', 'C1-M5'),
  ('tx-c1-6', 100000, '2026-05-01', 'IN', 'C1-A-CASH',     'C1-M6'),
  ('tx-c1-7',  60000, '2026-05-01', 'IN', 'C1-A-CASH',     'C1-M7'),
  ('tx-c1-8',  90000, '2026-05-01', 'IN', 'C1-A-CASH',     'C1-M8');

  -- Member payments col-2
INSERT INTO member_payment (id, amount, creation_date, member_debited_id, membership_fee_id, payment_mode, financial_account_id)
VALUES
  ('pay-c2-1', 120000, '2026-01-01', 'C1-M1', 'cot-3', 'CASH',           'C2-A-CASH'),
  ('pay-c2-2', 180000, '2026-01-01', 'C1-M2', 'cot-3', 'CASH',           'C2-A-CASH'),
  ('pay-c2-3', 200000, '2026-01-01', 'C1-M3', 'cot-3', 'CASH',           'C2-A-CASH'),
  ('pay-c2-4', 200000, '2026-01-01', 'C1-M4', 'cot-3', 'CASH',           'C2-A-CASH'),
  ('pay-c2-5', 200000, '2026-01-01', 'C1-M5', 'cot-3', 'CASH',           'C2-A-CASH'),
  ('pay-c2-6', 200000, '2026-01-01', 'C1-M6', 'cot-3', 'CASH',           'C2-A-CASH'),
  ('pay-c2-7',  80000, '2026-01-01', 'C1-M7', 'cot-3', 'MOBILE_BANKING', 'C2-A-MOBILE-1'),
  ('pay-c2-8', 120000, '2026-01-01', 'C1-M8', 'cot-3', 'MOBILE_BANKING', 'C2-A-MOBILE-1');

-- Transactions correspondantes col-2
INSERT INTO transaction (id, amount, creation_date, transaction_type, financial_account_id, member_debited_id)
VALUES
  ('tx-c2-1', 120000, '2026-01-01', 'IN', 'C2-A-CASH',     'C1-M1'),
  ('tx-c2-2', 180000, '2026-01-01', 'IN', 'C2-A-CASH',     'C1-M2'),
  ('tx-c2-3', 200000, '2026-01-01', 'IN', 'C2-A-CASH',     'C1-M3'),
  ('tx-c2-4', 200000, '2026-01-01', 'IN', 'C2-A-CASH',     'C1-M4'),
  ('tx-c2-5', 200000, '2026-01-01', 'IN', 'C2-A-CASH',     'C1-M5'),
  ('tx-c2-6', 200000, '2026-01-01', 'IN', 'C2-A-CASH',     'C1-M6'),
  ('tx-c2-7',  80000, '2026-01-01', 'IN', 'C2-A-MOBILE-1', 'C1-M7'),
  ('tx-c2-8', 120000, '2026-01-01', 'IN', 'C2-A-MOBILE-1', 'C1-M8');

  -- Member payments col-3
INSERT INTO member_payment (id, amount, creation_date, member_debited_id, membership_fee_id, payment_mode, financial_account_id)
VALUES
  -- Avril 2026
  ('pay-c3-1',  25000, '2026-04-01', 'C3-M1', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-1'),
  ('pay-c3-2',  25000, '2026-04-01', 'C3-M2', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-1'),
  ('pay-c3-3',  25000, '2026-04-01', 'C3-M3', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-1'),
  ('pay-c3-4',  25000, '2026-04-01', 'C3-M4', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-1'),
  ('pay-c3-5',  25000, '2026-04-01', 'C3-M5', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-2'),
  ('pay-c3-6',  25000, '2026-04-01', 'C3-M6', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-2'),
  ('pay-c3-7',  25000, '2026-04-01', 'C3-M7', 'cot-5', 'CASH',          'C3-A-CASH'),
  ('pay-c3-8',  25000, '2026-04-01', 'C3-M8', 'cot-5', 'CASH',          'C3-A-CASH'),
  -- Mai 2026
  ('pay-c3-9',  25000, '2026-05-01', 'C3-M1', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-1'),
  ('pay-c3-10', 25000, '2026-05-01', 'C3-M2', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-1'),
  ('pay-c3-11', 15000, '2026-05-01', 'C3-M3', 'cot-5', 'MOBILE_BANKING','C3-A-MOBILE-1'),
  ('pay-c3-12', 15000, '2026-05-01', 'C3-M4', 'cot-5', 'MOBILE_BANKING','C3-A-MOBILE-1'),
  ('pay-c3-13', 20000, '2026-05-01', 'C3-M5', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-2'),
  ('pay-c3-14', 25000, '2026-05-01', 'C3-M6', 'cot-5', 'BANK_TRANSFER', 'C3-A-BANK-2'),
  ('pay-c3-15',  5000, '2026-05-01', 'C3-M7', 'cot-5', 'CASH',          'C3-A-CASH'),
  ('pay-c3-16',  5000, '2026-05-01', 'C3-M8', 'cot-5', 'CASH',          'C3-A-CASH');

-- Transactions correspondantes col-3
INSERT INTO transaction (id, amount, creation_date, transaction_type, financial_account_id, member_debited_id)
VALUES
  -- Avril 2026
  ('tx-c3-1',  25000, '2026-04-01', 'IN', 'C3-A-BANK-1',   'C3-M1'),
  ('tx-c3-2',  25000, '2026-04-01', 'IN', 'C3-A-BANK-1',   'C3-M2'),
  ('tx-c3-3',  25000, '2026-04-01', 'IN', 'C3-A-BANK-1',   'C3-M3'),
  ('tx-c3-4',  25000, '2026-04-01', 'IN', 'C3-A-BANK-1',   'C3-M4'),
  ('tx-c3-5',  25000, '2026-04-01', 'IN', 'C3-A-BANK-2',   'C3-M5'),
  ('tx-c3-6',  25000, '2026-04-01', 'IN', 'C3-A-BANK-2',   'C3-M6'),
  ('tx-c3-7',  25000, '2026-04-01', 'IN', 'C3-A-CASH',     'C3-M7'),
  ('tx-c3-8',  25000, '2026-04-01', 'IN', 'C3-A-CASH',     'C3-M8'),
  -- Mai 2026
  ('tx-c3-9',  25000, '2026-05-01', 'IN', 'C3-A-BANK-1',   'C3-M1'),
  ('tx-c3-10', 25000, '2026-05-01', 'IN', 'C3-A-BANK-1',   'C3-M2'),
  ('tx-c3-11', 15000, '2026-05-01', 'IN', 'C3-A-MOBILE-1', 'C3-M3'),
  ('tx-c3-12', 15000, '2026-05-01', 'IN', 'C3-A-MOBILE-1', 'C3-M4'),
  ('tx-c3-13', 20000, '2026-05-01', 'IN', 'C3-A-BANK-2',   'C3-M5'),
  ('tx-c3-14', 25000, '2026-05-01', 'IN', 'C3-A-BANK-2',   'C3-M6'),
  ('tx-c3-15',  5000, '2026-05-01', 'IN', 'C3-A-CASH',     'C3-M7'),
  ('tx-c3-16',  5000, '2026-05-01', 'IN', 'C3-A-CASH',     'C3-M8');

  UPDATE collectivity_member
SET adhesion_date = '2026-01-01'
WHERE member_id IN ('C1-M1','C1-M2','C1-M3','C1-M4','C1-M5','C1-M6','C1-M7','C1-M8',
                    'C2-M1','C2-M2','C2-M3','C2-M4','C2-M5','C2-M6','C2-M7','C2-M8',
                    'C3-M1','C3-M2','C3-M3','C3-M4','C3-M5','C3-M6','C3-M7','C3-M8');

-- Insertion des membres
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid)
VALUES
  ('C1-M9',  'Tojo',    'Rakoto',    '2000-03-15', 'MALE',   'Antananarivo', 'Etudiant', '0341234001', 'tojo.rakoto@mail.com',    'JUNIOR', false, false),
  ('C1-M10', 'Fara',    'Rasoa',     '2001-06-20', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234002', 'fara.rasoa@mail.com',     'JUNIOR', false, false),
  ('C1-M11', 'Hery',    'Andria',    '1999-11-10', 'MALE',   'Antananarivo', 'Etudiant', '0341234003', 'hery.andria@mail.com',    'JUNIOR', false, false),
  ('C1-M12', 'Voahira', 'Ranampy',   '2002-01-05', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234004', 'voahira.ranampy@mail.com','JUNIOR', false, false);

-- Insertion dans collectivity_member avec dates d'adhésion
INSERT INTO collectivity_member (id, member_id, collectivity_id, adhesion_date)
VALUES
  ('cm-c1-9',  'C1-M9',  'col-1', '2026-04-01'),
  ('cm-c1-10', 'C1-M10', 'col-1', '2026-04-01'),
  ('cm-c1-11', 'C1-M11', 'col-1', '2026-05-01'),
  ('cm-c1-12', 'C1-M12', 'col-1', '2026-06-01');

-- Membres référents (C1-M1 et C1-M2 pour tous)
INSERT INTO member_referee (id, member_refereed_id, member_referee_id)
VALUES
  ('ref-c1-9a',  'C1-M9',  'C1-M1'), ('ref-c1-9b',  'C1-M9',  'C1-M2'),
  ('ref-c1-10a', 'C1-M10', 'C1-M1'), ('ref-c1-10b', 'C1-M10', 'C1-M2'),
  ('ref-c1-11a', 'C1-M11', 'C1-M1'), ('ref-c1-11b', 'C1-M11', 'C1-M2'),
  ('ref-c1-12a', 'C1-M12', 'C1-M1'), ('ref-c1-12b', 'C1-M12', 'C1-M2');


INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid)
VALUES
  ('C2-M9',  'Nivo',   'Rasolofo',  '2000-07-12', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234005', 'nivo.rasolofo@mail.com',  'JUNIOR', false, false),
  ('C2-M10', 'Lalao',  'Rakotobe',  '2001-09-18', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234006', 'lalao.rakotobe@mail.com', 'JUNIOR', false, false),
  ('C2-M11', 'Mamy',   'Andrianja', '1998-04-25', 'MALE',   'Antananarivo', 'Etudiant', '0341234007', 'mamy.andrianja@mail.com', 'JUNIOR', false, false);

INSERT INTO collectivity_member (id, member_id, collectivity_id, adhesion_date)
VALUES
  ('cm-c2-9',  'C2-M9',  'col-2', '2026-03-01'),
  ('cm-c2-10', 'C2-M10', 'col-2', '2026-03-01'),
  ('cm-c2-11', 'C2-M11', 'col-2', '2026-03-01');

INSERT INTO member_referee (id, member_refereed_id, member_referee_id)
VALUES
  ('ref-c2-9a',  'C2-M9',  'C1-M1'), ('ref-c2-9b',  'C2-M9',  'C1-M2'),
  ('ref-c2-10a', 'C2-M10', 'C1-M1'), ('ref-c2-10b', 'C2-M10', 'C1-M2'),
  ('ref-c2-11a', 'C2-M11', 'C1-M1'), ('ref-c2-11b', 'C2-M11', 'C1-M2');


  INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid)
VALUES
  ('C3-M9',  'Zo',      'Rakoto',    '2001-01-10', 'MALE',   'Antananarivo', 'Etudiant', '0341234008', 'zo.rakoto@mail.com',      'JUNIOR', false, false),
  ('C3-M10', 'Hasina',  'Rafarabe',  '2000-02-14', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234009', 'hasina.rafarabe@mail.com','JUNIOR', false, false),
  ('C3-M11', 'Tiana',   'Andriampy', '1999-02-28', 'MALE',   'Antananarivo', 'Etudiant', '0341234010', 'tiana.andriampy@mail.com','JUNIOR', false, false),
  ('C3-M12', 'Miora',   'Rasoavin',  '2002-03-05', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234011', 'miora.rasoavin@mail.com', 'JUNIOR', false, false),
  ('C3-M13', 'Rado',    'Rakotobe',  '2001-03-19', 'MALE',   'Antananarivo', 'Etudiant', '0341234012', 'rado.rakotobe@mail.com',  'JUNIOR', false, false),
  ('C3-M14', 'Saholy',  'Andriafy',  '2000-03-22', 'FEMALE', 'Antananarivo', 'Etudiant', '0341234013', 'saholy.andriafy@mail.com','JUNIOR', false, false);

INSERT INTO collectivity_member (id, member_id, collectivity_id, adhesion_date)
VALUES
  ('cm-c3-9',  'C3-M9',  'col-3', '2026-01-01'),
  ('cm-c3-10', 'C3-M10', 'col-3', '2026-02-01'),
  ('cm-c3-11', 'C3-M11', 'col-3', '2026-02-01'),
  ('cm-c3-12', 'C3-M12', 'col-3', '2026-03-01'),
  ('cm-c3-13', 'C3-M13', 'col-3', '2026-03-01'),
  ('cm-c3-14', 'C3-M14', 'col-3', '2026-03-01');

INSERT INTO member_referee (id, member_refereed_id, member_referee_id)
VALUES
  ('ref-c3-9a',  'C3-M9',  'C3-M1'), ('ref-c3-9b',  'C3-M9',  'C3-M2'),
  ('ref-c3-10a', 'C3-M10', 'C3-M1'), ('ref-c3-10b', 'C3-M10', 'C3-M2'),
  ('ref-c3-11a', 'C3-M11', 'C3-M1'), ('ref-c3-11b', 'C3-M11', 'C3-M2'),
  ('ref-c3-12a', 'C3-M12', 'C3-M1'), ('ref-c3-12b', 'C3-M12', 'C3-M2'),
  ('ref-c3-13a', 'C3-M13', 'C3-M1'), ('ref-c3-13b', 'C3-M13', 'C3-M2'),
  ('ref-c3-14a', 'C3-M14', 'C3-M1'), ('ref-c3-14b', 'C3-M14', 'C3-M2');
-- Collectivity activities
INSERT INTO collectivity_activity (id, label, activity_type, recurrence_week, recurrence_day, executive_date, collectivity_id)
VALUES
  -- col-1 : assemblée générale mensuelle (2ème dimanche du mois)
  ('act-c1-1', 'Assemblée générale janvier',  'MEETING',  2, 'SU', '2026-01-11', 'col-1'),
  ('act-c1-2', 'Assemblée générale février',  'MEETING',  2, 'SU', '2026-02-08', 'col-1'),
  ('act-c1-3', 'Formation juniors janvier',   'TRAINING', 4, 'SA', '2026-01-24', 'col-1'),
  ('act-c1-4', 'Formation juniors février',   'TRAINING', 4, 'SA', '2026-02-28', 'col-1'),
  ('act-c1-5', 'Activité exceptionnelle',     'OTHER',    null, null, '2026-03-15', 'col-1'),

  -- col-2
  ('act-c2-1', 'Assemblée générale janvier',  'MEETING',  2, 'SU', '2026-01-11', 'col-2'),
  ('act-c2-2', 'Formation juniors janvier',   'TRAINING', 4, 'SA', '2026-01-24', 'col-2'),

  -- col-3
  ('act-c3-1', 'Assemblée générale janvier',  'MEETING',  2, 'SU', '2026-01-11', 'col-3'),
  ('act-c3-2', 'Formation juniors janvier',   'TRAINING', 4, 'SA', '2026-01-24', 'col-3');

-- Activity occupations (qui est concerné par chaque activité)
INSERT INTO activity_occupation (id, activity_id, occupation)
VALUES
  -- Assemblées générales -> tous les membres
  ('ao-1',  'act-c1-1', 'JUNIOR'),
  ('ao-2',  'act-c1-1', 'SENIOR'),
  ('ao-3',  'act-c1-1', 'SECRETARY'),
  ('ao-4',  'act-c1-1', 'TREASURER'),
  ('ao-5',  'act-c1-1', 'VICE_PRESIDENT'),
  ('ao-6',  'act-c1-1', 'PRESIDENT'),

  -- Formation -> juniors uniquement
  ('ao-7',  'act-c1-3', 'JUNIOR'),
  ('ao-8',  'act-c2-2', 'JUNIOR'),
  ('ao-9',  'act-c3-2', 'JUNIOR'),

  -- Activité exceptionnelle col-1 -> tous
  ('ao-10', 'act-c1-5', 'JUNIOR'),
  ('ao-11', 'act-c1-5', 'SENIOR'),
  ('ao-12', 'act-c1-5', 'SECRETARY'),
  ('ao-13', 'act-c1-5', 'TREASURER'),
  ('ao-14', 'act-c1-5', 'VICE_PRESIDENT'),
  ('ao-15', 'act-c1-5', 'PRESIDENT');

-- Activity attendance
INSERT INTO activity_attendance (id, member_id, activity_id, status)
VALUES
  -- Assemblée générale col-1 janvier
  ('att-1',  'C1-M1', 'act-c1-1', 'ATTENDED'),
  ('att-2',  'C1-M2', 'act-c1-1', 'ATTENDED'),
  ('att-3',  'C1-M3', 'act-c1-1', 'ATTENDED'),
  ('att-4',  'C1-M4', 'act-c1-1', 'ATTENDED'),
  ('att-5',  'C1-M5', 'act-c1-1', 'MISSING'),
  ('att-6',  'C1-M6', 'act-c1-1', 'ATTENDED'),
  ('att-7',  'C1-M7', 'act-c1-1', 'MISSING'),
  ('att-8',  'C1-M8', 'act-c1-1', 'ATTENDED'),

  -- Formation juniors col-1 janvier
  ('att-9',  'C1-M9',  'act-c1-3', 'ATTENDED'),
  ('att-10', 'C1-M10', 'act-c1-3', 'MISSING'),

  -- Assemblée générale col-2 janvier
  ('att-11', 'C1-M1', 'act-c2-1', 'ATTENDED'),
  ('att-12', 'C1-M2', 'act-c2-1', 'MISSING'),
  ('att-13', 'C1-M3', 'act-c2-1', 'ATTENDED'),
  ('att-14', 'C1-M4', 'act-c2-1', 'ATTENDED'),
  ('att-15', 'C1-M5', 'act-c2-1', 'ATTENDED'),
  ('att-16', 'C1-M6', 'act-c2-1', 'MISSING'),
  ('att-17', 'C1-M7', 'act-c2-1', 'ATTENDED'),
  ('att-18', 'C1-M8', 'act-c2-1', 'ATTENDED'),

  -- Assemblée générale col-3 janvier
  ('att-19', 'C3-M1', 'act-c3-1', 'ATTENDED'),
  ('att-20', 'C3-M2', 'act-c3-1', 'ATTENDED'),
  ('att-21', 'C3-M3', 'act-c3-1', 'MISSING'),
  ('att-22', 'C3-M4', 'act-c3-1', 'ATTENDED'),
  ('att-23', 'C3-M5', 'act-c3-1', 'ATTENDED'),
  ('att-24', 'C3-M6', 'act-c3-1', 'MISSING'),
  ('att-25', 'C3-M7', 'act-c3-1', 'ATTENDED'),
  ('att-26', 'C3-M8', 'act-c3-1', 'ATTENDED');



  -- Dans l'ordre pour respecter les FK
DELETE FROM activity_attendance;
DELETE FROM activity_occupation;
DELETE FROM collectivity_activity;
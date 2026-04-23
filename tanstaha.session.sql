select * from collectivity;

select * from member;

delete from member where id like '68f1e8a8-f3c5-448e-bbfd-4c4f768c65bc';

select * from role_attribution;

delete from role_attribution where id > 8

        SELECT id, location, name, number
        FROM collectivity
        WHERE id = 'COL-001'
create table if not exists cash_account
(
    id              varchar primary key,
    collectivity_id varchar references collectivity (id)
);

alter table cash_account add column amount numeric(10,2);
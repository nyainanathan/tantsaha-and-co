alter table transaction add id_member references member(id);
alter table transaction add id_fee references memberhip_fee(id);
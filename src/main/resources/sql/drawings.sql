-- We need some prize tiers to fill at least 1 pool
insert into prize_tiers(id) values(1);
insert into prize_tiers(id) values(2);
insert into prize_tiers(id) values(3);
insert into prize_tiers(id) values(4);
insert into prize_tiers(id) values(5);
insert into prize_tiers(id) values(6);
insert into prize_tiers(id) values(7);
insert into prize_tiers(id) values(8);
insert into prize_tiers(id) values(9);
insert into prize_tiers(id) values(10);

-- And at least 1 pool to put in a drawing
insert into prize_pools(id, tier1, tier2, tier3, tier4, tier5, tier6, tier7, tier8, tier9, tier10)
values(1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

insert into drawings (scheduled, small_pool) values(1393113600, 1);

insert into entries(id, amount, entered_date) values(1, 5, 1393027200);
insert into drawing_entries (drawing_id, entry_id) values (1, 1);
insert into tier_entries (tier_id, entry_id) values (1, 1);
insert into user_entries (user_id, entry_id) values (1, 1);
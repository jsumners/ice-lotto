-- One sql file to load for testing the whole damn thing --
-----------------------------------------------------------

-- First, we need some user roles
insert into roles (id, name, description) values (1, "admin", "Application Administrator");
insert into roles (id, name, description) values (2, "user", "Basic account");

-- Second, some users
insert into users (id, gw2display_name, enabled) values (1, "Morhyn.8032", 1);
insert into user_roles (user_id, role_id) values (1, 1);
insert into characters (id, name) values (1, "Semaj Srenmus");
insert into characters (id, name) values (2, "Krait Hate");
insert into characters (id, name) values (3, "Poncy Jigglebottom");
insert into user_characters (user_id, character_id) values (1, 1);
insert into user_characters (user_id, character_id) values (1, 2);
insert into user_characters (user_id, character_id) values (1, 3);

insert into users (id, gw2display_name, enabled) values (2, "Sanctum.7938", 1);
insert into user_roles (user_id, role_id) values (2, 1);
insert into characters (id, name) values (4, "Sanctum Chrae");
insert into user_characters (user_id, character_id) values (2, 4);

-- Just going to start from the top of http://ice-gw2.proboards.com/thread/316/name
insert into users (id, gw2display_name, enabled) values (3, "Mia Wynd.2367", 0);
insert into user_roles (user_id, role_id) values (3, 2);
insert into characters (id, name) values (5, "Mia Wynd");
insert into characters (id, name) values (6, "Bella Wynd");
insert into characters (id, name) values (7, "Randi Wynd");
insert into user_characters (user_id, character_id) values (3, 5);
insert into user_characters (user_id, character_id) values (3, 6);
insert into user_characters (user_id, character_id) values (3, 7);

insert into users (id, gw2display_name, enabled) values (4, "vale.5139", 0);
insert into user_roles (user_id, role_id) values (4, 2);
insert into characters (id, name) values (8, "Nar");
insert into characters (id, name) values (9, "Swanhilld");
insert into characters (id, name) values (10, "Baçon");
insert into user_characters (user_id, character_id) values (4, 8);
insert into user_characters (user_id, character_id) values (4, 9);
insert into user_characters (user_id, character_id) values (4, 10);

insert into users (id, gw2display_name, enabled) values (5, "Una Vida.6189", 0);
insert into user_roles (user_id, role_id) values (5, 2);
insert into characters (id, name) values (11, "Una Peluda");
insert into characters (id, name) values (12, "Una Vida");
insert into characters (id, name) values (13, "Una Vita");
insert into characters (id, name) values (14, "Una Muerta");
insert into user_characters (user_id, character_id) values (5, 11);
insert into user_characters (user_id, character_id) values (5, 12);
insert into user_characters (user_id, character_id) values (5, 13);
insert into user_characters (user_id, character_id) values (5, 14);

insert into users (id, gw2display_name, enabled) values (6, "Sergio Pino.6308", 0);
insert into user_roles (user_id, role_id) values (6, 2);
insert into characters (id, name) values (15, "Furtiveness");
insert into characters (id, name) values (16, "Ember Legionnaire");
insert into characters (id, name) values (17, "Ragehorn Stonehoof");
insert into user_characters (user_id, character_id) values (6, 15);
insert into user_characters (user_id, character_id) values (6, 16);
insert into user_characters (user_id, character_id) values (6, 17);

insert into users (id, gw2display_name, enabled) values (7, "TakuNue.6104", 0);
insert into user_roles (user_id, role_id) values (7, 2);
insert into characters (id, name) values (18, "Takunue");
insert into characters (id, name) values (19, "Eiskremnue");
insert into characters (id, name) values (20, "Kankanue");
insert into characters (id, name) values (21, "Kyorannue");
insert into characters (id, name) values (22, "Karum Nue");
insert into user_characters (user_id, character_id) values (7, 18);
insert into user_characters (user_id, character_id) values (7, 19);
insert into user_characters (user_id, character_id) values (7, 20);
insert into user_characters (user_id, character_id) values (7, 21);
insert into user_characters (user_id, character_id) values (7, 22);

insert into users (id, gw2display_name, enabled) values (8, "EleanorVesper.1253", 0);
insert into user_roles (user_id, role_id) values (8, 2);
insert into characters (id, name) values (23, "Lady Manae");
insert into characters (id, name) values (24, "Loki of Asgard");
insert into characters (id, name) values (25, "Lady Liam");
insert into characters (id, name) values (26, "Lady Basil");
insert into user_characters (user_id, character_id) values (8, 23);
insert into user_characters (user_id, character_id) values (8, 24);
insert into user_characters (user_id, character_id) values (8, 25);
insert into user_characters (user_id, character_id) values (8, 26);

insert into users (id, gw2display_name, enabled) values (9, "stonedragon.8412", 0);
insert into user_roles (user_id, role_id) values (9, 2);
insert into characters (id, name) values (27, "Robyn Wynd");
insert into characters (id, name) values (28, "Rindi Wynd");
insert into characters (id, name) values (29, "Mal Wynd");
insert into characters (id, name) values (30, "Glacial Wynd");
insert into user_characters (user_id, character_id) values (9, 27);
insert into user_characters (user_id, character_id) values (9, 28);
insert into user_characters (user_id, character_id) values (9, 29);
insert into user_characters (user_id, character_id) values (9, 30);

-- Third, we need some game items
insert into game_items (id, name, description, min_level, rarity)
values (2952, "Acolyte Coat", "A lovely coat", 1, "Common");

-- Fourth, we need some prize items
insert into prize_items (id, game_item, count) values (1, 2952, 1);
insert into prize_items (id, game_item, count) values (2, 2952, 1);
insert into prize_items (id, game_item, count) values (3, 2952, 1);
insert into prize_items (id, game_item, count) values (4, 2952, 1);
insert into prize_items (id, game_item, count) values (5, 2952, 1);
insert into prize_items (id, game_item, count) values (6, 2952, 1);
insert into prize_items (id, game_item, count) values (7, 2952, 1);
insert into prize_items (id, game_item, count) values (8, 2952, 1);
insert into prize_items (id, game_item, count) values (9, 2952, 1);
insert into prize_items (id, game_item, count) values (10, 2952, 1);
insert into prize_items (id, game_item, count) values (11, 2952, 1);
insert into prize_items (id, game_item, count) values (12, 2952, 1);
insert into prize_items (id, game_item, count) values (13, 2952, 1);
insert into prize_items (id, game_item, count) values (14, 2952, 1);
insert into prize_items (id, game_item, count) values (15, 2952, 1);
insert into prize_items (id, game_item, count) values (16, 2952, 1);
insert into prize_items (id, game_item, count) values (17, 2952, 1);
insert into prize_items (id, game_item, count) values (18, 2952, 1);
insert into prize_items (id, game_item, count) values (19, 2952, 1);
insert into prize_items (id, game_item, count) values (20, 2952, 1);
insert into prize_items (id, game_item, count) values (21, 2952, 1);
insert into prize_items (id, game_item, count) values (22, 2952, 1);
insert into prize_items (id, game_item, count) values (23, 2952, 1);
insert into prize_items (id, game_item, count) values (24, 2952, 1);
insert into prize_items (id, game_item, count) values (25, 2952, 1);
insert into prize_items (id, game_item, count) values (26, 2952, 1);
insert into prize_items (id, game_item, count) values (27, 2952, 1);
insert into prize_items (id, game_item, count) values (28, 2952, 1);
insert into prize_items (id, game_item, count) values (29, 2952, 1);
insert into prize_items (id, game_item, count) values (30, 2952, 1);

-- Fifth, we need prize tiers
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
insert into prize_tiers(id) values(11);
insert into prize_tiers(id) values(12);
insert into prize_tiers(id) values(13);
insert into prize_tiers(id) values(14);
insert into prize_tiers(id) values(15);
insert into prize_tiers(id) values(16);
insert into prize_tiers(id) values(17);
insert into prize_tiers(id) values(18);
insert into prize_tiers(id) values(19);
insert into prize_tiers(id) values(20);

-- Sixth, we need prize pools
insert into prize_pools(id, tier1, tier2, tier3, tier4, tier5, tier6, tier7, tier8, tier9, tier10)
values(1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
insert into prize_pools(id, tier1, tier2, tier3, tier4, tier5, tier6, tier7, tier8, tier9, tier10)
values(2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

-- Seventh, we need to items in the tiers
-- TODO: add more prize items and put them in more tiers
update prize_tiers set item1=1, item2=2, item3=3, item4=4, item5=5,
  item6=6, item7=7, item8=8, item9=9, item10=10 where id = 1;
update prize_tiers set item1=11, item2=12, item3=13, item4=14, item5=15,
  item6=16, item7=17, item8=18, item9=19, item10=20 where id = 2;
update prize_tiers set item1=21, item2=22, item3=23, item4=24, item5=25,
  item6=26, item7=27, item8=28, item9=29, item10=30 where id = 3;

-- Eigth, we need an acutal drawing
-- Note: the date functions used here are SQLite specific (they get the next saturday)
insert into drawings (scheduled, small_pool, large_pool, in_progress)
values(
  (select strftime('%s', datetime('now', 'Weekday 6'))),
  1,
  2,
  0
);

-- Ninth, we need some entries
insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  1,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  1
);

insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  2,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  1
);

insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  3,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  2
);

insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  4,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  3
);

insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  5,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  4
);

insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  6,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  5
);

insert into entries (id, amount, entered_date, drawing, prize_tier, user)
values (
  7,
  1,
  (select strftime('%s', datetime('now'))),
  1,
  1,
  5
);
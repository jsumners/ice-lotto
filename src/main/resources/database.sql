-- This table should only ever contain one row. It will be used
-- to determine which scripts are necessary to perform a database upgrade.
create table db_info (
  version integer not null default(1),
  initial_version integer not null default(1)
);
-- This will be the only data insertion in this script. This statement
-- can be used to determine which version of the database this script will
-- create.
insert into db_info (version, initial_version) values (1, 1);

create table users (
  id integer primary key,
  gw2display_name text not null,
  display_name text,
  email text,
  password text,
  time_zone text,
  datetime_format text,
  claim_key text,
  claimed integer not null default(0),
  enabled integer not null default(0)
);

create table roles (
  id integer primary key,
  name text unique,
  description text
);

create table user_roles (
  user_id integer,
  role_id integer,
  constraint user_roles_pk primary key (user_id, role_id),
  foreign key (user_id) references users (id),
  foreign key (role_id) references roles (id)
);

create table characters (
  id integer primary key,
  name text not null
);

create table user_characters (
  user_id integer not null,
  character_id integer not null,
  constraint user_characters_pk primary key (user_id, character_id),
  foreign key (user_id) references users (id),
  foreign key (character_id) references characters (id)
);

create table game_items (
  id integer, -- The actual Guild Wars 2 item id, not an autoincrement id
  description text,
  image_url text,
  min_level integer,
  name text not null,
  rarity integer,
  constraint game_items_pk primary key (id, name)
);

create table prize_items (
  id integer primary key,
  count integer not null default(1),
  game_item integer not null,
  foreign key (game_item) references game_items (id)
);

create table prize_tiers (
  id integer primary key,
  item1 integer,
  item2 integer,
  item3 integer,
  item4 integer,
  item5 integer,
  item6 integer,
  item7 integer,
  item8 integer,
  item9 integer,
  item10 integer,
  drawn integer not null default(0),
  draw_result integer,
  foreign key (item1) references prize_items (id),
  foreign key (item2) references prize_items (id),
  foreign key (item3) references prize_items (id),
  foreign key (item4) references prize_items (id),
  foreign key (item5) references prize_items (id),
  foreign key (item6) references prize_items (id),
  foreign key (item7) references prize_items (id),
  foreign key (item8) references prize_items (id),
  foreign key (item9) references prize_items (id),
  foreign key (item10) references prize_items (id)
);

create table prize_pools (
  id integer primary key,
  tier1 integer,
  tier2 integer,
  tier3 integer,
  tier4 integer,
  tier5 integer,
  tier6 integer,
  tier7 integer,
  tier8 integer,
  tier9 integer,
  tier10 integer,
  drawn integer not null default(0),
  draw_result integer, -- The id of a money_draw_results record
  foreign key (tier1) references prize_tiers (id),
  foreign key (tier2) references prize_tiers (id),
  foreign key (tier3) references prize_tiers (id),
  foreign key (tier4) references prize_tiers (id),
  foreign key (tier5) references prize_tiers (id),
  foreign key (tier6) references prize_tiers (id),
  foreign key (tier7) references prize_tiers (id),
  foreign key (tier8) references prize_tiers (id),
  foreign key (tier9) references prize_tiers (id),
  foreign key (tier10) references prize_tiers (id),
  foreign key (draw_result) references money_draw_results (id)
);

create table drawings (
  -- The times are Unix timestamps (i.e. seconds since 00:00 1970/01/01)
  id integer primary key,
  small_pool integer,
  large_pool integer,
  scheduled integer, -- time the drawing is/was scheduled to start
  started integer, -- time the drawing started
  ended integer, -- time the drawing ended
  in_progress integer not null default(0),
  duplicated integer not null default(0),
  foreign key (small_pool) references prize_pools (id),
  foreign key (large_pool) references prize_pools (id)
);

create table entries (
  -- Records an entry into a drawing in terms of the amount
  -- of gold that was used to enter into the drawing.
  id integer primary key,
  amount integer not null,
  entered_date integer
);

create table drawing_entries (
  -- Ties an entry to its parent drawing.
  drawing_id integer,
  entry_id integer,
  constraint drawing_entries_pk primary key (drawing_id, entry_id),
  foreign key (drawing_id) references drawings (id),
  foreign key (entry_id) references entries (id)
);

create table tier_entries (
  -- Ties an entry to the prize tier it is for.
  tier_id integer,
  entry_id integer,
  constraint tier_entries_pk primary key (tier_id, entry_id),
  foreign key (tier_id) references prize_tiers (id),
  foreign key (entry_id) references entries (id)
);

create table user_entries (
  -- Ties an entry to the user it is for.
  user_id integer,
  entry_id integer,
  constraint user_entries_pk primary key (user_id, entry_id),
  foreign key (user_id) references users (id),
  foreign key (entry_id) references entries (id)
);

create table shuffled_tier_entries (
  -- Records the order in which tier entries were shuffled.
  id integer primary key,
  position integer not null,
  entry_id integer not null,
  tier_id integer not null,
  foreign key (entry_id) references entries (id),
  foreign key (tier_id) references prize_tiers (id)
);

create table shuffled_pool_entries (
  -- Records the order in which pool (money) entries were shuffled.
  id integer primary key,
  position integer not null,
  entry_id integer not null,
  pool_id integer not null,
  foreign key (entry_id) references entries (id),
  foreign key (pool_id) references prize_pools (id)
);

create table prize_draw_results (
  -- Records the drawing results from the shuffled_tier_entries.
  id integer primary key,
  awarded integer not null, -- Time it was won (Unix timestamp)
  item_draw_number integer not null, -- The random number used to pick the won item
  user_draw_number integer not null -- The random number used to pick the winner
);

create table drawn_prize_items (
  -- Ties a prize_draw_results record to a prize item.
  prize_item_id integer,
  prize_draw_result_id integer,
  constraint drawn_prize_items_pk primary key (prize_item_id, prize_draw_result_id),
  foreign key (prize_item_id) references prize_items (id),
  foreign key (prize_draw_result_id) references prize_draw_results (id)
);

create table drawn_prize_tiers (
  -- Ties a prize_draw_results record to a prize tier.
  prize_tier_id integer,
  prize_draw_result_id integer,
  constraint drawn_prize_tiers_pk primary key (prize_tier_id, prize_draw_result_id),
  foreign key (prize_tier_id) references prize_tiers (id),
  foreign key (prize_draw_result_id) references prize_draw_results (id)
);

create table drawn_prize_winners (
  -- Ties prize_draw_results records to users (winners)
  winner_id integer,
  prize_draw_result_id integer,
  constraint drawn_prize_winners_pk primary key (winner_id, prize_draw_result_id),
  foreign key (winner_id) references users (id),
  foreign key (prize_draw_result_id) references prize_draw_results (id)
);

create table money_draw_results (
  -- Records the drawing results from the shuffled_pool_entries.
  id integer primary key,
  awarded integer not null, -- Time it was won (Unix timestamp)
  amount_won integer not null,
  draw_number integer not null -- The random number used to pick the winner
);

create table money_draw_drawings (
  -- Ties money_draw_results records to drawings.
  drawing_id integer,
  money_draw_result_id integer,
  constraint money_draw_drawings_pk primary key (drawing_id, money_draw_result_id),
  foreign key (drawing_id) references drawings (id),
  foreign key (money_draw_result_id) references money_draw_results (id)
);

create table money_draw_pools (
  -- Ties money_draw_results records to prize_pools records.
  pool_id integer,
  money_draw_result_id integer,
  constraint money_draw_pools_pk primary key (pool_id, money_draw_result_id),
  foreign key (pool_id) references prize_pools (id),
  foreign key (money_draw_result_id) references money_draw_results (id)
);

create table money_draw_winners (
  -- Ties money_draw_results records to users (winners).
  winner_id integer,
  money_draw_result_id integer,
  constraint money_draw_winners_pk primary key (winner_id, money_draw_result_id),
  foreign key (winner_id) references users (id),
  foreign key (money_draw_result_id) references money_draw_results (id)
);
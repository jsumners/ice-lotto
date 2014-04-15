insert into users (id, gw2display_name, enabled, password, claim_key)
  values(1, "Morhyn.8032", 0, "$2a$13$6SaY/IFkGK8YBb6eC40SYe4CwPCkOc6GKvgnW0YNoo7QMbnbvPSFK", "aeb98b1ef97be944");
insert into roles (id, name, description) values(1, "admin", "Application Administrator");
insert into user_roles(user_id, role_id) values(1, 1);

insert into characters (name) values ("Semaj Srenmus");
insert into characters (name) values ("Krait Hate");
insert into characters (name) values ("Poncy Jigglebottom");

insert into user_characters (user_id, character_id) values(1, 1);
insert into user_characters (user_id, character_id) values(1, 2);
insert into user_characters (user_id, character_id) values(1, 3);

insert into users (id, gw2display_name, enabled, password, claim_key)
values(2, "Sanctum.7938", 0, "$2a$13$6SaY/IFkGK8YBb6eC40SYe4CwPCkOc6GKvgnW0YNoo7QMbnbvPSFK", "71eb3a535454f205");
insert into user_roles (user_id, role_id) values(2, 1);
insert into characters (name) values("Sanctum Chrae");
insert into user_characters (user_id, character_id) values(2, 4);
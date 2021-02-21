
-- note this is using FORMAT JSON for H2 db, this might not work for other databases

insert into configuration (key, value) values ('hjug', ' { "what": "tech talk", "when":224 } ' FORMAT JSON);
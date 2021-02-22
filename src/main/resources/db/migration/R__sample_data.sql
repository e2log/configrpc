
-- note this is using FORMAT JSON for H2 db, this might not work for other databases

insert into configuration (key, value) values
('/service/data', ' {
  "environments" : [
    {"environment": "TEST", "database": {"host": "10.0.0.79", "user": "sa", "password_key": "test-db-pwd"}, "admin": "sam@svc.com"},
    {"environment": "STAGING", "database": {"host": "10.1.0.237", "user": "sa", "password_key": "staging-db-pwd"}, "admin": "ana@svc.com"}
  ]
} ' FORMAT JSON);
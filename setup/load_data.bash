#!/bin/bash
cat create_postgres.sql > tmpfile
sqlite3 new_courses_500.db .dump | grep 'INSERT' >> tmpfile
sql < tmpfile
rm tmpfile

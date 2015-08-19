#!/bin/bash
DATABASE="courses.db"
CREATEPOSTGRES="sql/create_postgres.sql"
export DATABASE

dump_filter()
{
	#Dump the sqlite table, take only the insert statements (i.e., not the CREATE TABLE statements),
	#and then remove the quotes from around the table names
	sqlite3 $DATABASE ".dump $1" | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/'
}

get_tables()
{
	#Use the create table statements for postgres to get the table names. This whole script assumes that
	#the sqlite and postgres tables have the same names, so could use either the create script or a
	#.dump of the database, so use the script for efficiency's sake
	grep 'CREATE TABLE' $CREATEPOSTGRES | sed 's/CREATE TABLE \([a-zA-Z_]*\)/\1/'
}

export -f dump_filter
export -f get_tables

if [[ $# -ne 3 ]] ; then
	echo Please include hostname, username, and database name to which to connect.
else
	cat $CREATEPOSTGRES <(get_tables | xargs -n 1 -i bash -c 'dump_filter "$@"' _ {}) | psql -h $1 -U $2 $3 > /dev/null
fi

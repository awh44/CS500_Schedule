#!/bin/bash
DATABASE="courses.db"
TMPFILE="tmpfile"
CREATEPOSTGRES="sql/create_postgres.sql"

dump_filter()
{
	#Dump the sqlite table, take only the insert statements (i.e., not the CREATE TABLE statements),
	#and then remove the quotes from around the table names
	sqlite3 $DATABASE ".dump $1" | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/'
}

dump_filter_convert()
{
	#The sqlite database currently uses integers in some places it should be using text, so some
	#leading zeros have been removed. Look for the one and two leading zero cases and fix them
	dump_filter "$1" | sed 's/,\([0-9][0-9]\),/,'\''0\1'\'',/' | sed 's/,\([0-9]\),/,'\''00\1'\'',/'
}

dump_filter_write()
{
	dump_filter "$1" >> $TMPFILE
}

dump_filter_convert_write()
{
	dump_filter_convert "$1" >> $TMPFILE
}

if [[ $# -ne 3 ]] ; then
	echo Please include hostname, username, and database name to which to connect.
else
	cat $CREATEPOSTGRES > $TMPFILE
	dump_filter_write Subjects
	dump_filter_convert_write Courses_Have
	dump_filter_write Terms
	dump_filter_convert_write Course_Offered_In_Term
	dump_filter_write Instructors
	dump_filter_write Campuses
	dump_filter_write Sections
	dump_filter_write TimeBlocks
	dump_filter_write Meets_At
	psql -h $1 -U $2 $3 < $TMPFILE > /dev/null
	rm $TMPFILE
fi

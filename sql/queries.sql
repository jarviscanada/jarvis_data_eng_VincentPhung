-- Creating tables
CREATE TABLE cd.members
    (
       memid integer NOT NULL,
       surname character varying(200) NOT NULL,
       firstname character varying(200) NOT NULL,
       address character varying(300) NOT NULL,
       zipcode integer NOT NULL,
       telephone character varying(20) NOT NULL,
       recommendedby integer,
       joindate timestamp NOT NULL,
       CONSTRAINT members_pk PRIMARY KEY (memid),
       CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
            REFERENCES cd.members(memid) ON DELETE SET NULL
    );

 CREATE TABLE cd.facilities
    (
       facid integer NOT NULL,
       name character varying(100) NOT NULL,
       membercost numeric NOT NULL,
       guestcost numeric NOT NULL,
       initialoutlay numeric NOT NULL,
       monthlymaintenance numeric NOT NULL,
       CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );

CREATE TABLE cd.bookings
    (
       bookid integer NOT NULL,
       facid integer NOT NULL,
       memid integer NOT NULL,
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       CONSTRAINT bookings_pk PRIMARY KEY (bookid),
       CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
       CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );

--1. Insert some data into a table
insert into cd.facilities
	(facid,name,membercost,guestcost,initialoutlay,monthlymaintenance)
	values (9, 'Spa', 20, 30, 100000, 800) ;

--2. Insert calculated data into a table
insert into cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
		SELECT (select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

--3. Update some existing data
update cd.facilities
    set initialoutlay = 10000
    where facid = 1;

--4. Update a row based on the contents of another row
update cd.facilities facs
	set
		membercost = (select membercost * 1.1 from cd.facilities where facid = 0),
		guestcost = (select guestcost * 1.1 from cd.facilities where facid = 1)
where facs.facid = 1;

--5. Delete all bookings
Delete all bookings

--6. Delete a member from the cd.members table
Delete from cd.members
where cd.members.memid = 37

--7. Control which rows are retrieved - part 2
select facid, name, membercost, monthlymaintenance
from cd.facilities
where membercost > 0 and (membercost < monthlymaintenance/50.0);

--8. Basic string searches
select *
from cd.facilities
where
	name like '%Tennis%';

--9. Matching against multiple possible values
select * from cd.facilities
where facid in (1,5);

--10. Working with dates
select memid,surname,firstname, joindate
from cd.members
where joindate >= '2012-09-01';

--11. Combining results from multiple queries
select surname from cd.members
union
select name from cd.facilities;

--12. Retrieve the start times of members' bookings
select bks.starttime
	from cd.bookings bks
	INNER JOIN cd.members mems
		on mems.memid = bks.memid
	where mems.firstname = 'David' and
		  mems.surname = 'Farrell';

--13. Work out the start times of bookings for tennis courts
select bks.starttime as start, facs.name as name
	from cd.facilities facs
		INNER JOIN
		 cd.bookings bks
		 on facs.facid = bks.facid
	where
		facs.name in ('Tennis Court 2', 'Tennis Court 1') and
		bks.starttime >= '2012-09-21' and
		bks.starttime < '2012-09-22'
order by bks.starttime;

--14. Produce a list of all members, along with their recommender
select mems.firstname as memfname, mems.surname as memsname, recs.firstname as recfname, recs.surname as recsname
from
	cd.members mems
	left join cd.members recs
	on recs.memid = mems.recommendedby
order by memsname, memfname;

--15. Produce a list of all members who have recommended another member
select distinct recs.firstname as firstname, recs.surname as surname
from
	cd.members mems
	inner join cd.members recs
	on recs.memid = mems.recommendedby
order by surname,firstname;

--16. Produce a list of all members, along with their recommender, using no joins.
select distinct mems.firstname || ' ' ||  mems.surname as member,
	(select recs.firstname || ' ' || recs.surname as recommender
		from cd.members recs
		where recs.memid = mems.recommendedby
	)
	from
		cd.members mems
order by member;

--17. Count the number of recommendations each member makes.
select recommendedby, count(*)
	from cd.members
	where recommendedby is not null
	group by recommendedby
order by recommendedby;

--18. List the total slots booked per facility
select facid, sum(slots)
from cd.bookings
group by facid
order by facid;

--19.List the total slots booked per facility in a given month
select facid, sum(slots) as "Total Slots"
from cd.bookings
where
	starttime >= '2012-09-01'
	and starttime < '2012-10-01'
GROUP BY facid
order by sum(slots);

--20. List the total slots booked per facility per month
select facid, extract(month from starttime) as month, sum(slots) as "Total Slots"
from cd.bookings
where extract(year from starttime) = 2012
group by facid, month
order by facid, month;

--21. Find the count of members who have made at least one booking
select count(distinct memid) from cd.bookings;

--22. List each member's first booking after September 1st 2012
select mems.surname, mems.firstname, mems.memid, min(bks.starttime) as starttime
from cd.members mems
inner join cd.bookings bks on
mems.memid = bks.memid
where starttime >= '2012-09-01'
group by mems.surname, mems.firstname, mems.memid
order by mems.memid;

--23. Produce a list of member names, with each row containing the total member count
select count(*) over(), firstname, surname
	from cd.members
order by joindate;

--24. Produce a numbered list of members
select row_number() over(order by joindate), firstname, surname
	from cd.members
order by joindate;

--25. Output the facility id that has the highest number of slots booked, again
select facid, total from (
	select facid, sum(slots) total, rank() over (order by sum(slots) desc) rank
	from cd.bookings
	group by facid
) as ranked
where rank = 1;

--26. Format the names of members
Select surname || ', ' || firstname as name from cd.members;

--27. Find telephone numbers with parentheses
select memid, telephone from cd.members where telephone similar to '%[()]%';


--28. Count the number of members whose surname starts with each letter of the alphabet
select substr (mems.surname,1,1) as letter, count(*) as count
    from cd.members mems
    group by letter
    order by letter;









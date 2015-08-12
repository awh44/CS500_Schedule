package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.*;
import play.mvc.*;
import play.libs.Json;
import play.db.DB;
import play.twirl.api.Html;

import views.html.*;
import models.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class Application extends Controller
{

	public static Result index()
	{
		return ok(index.render(""));
	}

	private static SortedMap<String, String> getSubjectsInternal(Connection conn) throws SQLException
	{
		SortedMap<String, String> map = new TreeMap<String, String>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(
			"SELECT DISTINCT abbr, S.name AS subject_name " + 
			"FROM Courses_Have CH, Subjects S " + 
			"WHERE S.name = CH.subject " + 
			"ORDER BY CH.abbr");
		while (rs.next())
		{
			map.put(rs.getString("abbr"), rs.getString("subject_name"));
		}

		rs.close();
		statement.close();
		return map;
	}

	public static Result getSubjects()
	{
		Connection conn = DB.getConnection();
		SortedMap<String, String> map;
		try
		{
			map = getSubjectsInternal(conn);
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("{}");
		}

		return ok(Json.toJson(map));
	}

	public static String getSubjectOptions()
	{
		String result = "";
		Connection conn = DB.getConnection();
		try
		{
			SortedMap<String, String> map = getSubjectsInternal(conn);
			for (String abbr : map.keySet())
			{
				result += "<option value=\"" + abbr + "\">" + map.get(abbr) + "</option>";
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public static Html getSubjectOptionsAsHtml()
	{
		return Html.apply(getSubjectOptions());
	}

	private static List<Courses_Have> getCoursesForSubjectInternal(String abbr) throws SQLException
	{
		List<Courses_Have> courses = new ArrayList<Courses_Have>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT abbr, num, name, description, credits, subject " +
			"FROM Courses_Have " +
			"WHERE abbr = ? " + 
			"ORDER BY num");
		statement.setString(1, abbr);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			Courses_Have course = new Courses_Have
			(
				rs.getString("abbr"),
				rs.getString("num"),
				rs.getString("name"),
				rs.getString("description"),
				rs.getDouble("credits"),
				rs.getString("subject")
			);

			courses.add(course);
		}

		rs.close();
		statement.close();
		conn.close();
		return courses;
	}

	public static Result getCoursesForSubject(String abbr)
	{
		List<Courses_Have> courses;
		try
		{
			courses = getCoursesForSubjectInternal(abbr);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("[]");
		}

		return ok(Json.toJson(courses));
	}

	public static String getCoursesForSubjectOptions(String abbr)
	{
		String result = "";
		try
		{
			List<Courses_Have> courses = getCoursesForSubjectInternal(abbr);
			for (int i = 0; i < courses.size(); i++)
			{
				Courses_Have course = courses.get(i);
				result += "<option value=\""  + course.getAbbr() + " " + course.getNum() + "\">";
				result += course.getNum() + " " + course.getName();
				result += "</option>";
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static Result getCoursesForSubjectOptionsRoute(String abbr)
	{
		String result = getCoursesForSubjectOptions(abbr);
		if (result == null)
			return internalServerError("");

		return ok(result);
	}

	private static List<Instructor> getInstructorsInternal() throws SQLException
	{
		List<Instructor> instructors = new ArrayList<Instructor>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT name " +
			"FROM Instructors " +
			"ORDER BY name");
		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			instructors.add(new Instructor(rs.getString("name")));
		}

		rs.close();
		statement.close();
		conn.close();
		return instructors;
	}

	public static Result getInstructors()
	{
		try
		{
			return ok(Json.toJson(getInstructorsInternal()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("[]");
		}
	}

	public static String getInstructorsOptions() throws SQLException
	{
		String result = "";
		List<Instructor> instructors = getInstructorsInternal();
		for (int i = 0; i < instructors.size(); i++)
		{
			String instructor = instructors.get(i).getName();
			result += "<option value=\"" + instructor + "\">" + instructor + "</option>";
		}

		return result;
	}

	public static Html getInstructorsOptionsAsHtml()
	{
		try
		{
			return Html.apply(getInstructorsOptions());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return Html.apply("");
		}
	}

	private static List<Campus> getCampusesInternal() throws SQLException
	{
		List<Campus> campuses = new ArrayList<Campus>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT name " +
			"FROM Campuses " +
			"ORDER BY name");
		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			campuses.add(new Campus(rs.getString("name")));
		}

		rs.close();
		statement.close();
		conn.close();
		return campuses;
	}

	public static Result getCampuses()
	{
		try
		{
			return ok(Json.toJson(getCampusesInternal()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("[]");
		}
	}

	public static String getCampusesOptions() throws SQLException
	{
		String result = "";
		List<Campus> campuses = getCampusesInternal();
		for (int i = 0; i < campuses.size(); i++)
		{
			String campus = campuses.get(i).getName();
			result += "<option value=\"" + campus + "\">" + campus + "</option>";
		}

		return result;
	}

	public static Html getCampusesOptionsAsHtml()
	{
		try
		{
			return Html.apply(getCampusesOptions());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return Html.apply("");
		}
	}
	
	public static Html getNumberOptions()
	{
		String result = "";
		for (int i = 0; i < 1000; i++)
		{
			String formatted = String.format("%03d", i);
			result += "<option value=\"" + formatted + "\">" + formatted + "</option>";
		}

		return Html.apply(result);
	}

	public static Html getTermOptions()
	{
		String result = "";
		Connection conn = DB.getConnection();
		try
		{
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT season, term_type, year FROM Terms");
			while (rs.next())
			{
				String term = new Term(rs.getString("season"), rs.getString("term_type"), rs.getInt("year")).toString();
				result += "<option value=\"" + term + "\">" + term + "</option>";
			}
			rs.close();
			statement.close();
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return Html.apply(result);
	}

	private static List<Section> getSectionsForCourseInTermInternal(String subject, String num, String season, String term_type, int year) throws SQLException
	{
		List<Section> sections = new ArrayList<Section>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT CRN, subject, num, season, term_type, year, section_id, capacity, enrolled, instructor, campus " + 
			"FROM Sections " + 
			"WHERE subject = ? AND num = ? AND season = ? AND term_type = ? AND year = ? " + 
			"ORDER BY CRN");
		statement.setString(1, subject);
		statement.setString(2, num);
		statement.setString(3, season);
		statement.setString(4, term_type);
		statement.setInt(5, year);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			Section section = new Section
			(
				rs.getInt("CRN"),
				rs.getString("subject"),
				rs.getString("num"),
				rs.getString("season"),
				rs.getString("term_type"),
				rs.getInt("year"),
				rs.getString("section_id"),
				rs.getInt("capacity"),
				rs.getInt("enrolled"),
				rs.getString("instructor"),
				rs.getString("campus"),
				null
			);

			sections.add(section);
		}

		rs.close();
		statement.close();
		conn.close();

		return sections;
	}

	public static String getSectionsForCourseInTermTable(String subject, String num, String season, String term_type, int year)
	{
		return "";
	}

	private static Map<Course_Offered_In_Term, List<Section>> getSectionsForCourseInternal(String subject, String num) throws SQLException
	{
		Map<Course_Offered_In_Term, List<Section>> sections = new HashMap<Course_Offered_In_Term, List<Section>>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT " +
				"S.CRN AS SCRN, " +
				"COIT.subject AS COITsubject, " +
				"COIT.num AS COITnum, " +
				"COIT.season AS COITseason, " +
				"COIT.term_type AS COITterm_type, " +
				"COIT.year AS COITyear, " + 
				"S.section_id AS Ssection_id, " +
				"S.capacity AS Scapacity, " +
				"S.enrolled AS Senrolled, " +
				"S.instructor AS Sinstructor, " +
				"S.campus AS Scampus " +
			"FROM " +
				"Course_Offered_In_Term COIT LEFT OUTER JOIN Sections S ON " + 
					"COIT.subject = S.subject AND COIT.num = S.num AND COIT.season = S.season AND " +
					"COIT.term_type = S.term_type AND COIT.year = S.year " +
			"WHERE COIT.subject = ? AND COIT.num = ? " + 
			"ORDER BY COIT.year, " +
			"CASE " + 
			"WHEN COIT.season = 'Winter' THEN 1 " +
			"WHEN COIT.season = 'Spring' THEN 2 " +
			"WHEN COIT.season = 'Summer' THEN 3 " + 
			"WHEN COIT.season = 'Fall' THEN 4 ELSE 5 END");
		statement.setString(1, subject);
		statement.setString(2, num);

		ResultSet rs = statement.executeQuery();
		String last_season = null, last_type = null;
		Integer last_year = null;
		Course_Offered_In_Term coit = null;
		while (rs.next())
		{
			String season = rs.getString("COITseason");
			String term_type = rs.getString("COITterm_type");
			int year = rs.getInt("COITyear");

			if (!season.equals(last_season) || !term_type.equals(last_type) || year != last_year)
			{
				coit = new Course_Offered_In_Term(subject, num, season, term_type, year);
				sections.put(coit, new ArrayList<Section>());
			}

			Integer CRN = rs.getInt("SCRN");
			if (!rs.wasNull())
			{
				List<Meets_At> meets_at = getMeetsAtInternal(CRN, subject, num, season, term_type, year);
				Section section = new Section
				(
					CRN,
					subject,
					num,
					season,
					term_type,
					year,
					rs.getString("Ssection_id"),
					rs.getInt("Scapacity"),
					rs.getInt("Senrolled"),
					rs.getString("Sinstructor"),
					rs.getString("Scampus"),
					meets_at
				);
			
				sections.get(coit).add(section);
			}

			last_season = season;
			last_type = term_type;
			last_year = year;
		}

		rs.close();
		statement.close();
		conn.close();

		return sections;
	}

	public static Result getSectionsForCourse(String subject, String num)
	{
		try
		{
			return ok(Json.toJson(getSectionsForCourseInternal(subject, num)));
		}
		catch (SQLException e)
		{
			return internalServerError("{}");
		}
	}

	public static String getSectionsForCourseTables(String subject, String num) throws SQLException
	{
		String result = "";
		Map<Course_Offered_In_Term, List<Section>> coit_sections = getSectionsForCourseInternal(subject, num);
		for (Course_Offered_In_Term coit : coit_sections.keySet())
		{
			String quarter = coit.getSeason() + " " + coit.getType() + " " + coit.getYear();
			result += "<div class=\"quarter-table\">";
			result += "<h3>" + quarter + "</h3>";
			result +=
				"<div class=\"grid\">" +
					"<div class=\"row\">" +
						 "<h4 class=\"col-md-2\">CRN</h4>" +
						 "<h4 class=\"col-md-2\">Section</h4>" +
						 "<h4 class=\"col-md-1\">Capacity</h4>" +
						 "<h4 class=\"col-md-1\">Enrolled</h4>" +
						 "<h4 class=\"col-md-2\">Instructor</h4>" +
						 "<h4 class=\"col-md-2\">Campus</h4>" +
						 "<h4 class=\"col-md-2\">Meets At</h4>" +
					"</div>";

			List<Section> sections = coit_sections.get(coit);
			if (sections.size() > 0)
			{
				for (int i = 0; i < sections.size(); i++)
				{
					Section section = sections.get(i);
					result +=
						"<div class=\"row\">" +
							"<span class=\"col-md-2\">" + section.getCRN() + "</span>" +
							"<span class=\"col-md-2\">" + section.getSectionId() + "</span>" +
							"<span class=\"col-md-1\">" + section.getCapacity() + "</span>" +
							"<span class=\"col-md-1\">" + section.getEnrolled() + "</span>" +
							"<span class=\"col-md-2\">" + section.getInstructor() + "</span>" +
							"<span class=\"col-md-2\">" + section.getCampus() + "</span>" +
							"<span class=\"col-md-2\">";

					List<Meets_At> meets = section.getMeetsAt();
					if (meets.size() > 0)
					{
						Meets_At m = meets.get(0);
						result += m.getDay() + " " + m.getStartTime() + "-" + m.getEndTime();
						for (int j = 1; j < meets.size(); j++)
						{
							m = meets.get(j);
							result += "<br>" + m.getDay() + " " + m.getStartTime() + "-" + m.getEndTime();
						}
						result += "</span>";
					}
					else
					{
						result += "None</span>";
					}

					result += "</div>";
				}

			}
			else
			{
				result += "<div class=\"row\">" + "<span class=\"col-md-12\">No section information.</option></div>";
			}

			result += "</div></div>";
		}

		return result;
	}

	public static Result getSectionsForCourseTablesRoute(String subject, String num)
	{
		try
		{
			return ok(getSectionsForCourseTables(subject, num));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("");
		}
	}

	private static List<Meets_At> getMeetsAtInternal(int CRN, String subject, String num, String season, String term_type, int year) throws SQLException
	{
		List<Meets_At> meets_at = new ArrayList<Meets_At>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT day, start_time, end_time " +
			"FROM Meets_At " +
			"WHERE CRN = ? AND subject = ? AND num = ? AND season = ? AND term_type = ? AND year = ? " +
			"ORDER BY " +
			"CASE " + 
			"WHEN day = 'M' THEN 1 " +
			"WHEN day = 'T' THEN 2 " +
			"WHEN day = 'W' THEN 3 " +
			"WHEN day = 'R' THEN 4 " +
			"WHEN day = 'F' THEN 5 " + 
			"WHEN day = 'S' THEN 6 " +
			"ELSE 7 END, start_time");

		statement.setInt(1, CRN);
		statement.setString(2, subject);
		statement.setString(3, num);
		statement.setString(4, season);
		statement.setString(5, term_type);
		statement.setInt(6, year);
		
		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			Meets_At meets = new Meets_At
			(
				CRN,
				subject,
				num,
				season,
				term_type,
				year,
				rs.getString("day"),
				rs.getTime("start_time"),
				rs.getTime("end_time")
			);

			meets_at.add(meets);
		}

		rs.close();
		statement.close();
		conn.close();

		return meets_at;
	}

	private static SortedMap<String, Integer> getExtremaPerSeasonInternal(String subject, String num, boolean isMostRecent) throws SQLException
	{
		SortedMap<String, Integer> extrema = new TreeMap<String, Integer>();
		Connection conn = DB.getConnection();
		String st = "SELECT season, " + (isMostRecent ? "MAX" : "MIN") + "(year) AS extremum " +
			"FROM Course_Offered_In_Term " +
			"WHERE subject = ? AND num = ? " +
			"GROUP BY season " +
			"ORDER BY " +
			"CASE " +
			"WHEN season = 'Fall' THEN 1 " +
			"WHEN season = 'Winter' THEN 2 " +
			"WHEN season = 'Spring' THEN 3 " +
			"WHEN season = 'Summer' THEN 4 " +
			"ELSE 5 END";
		PreparedStatement statement = conn.prepareStatement(st);
		statement.setString(1, subject);
		statement.setString(2, num);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			extrema.put(rs.getString("season"), rs.getInt("extremum"));
		}

		rs.close();
		statement.close();
		conn.close();

		return extrema;
	}

	private static SortedMap<String, Integer> getMostRecentPerSeasonInternal(String subject, String num) throws SQLException
	{
		return getExtremaPerSeasonInternal(subject, num, true);
	}

	public static String getExtremaPerSeasonTable(String subject, String num, boolean isMostRecent) throws SQLException
	{
		SortedMap<String, Integer> extrema = getExtremaPerSeasonInternal(subject, num, isMostRecent);
		String result = "";
		for (String season : extrema.keySet())
		{
			result += "<span class=\"season-label\">" + season + ": </span>";
			result += "<span class=\"season-extrema\">" + extrema.get(season) + "</span>";
			result += "<br>";
		}

		return result;
	}

	public static Result getMostRecentPerSeasonTableRoute(String subject, String num)
	{
		try
		{
			return ok(getExtremaPerSeasonTable(subject, num, true));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("");
		}
	}

	public static SortedMap<String, Integer> getEarliestPerSeasonInternal(String subject, String num) throws SQLException
	{
		return getExtremaPerSeasonInternal(subject, num, false);
	}

	public static Result getEarliestPerSeasonTableRoute(String subject, String num)
	{
		try
		{
			return ok(getExtremaPerSeasonTable(subject, num, false));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("");
		}
	}

	public static List<Courses_Have> getCoursesForInstructorInternal(String name) throws SQLException
	{
		List<Courses_Have> courses = new ArrayList<Courses_Have>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT DISTINCT " +
				"CH.abbr AS CHabbr, " +
				"CH.num AS CHnum, " +
				"CH.name AS CHname, " +
				"CH.description AS CHdesc, " +
				"CH.credits AS CHcredits, " +
				"CH.subject AS CHsubject " +
			"FROM Courses_Have CH, Sections S " +
			"WHERE CH.abbr = S.subject AND CH.num = S.num AND S.instructor = ? " +
			"ORDER BY CH.abbr, CH.num");
		statement.setString(1, name);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			courses.add(new Courses_Have
			(
				rs.getString("CHabbr"),
				rs.getString("CHnum"),
				rs.getString("CHname"),
				rs.getString("CHdesc"),
				rs.getDouble("CHcredits"),
				rs.getString("CHsubject")
			));
		}

		return courses;
	}

	public static Result getCoursesForInstructor(String name)
	{
		try
		{
			return ok(Json.toJson(getCoursesForInstructorInternal(name)));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("[]");
		}
	}

	public static String getCoursesForInstructorTable(String name) throws SQLException
	{
		String result =
			"<div class=\"grid\">" +
				"<div class=\"row\">" +
					"<div class=\"col-md-3\"><h4>Subject</h4></div>" +
					"<div class=\"col-md-1\"><h4>Abbreviation</h4></div>" +
					"<div class=\"col-md-1\"><h4>Course Number</h4></div>" +
					"<div class=\"col-md-2\"><h4>Course Name</h4></div>" +
					"<div class=\"col-md-4\"><h4>Description</h4></div>" +
					"<div class=\"col-md-1\"><h4>Credits</h4></div>" +
				"</div>";
		
		List<Courses_Have> courses = getCoursesForInstructorInternal(name);
		for (int i = 0; i < courses.size(); i++)
		{
			Courses_Have course = courses.get(i);
			result += 
				"<div class=\"row\">" +
					"<span class=\"col-md-3\">" + course.getSubject() + "</span>" +
					"<span class=\"col-md-1\">" + course.getAbbr() + "</span>" +
					"<span class=\"col-md-1\">" + course.getNum() + "</span>" +
					"<span class=\"col-md-2\">" + course.getName() + "</span>" +
					"<span class=\"col-md-4\">" + course.getDescription() + "</span>" +
					"<span class=\"col-md-1\">" + course.getCredits() + "</span>" +
				"</div>";
		}

		result += "</div>";

		return result;
	}

	public static Result getCoursesForInstructorTableRoute(String name)
	{
		try
		{
			return ok(getCoursesForInstructorTable(name));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("");
		}
	}
	public static List<Instructor> getInstructorsForCourseInternal(String subject, String number) throws SQLException
	{
		List<Instructor> instructors = new ArrayList<Instructor>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT DISTINCT " +
				"I.name AS Iname " +
			"FROM Instructors I " +
				"JOIN Sections S " + 
					"ON S.instructor = I.name " +
				"JOIN Course_Offered_In_Term COIT " +
					"ON COIT.subject = S.subject AND COIT.num = S.num " +
			"WHERE COIT.subject = ? AND COIT.num = ?");
			
		statement.setString(1, subject);
		statement.setString(2, number);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			instructors.add(new Instructor
			(
				rs.getString("Iname")
			));
		}

		rs.close();
		statement.close();
		conn.close();

		return instructors;
	}

	public static List<Instructor> getInstructorsForCourseByName(String course_name) throws SQLException
	{
		List<Instructor> instructors = new ArrayList<Instructor>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT DISTINCT " +
				"I.name AS Iname " +
			"FROM Instructors I, Sections S, Courses_Have CH " +
			"WHERE " +
				"I.name = S.instructor AND CH.abbr = S.subject AND CH.num = S.num AND CH.name = ? ");
		statement.setString(1, course_name);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			instructors.add(new Instructor
			(
				rs.getString("Iname")
			));
		}
		
		rs.close();
		statement.close();
		conn.close();

		return instructors;
	}

	public static Result getInstructorsForCourse(String subject, String number)
	{
		try
		{
			return ok(Json.toJson(getInstructorsForCourseInternal(subject, number)));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("[]");
		}
	}

	public static String buildInstructorsForCourseTable(List<Instructor> instructors) throws SQLException
	{
		String result =
			"<div class=\"grid\">" +
				"<div class=\"row\">" +
					"<div class=\"col-md-3\"><h4>Instructors</h4></div>" +
				"</div>";
		
		for (int i = 0; i < instructors.size(); i++)
		{
			Instructor instructor = instructors.get(i);
			result += 
				"<div class=\"row\">" +
					"<span class=\"col-md-3\">" + instructor.getName() + "</span>" +
				"</div>";
		}

		result += "</div>";

		return result;
	}

	public static String getInstructorsForCourseTable(String subject, String number) throws SQLException
	{
		return buildInstructorsForCourseTable(getInstructorsForCourseInternal(subject, number));
	}

	public static String getInstructorsForCourseByNameTable(String name) throws SQLException
	{
		return buildInstructorsForCourseTable(getInstructorsForCourseByName(name));
	}

	public static Result getInstructorsForCourseTableRoute(String subject, String number)
	{
		try
		{
			return ok(getInstructorsForCourseTable(subject, number));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("");
		}
	}

	public static Result getInstructorsForCourseByNameTableRoute(String name)
	{
		try
		{
			return ok(getInstructorsForCourseByNameTable(name));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return internalServerError("");
		}
	}
}

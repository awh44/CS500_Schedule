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

	private static List<Section> getSectionsForCourseInternal(String subject, String num) throws SQLException
	{
		List<Section> sections = new ArrayList<Section>();
		Connection conn = DB.getConnection();
		PreparedStatement statement = conn.prepareStatement(
			"SELECT CRN, subject, num, season, term_type, year, section_id, capacity, enrolled, instructor, campus " +
			"FROM Sections " + 
			"WHERE subject = ? AND num = ? " + 
			"ORDER BY year, " +
			"CASE " + 
			"WHEN season = 'Winter' THEN 1 " +
			"WHEN season = 'Spring' THEN 2 " +
			"WHEN season = 'Summer' THEN 3 " + 
			"WHEN season = 'Fall' THEN 4 ELSE 5 END");
		statement.setString(1, subject);
		statement.setString(2, num);

		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			int CRN = rs.getInt("CRN");
			String season = rs.getString("season");
			String term_type = rs.getString("term_type");
			int year = rs.getInt("year");
			List<Meets_At> meets_at = getMeetsAtInternal(CRN, subject, num, season, term_type, year);

			Section section = new Section
			(
				CRN,
				subject,
				num,
				season,
				term_type,
				year,
				rs.getString("section_id"),
				rs.getInt("capacity"),
				rs.getInt("enrolled"),
				rs.getString("instructor"),
				rs.getString("campus"),
				meets_at
			);
			
			sections.add(section);
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
			return internalServerError("[]");
		}
	}

	public static String getSectionsForCourseTables(String subject, String num) throws SQLException
	{
		String result = "";
		List<Section> sections = getSectionsForCourseInternal(subject, num);
		String prev_quarter = null;
		for (int i = 0; i < sections.size(); i++)
		{
			System.out.println("burg");
			Section section = sections.get(i);
			String quarter = section.getSeason() + " " + section.getType() + " " + section.getYear();
			if (!quarter.equals(prev_quarter))
			{
				if (prev_quarter != null)
				{
					result += "</tbody></table></div>";
				}
				result += "<div class=\"quarter-table\">";
				result += "<h3>" + quarter  + "</h3>";

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
			}

			prev_quarter = quarter;

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

		result += "</div></div>";
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
}

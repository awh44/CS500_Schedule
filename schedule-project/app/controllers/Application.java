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

	public static Result getSubjects()
	{
		Connection conn = DB.getConnection();
		SortedMap<String, String> map;
		try
		{
			map = getSubjectsMap(conn);
		}
		catch (SQLException e)
		{
			return internalServerError("{}");
		}

		return ok(Json.toJson(map));
	}

	private static SortedMap<String, String> getSubjectsMap(Connection conn) throws SQLException
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

	public static Html getSubjectOptions()
	{
		String result = "";
		Connection conn = DB.getConnection();
		try
		{
			SortedMap<String, String> map = getSubjectsMap(conn);
			for (String abbr : map.keySet())
			{
				result += "<option value=\"" + abbr + "\">" + map.get(abbr) + "</option>";
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return Html.apply(result);
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
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return Html.apply(result);
	}
}

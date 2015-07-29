package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.*;
import play.mvc.*;
import play.libs.Json;

import views.html.*;

import java.sql.Connection;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;

public class Application extends Controller
{

	public static Result index()
	{
		return ok(index.render("Your new application is ready."));
	}

	public static Result getSubjects()
	{
		List<String> list = new ArrayList<String>();
		list.add("hi");
		list.add("yo");
		return ok(Json.toJson(list));
	}

}

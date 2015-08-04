/*jshint sub:true*/

function quarters_subject_onchange()
{
	$("#quarters_num").empty();
	$("#quarters_results").empty();
	var abbr = $("#quarters_subject").val();
	$.ajax
	(
		{
			"type": "GET",
			"data": { "abbr": abbr },
			"url": "getCoursesForSubjectOptionsRoute",
			"success": function (data)
			{
				$("#quarters_num").append(data);
			}
		}
	);
}

function instr_subject_onchange()
{
	$("#instr_num").empty();
	$("#instr_results").empty();
	var abbr = $("#instr_subject").val();
	$.ajax
	(
		{
			"type": "GET",
			"data": { "abbr": abbr },
			"url": "getCoursesForSubjectOptionsRoute",
			"success": function (data)
			{
				$("#instr_num").append(data);
			}
		}
	);
}

function course_in_quarter_query(url)
{
	$("#quarters_results").empty();
	var num = $("#quarters_num").val().split(" ");
	$.ajax
	(
		{
			"type": "GET",
			"data": { "subject": num[0], "num": num[1] },
			"url": url,
			"success": function (data)
			{
				$("#quarters_results").append(data);
			},
			"error": function (jqXHR, textStatus, data)
			{
				alert(data);
			}
		}
	);
}

function find_all_sections_click()
{
	course_in_quarter_query("getSectionsForCourseTablesRoute");
}

function find_most_recent_per_season_click()
{
	course_in_quarter_query("getMostRecentPerSeasonTableRoute");
}

function find_earliest_per_season_click()
{
	course_in_quarter_query("getEarliestPerSeasonTableRoute");
}

function find_courses_for_instructor_click()
{
	$("#instructors_results").empty();
	var name = $("#instructors_instructor").val();
	$.ajax
	(
		{
			"type": "GET",
			"data": { "name": name },
			"url": "getCoursesForInstructorTableRoute",
			"success": function (data)
			{
				$("#instructors_results").append(data);
			},
			"error": function (jqXHR, textStatus, data)
			{
				alert(data);
			}
		}
	);
}

function find_instructors_for_course_click()
{
	$("#instr_results").empty();
	var name = $("#instr_subject").val();
	$.ajax
	(
		{
			"type": "GET",
			"data": { "name": name },
			"url": "getInstructorsForCourseTableRoute",
			"success": function (data)
			{
				$("#instr_results").append(data);
			},
			"error": function(jqXHR, textStatus, data)
			{
				alert(data);
			}
		}
	);
}

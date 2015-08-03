/*jshint sub:true*/

function quarters_subject_onchange()
{
	$("#quarters_num").empty();
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

function find_all_sections_click()
{
	$("#quarters_results").empty();
	var num = $("#quarters_num").val().split(" ");
	$.ajax
	(
		{
			"type": "GET",
			"data": { "subject": num[0], "num": num[1] },
			"url": "getSectionsForCourseTablesRoute",
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

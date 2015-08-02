package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Course_Offered_In_Term")
public class Course_Offered_In_Term extends Model
{
	private String subject_;
	private String num_;
	private String season_;
	private String term_type_;
	private int year_;

	public Course_Offered_In_Term(String subject, String num, String season, String term_type, int year)
	{
		setSubject(subject);
		setNum(num);
		setSeason(season);
		setType(term_type);
		setYear(year);
	}

	public void setSubject(String subject)
	{
		subject_ = subject;
	}

	public String getSubject()
	{
		return subject_;
	}

	public void setNum(String num)
	{
		num_ = num;
	}

	public String getNum()
	{
		return num_;
	}

	public void setSeason(String season)
	{
		season_ = season;
	}

	public String getSeason()
	{
		return season_;
	}

	public void setType(String term_type)
	{
		term_type_ = term_type;
	}

	public String getType()
	{
		return term_type_;
	}

	public void setYear(int year)
	{
		year_ = year;
	}
	
	public int getYear()
	{
		return year_;
	}

}

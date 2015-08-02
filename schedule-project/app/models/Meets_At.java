package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Time;

@Entity
@Table(name = "Meets_At")
public class Meets_At extends Model
{
	private int CRN_;
	private String subject_;
	private String num_;
	private String season_;
	private String term_type_;
	private int year_;
	private String day_;
	private Time start_time_;
	private Time end_time_;

	public Meets_At(int CRN, String subject, String num, String season, String term_type, int year, String day, Time start_time, Time end_time)
	{
		setCRN(CRN);
		setSubject(subject);
		setNum(num);
		setSeason(season);
		setType(term_type);
		setYear(year);
		setDay(day);
		setStartTime(start_time);
		setEndTime(end_time);
	}

	public void setCRN(int CRN)
	{
		CRN_ = CRN;
	}

	public int getCRN()
	{
		return CRN_;
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

	public void setDay(String day)
	{
		day_ = day;
	}

	public String getDay()
	{
		return day_;
	}

	public void setStartTime(Time start_time)
	{
		start_time_ = start_time;
	}

	public Time getStartTime()
	{
		return start_time_;
	}

	public void setEndTime(Time end_time)
	{
		end_time_ = end_time;
	}

	public Time getEndTime()
	{
		return end_time_;
	}
}

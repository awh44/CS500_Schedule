package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Subjects")
public class Term extends Model
{
	private String season_;
	private String term_type_;
	private int year_;

	public Term(String season, String term_type, int year)
	{
		setSeason(season);
		setType(term_type);
		setYear(year);
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

	public String toString()
	{
		return season_ + " " + term_type_ + " " + year_;
	}
}

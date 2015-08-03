package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Courses_Have")
public class Courses_Have extends Model
{
	private String abbr_;
	private String num_;
	private String name_;
	private String description_;
	private double credits_;
	private String subject_;

	public Courses_Have(String abbr, String num, String name, String description, double credits, String subject)
	{
		setAbbr(abbr);
		setNum(num);
		setName(name);
		setDescription(description);
		setCredits(credits);
		setSubject(subject);
	}

	public void setAbbr(String abbr)
	{
		abbr_ = abbr;
	}

	public String getAbbr()
	{
		return abbr_;
	}

	public void setNum(String num)
	{
		num_ = num;
	}

	public String getNum()
	{
		return num_;
	}

	public void setName(String name)
	{
		name_ = name;
	}

	public String getName()
	{
		return name_;
	}

	public void setDescription(String description)
	{
		description_ = description;
	}

	public String getDescription()
	{
		return description_;
	}

	public void setCredits(double credits)
	{
		credits_ = credits;
	}

	public double getCredits()
	{
		return credits_;
	}

	public void setSubject(String subject)
	{
		subject_ = subject;
	}

	public String getSubject()
	{
		return subject_;
	}
}

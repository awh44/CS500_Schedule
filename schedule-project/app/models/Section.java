package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Sections")
public class Section extends Model
{
	private int CRN_;
	private String subject_;
	private String num_;
	private String season_;
	private String term_type_;
	private int year_;
	private String section_id_;
	private int capacity_;
	private int enrolled_;
	private String instructor_;
	private String campus_;

	public Section(int CRN, String subject, String num, String season, String term_type, int year, String section_id, int capacity, int enrolled, String instructor, String campus)
	{
		setCRN(CRN);
		setSubject(subject);
		setNum(num);
		setSeason(season);
		setType(term_type);
		setYear(year);
		setSectionId(section_id);
		setCapacity(capacity);
		setEnrolled(enrolled);
		setInstructor(instructor);
		setCampus(campus);
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

	public void setSectionId(String section_id)
	{
		section_id_ = section_id;
	}

	public String getSectionId()
	{
		return section_id_;
	}

	public void setCapacity(int capacity)
	{
		capacity_ = capacity;
	}

	public int getCapacity()
	{
		return capacity_;
	}

	public void setEnrolled(int enrolled)
	{
		enrolled_ = enrolled;
	}

	public int getEnrolled()
	{
		return enrolled_;
	}

	public void setInstructor(String instructor)
	{
		instructor_ = instructor;
	}

	public String getInstructor()
	{
		return instructor_;
	}

	public void setCampus(String campus)
	{
		campus_ = campus;
	}

	public String getCampus()
	{
		return campus_;
	}

}

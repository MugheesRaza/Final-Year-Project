package com.example.mughees.chat_app.Model;

public class fame {
    String name;
    String job;
    String jobdescription;
    String gpa;
    String year;
    String city;
    String image;

    public fame(String name, String job, String jobdescription, String gpa, String year, String city, String image) {
        this.name = name;
        this.job = job;
        this.jobdescription = jobdescription;
        this.gpa = gpa;
        this.year = year;
        this.city = city;
        this.image = image;
    }

    public fame() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobdescription() {
        return jobdescription;
    }

    public void setJobdescription(String jobdescription) {
        this.jobdescription = jobdescription;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

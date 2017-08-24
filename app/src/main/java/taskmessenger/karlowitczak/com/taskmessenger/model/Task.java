package taskmessenger.karlowitczak.com.taskmessenger.model;

/**
 * Created by JakubWitczak on 01.05.2017.
 */
public class Task {
    public String title;
    public String description;
    public String status;
    public String assignedPerson;

    public Task(){

    }

    public Task(String title, String description, String status, String assignedPerson){
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedPerson = assignedPerson;
    }
}

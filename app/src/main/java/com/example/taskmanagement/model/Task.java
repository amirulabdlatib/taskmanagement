package com.example.taskmanagement.model;

public class Task {
    private int taskID;
    private String taskName;
    private String description;
    private String assignedDate;
    private String taskDue;
    private String staffName;
    private String status;


    public Task(){}

    public Task(int taskID, String taskName, String description, String assignedDate, String taskDue, String staffName, String status) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
        this.assignedDate = assignedDate;
        this.taskDue = taskDue;
        this.staffName = staffName;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getTaskDue() {
        return taskDue;
    }

    public void setTaskDue(String taskDue) {
        this.taskDue = taskDue;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", assignedDate='" + assignedDate + '\'' +
                ", taskDue='" + taskDue + '\'' +
                ", staffName='" + staffName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}


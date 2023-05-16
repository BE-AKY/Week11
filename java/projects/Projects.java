package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectsService;

public class Projects {
	private Scanner scanner = new Scanner(System.in);
	private ProjectsService projectsService = new ProjectsService();
	private Project curProject;

	// @formatter:off
	private List<String> operations = List.of(
			"1) Create project",
			"2) List available projects",
			"3) Select an available project",
			"4) Update a project's details",
			"5) Delete a project"
		);
	// @formatter:on

	
	public static void main(String[] args) {
		new Projects().processUserSelections();
	}

//Menu operations and user selection	
	private void processUserSelections() {
		boolean done = false;

		while (!done) {
		try {
			int selection = getUserSelection();
			
			switch (selection) {
				case -1:
					done = exitMenu();
					break;
					
				case 1:
					createProject();
					break;
					
				case 2:
					listProjects();
					break;
					
				case 3:
					setCurrentProject();
					break;
					
				case 4:
					updateProjectDetails();
					break;
				
				case 5:
					deleteProject();
					break;
					
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
		} catch (Exception e) {
			System.out.println("\nError: " + e.toString() + " Try again.");

			}
		}
	}
	
//OPTION 5
private void deleteProject() {
	listProjects();
	
	Integer projectId = getIntInput("Enter the ID of the project to delete: ");
	
	if(Objects.nonNull(projectId)) {
	projectsService.deleteProject(projectId);
		System.out.println("Project " + projectId + " was deleted successfully.");
	
	if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
		curProject = null;
	}
}
}


//OPTION 4
private void updateProjectDetails() {
	if(Objects.isNull(curProject)) {
		System.out.println("\nPlease select a project first.");
		return;
	}
	
	String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");

	BigDecimal estimatedHours = 
			getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
	
	BigDecimal actualHours = 
			getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");

	Integer difficulty = 
			getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
	
	String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
	
//sets inputed info into updated project	
Project project = new Project();
	
project.setProjectId(curProject.getProjectId());

	project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);

	project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);

	project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
	
	project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
	
	project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
	

projectsService.modifyProjectDetails(project);
	
	curProject = projectsService.fetchProjectById(curProject.getProjectId());
	
}

//OPTION 3
private void setCurrentProject() {
//	List<Project> projects = listProjects();
//	
//	Integer projectId = getIntInput("Select a project ID");
//	
//	curProject = null;
//	
//	for(Project project : projects) {
//		if(project.getProjectId().equals(projectId)) {
//			curProject = projectsService.fetchProjectById(projectId);
//			break;
//		}
	listProjects();
	Integer projectId = getIntInput("Select a project ID to view: ");
	
	curProject = null;
	
	curProject = projectsService.fetchProjectById(projectId);
	}


//OPTION 2
private void listProjects() {
	List<Project> projects = projectsService.fetchAllProjects();
	
	curProject = null;
	
	System.out.println("\nAvailable Projects");
	
	projects.forEach(project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
}


//OPTION 1 (Gathers user input)	
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectsService.addProject(project);
			System.out.println("You have successfully created project: " + dbProject);
	
		curProject = projectsService.fetchProjectById(dbProject.getProjectId());
	}
	
//Prints start menu
	private void printOperations() {
		System.out.println();
		System.out.println("Here's what you can do:");

		operations.forEach(op -> System.out.println("   " + op));


		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		
		} else {
			System.out.println("\nYou are working with project: " + curProject);
	}
}
	
	
//Big Decimal
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
			
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	
//To Exit Menu	
	private boolean exitMenu() {
		System.out.println("\nExiting the menu. Goodbye.");
		return true;
	}

	
//Available Menu & User's Selection	
	private int getUserSelection() {
		printOperations();
		
		Integer op = getIntInput("\nEnter an operation number (press Enter to quit)");

		return Objects.isNull(op) ? -1 : op;
	}

//	private void printOperations() {
//		System.out.println();
//		System.out.println("Here's what you can do:");
//
//		operations.forEach(op -> System.out.println("   " + op));
//	}
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

//	private Double getDoubleInput(String prompt) {
//		String input = getStringInput(prompt);
//
//		if (Objects.isNull(input)) {
//			return null;
//		}
//
//		try {
//			return Double.parseDouble(input);
//		} catch (NumberFormatException e) {
//			throw new DbException(input + " is not a valid number.");
//		}
//	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String line = scanner.nextLine();

		return line.isBlank() ? null : line.trim();
	}	
}
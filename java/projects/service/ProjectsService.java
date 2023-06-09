package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.LinkedList;
//import java.util.List;

//import projects.Projects;
import projects.dao.ProjectsDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectsService {

	private ProjectsDao projectsDao = new ProjectsDao();

	public Project addProject(Project project) {
		return projectsDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
	// @formatter:off	
		return projectsDao.fetchAllProjects()
			.stream()
			.sorted((p1, p2) -> p1.getProjectId() - p2.getProjectId())
			.collect(Collectors.toList());
	// @formatter:on	
	}

	public Project fetchProjectById(Integer projectId) {
		return projectsDao.fetchProjectById(projectId)
				.orElseThrow(() -> new NoSuchElementException(
					"Project with ID=" + projectId + " does not exist."));
	}

	public void modifyProjectDetails(Project project) {
		if(!projectsDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID= " + project.getProjectId() + " does not exist.");
		}
	}

	public void deleteProject(Integer projectId) {
		if(!projectsDao.deleteProject(projectId)) {
			throw new DbException("Project with ID= " + projectId + " does not exist.");
		}
	}
	
//	private void loadFromFile(String fileName) {
//		String content = readFileContent(fileName);
//		List<String> sqlStatements = convertContentToSqlStatements(content);
//		
//		projectsDao.executeBatch(sqlStatements);
//	}
//
//	private List<String> convertContentToSqlStatements(String content) {
//		content = removeComments(content);
//		content = replaceWhiteSpaceSequencesWithSingleSpace(content);
//		
//		return extractLinesFromContent(content);
//	}
//
//	
//	private List<String> extractLinesFromContent(String content) {
//		List<String> lines = new LinkedList<>();
//	
//		while(!content.isEmpty()) {	
//			int semicolon = content.indexOf(";");
//			
//			if(semicolon == -1) {
//				if(!content.isBlank()) {
//					lines.add(content);
//				}
//				
//				content = "";
//			} else {
//				lines.add(content.substring(0, semicolon).trim());
//				content = content.substring(semicolon + 1);
//			}
//		}
//			return lines;
//	}
//
//	private String replaceWhiteSpaceSequencesWithSingleSpace(String content) {
//		return content.replaceAll("\\s+", " ");
//	}
//
//	private String removeComments(String content) {
//		StringBuilder builder = new StringBuilder(content);
//		int commentPos = 0;
//		
//		while ((commentPos = builder.indexOf("-- ", commentPos)) != -1) {
//			int eolPos = builder.indexOf("\n", commentPos + 1);
//			
//			if (eolPos == -1) {
//				builder.replace(commentPos, builder.length(), "");
//			} else {
//				builder.replace(commentPos, eolPos + 1, "");
//			}
//		}		
//		return builder.toString();
//	}
//	
//		
//	private String readFileContent(String fileName) {
//		try {
//			Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
//			return Files.readString(path);
//		} catch (Exception e) {
//			throw new DbException(e);
//		}
//	}

////		public static void main(String[] args) {
////			new ProjectsService().createAndPopulateProjects();
////	}
}

# Projet 3d touch map generator - 3DMapGen

## Installation and launch

- Using Eclipse :

To launch this project, clone or download into your workspace.
Choose File -> Import -> Existing project into workspace -> Browse ("Select root directory") -> Select 3DMapGen -> Finish

For the first launch, you can clean the project : Project -> Clean ...
Now you can execute the MainApplication.

- Using Netbeans : To be completed
	
## Anatomy of the application

The directory layout looks like this:

    ├ 3DMapGen   # The main floder of the application.
        ├ src/          # The application sources (codes). 
        ├ ressources/   # The application ressources.
        ├ .classpath    # Specifies the location of user-defined classes and packages.
        ├ .project      # The project description file that describes the project.
        ├ .gitignore    # File specifies intentionally untracked files that Git should ignore. 
        ├ readme.md     # The information about this project.

### src directory
This folder contains all source code of this application. It organized with some packages :
	
    ├ application    # Entry point classe for launching application. 
    ├ model          # Entities or treatment classes.
    ├ properties     # The properties files.
    ├ config         # Contains constant static variables to configure application.

### ressources directory
	
    ├ fxml        # Contains all application views.
    ├ image       # Contains all images used into application.
    ├ stylesheet  # Contains all stylesheet ressources.
    ├ other       # Contains some image file for the development.

---------------
## Team members

#### Manager: 
	- Jean-Marie Favreau

#### Developer: 
	- Alexis Dardinier
	- Mathieu Vincent
	- Pierre Petit
	- Thomas Klein
	- Timothe Rouze
	- Pierre Charles

#### End User: 
	- Guillaume Touya

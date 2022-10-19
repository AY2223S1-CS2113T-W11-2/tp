package computercomponentchooser;

import computercomponentchooser.exceptions.UnknownCommandException;
import computercomponentchooser.exceptions.DuplicateBuildException;
import computercomponentchooser.exceptions.UnlistedBuildException;

import static computercomponentchooser.ComputerComponentChooser.storage;

public class Parser {

    static final int COMMAND_PARAMETER = 0;
    static final int NAME_PARAMETER = 1;

    private final BuildManager buildManager;

    public Parser(BuildManager buildManager) {
        this.buildManager = buildManager;
    }


    private static String getParameter(String line, int mode) {
        String[] lineSplit = line.split(" ", 2);
        return lineSplit[mode];
    }

    static boolean checkBye(String line) {
        String checkLine = line.toLowerCase();
        return checkLine.equals("bye");
    }

    static boolean checkEdit(String line) {
        String edit = getParameter(line, COMMAND_PARAMETER).toLowerCase();
        return edit.equals("edit");
    }

    public void parse(String line) {
        String command = getParameter(line, COMMAND_PARAMETER);
        Build newBuild;
        String name;
        try {
            switch (command) {
            case "bye":
            case "edit":
                break;
            case "list":
                Ui.printLine();
                System.out.println("Your current builds:");
                System.out.print(buildManager.toString());
                Ui.printLine();
                break;
            case "add":
                name = getParameter(line, NAME_PARAMETER);
                newBuild = new Build(name);
                buildManager.addBuild(newBuild);
                Ui.printLine();
                System.out.println("You have added " + name);
                Ui.printLine();
                try {
                    storage.saveBuild(buildManager);
                } catch (Exception e) {
                    System.out.println("Error saving builds");
                }
                break;
            case "view":
                name = getParameter(line, NAME_PARAMETER);
                Ui.printLine();
                System.out.print(buildManager.getBuild(name).toString());
                Ui.printLine();
                break;
            case "delete":
                name = getParameter(line, NAME_PARAMETER);
                newBuild = new Build(name);
                try {
                    storage.deleteBuild(name, buildManager);
                } catch (Exception e) {
                    System.out.println("Error saving builds");
                }
                buildManager.deleteBuild(name, newBuild);
                Ui.printLine();
                System.out.println("You have removed " + name);
                Ui.printLine();
                break;
            case "back":
                Ui.printLine();
                System.out.println("Back to main mode.");
                Ui.printLine();
                break;
            default:
                throw new UnknownCommandException();
            }
        } catch (UnknownCommandException e) {
            System.out.println(e.getMessage());
            Ui.printLine();
        } catch (DuplicateBuildException e) {
            System.out.println(e.getMessage());
            Ui.printLine();
        } catch (UnlistedBuildException e) {
            System.out.println(e.getMessage());
            Ui.printLine();
        }
    }
}

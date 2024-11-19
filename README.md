Project: Compute Roots Using Newton Iteration
Objectives
Exposure to using double variables, while loops, and static methods
Exposure to using several Eclipse features including creating a project by copying an existing project, renaming a Java file using refactoring, deleting files, creating a file by copying an existing file, using the Java editor, and exporting a project
The Problem
Your first job is to create a Java program that repeatedly asks the user whether they wish to calculate a square root. Each time, if the response is "y", then the program should proceed; if it is anything else, then the program should quit. Whenever it proceeds, the program should prompt the user for a number (a positive double, and your program may simply assume the input is consistent with this requirement) and then report the square root of that number to within a relative error of no more than 0.01%. The computation must be done using Newton iteration.

The intuitive idea of Newton iteration for computing square roots is fairly straightforward. Suppose you have a guess r for x1/2 that is too large; the argument is similar if it is too small. If r is too large to be the square root of x, then x/r must be too small, so the average of r and x/r should be a better guess than either r or x/r. This suggests that if you repeatedly replace your current guess r by (r + x/r)/2, then your sequence of guesses should converge to x1/2. And indeed it can be proved that it does. A good initial guess for x1/2 is simply r = x. If you continue updating r until |r2 – x |/x < ε2, then the relative error of the guess r will be less than ε.

After your initial program works, there are a number of other requirements to change it slightly, one step at a time, as explained below.

Method
Note: this is the last project description that will include instructions about basic Eclipse operations you have practiced already; see Environment Setup and Getting to Know Eclipse and Java. As always, we recommend that you start early and ask questions in class if you run into problems..

Create a new Eclipse project by copying ProjectTemplate (if needed, see Creating a New Project from a Project Template for details). Name the new project Newton.
Open the src folder of this project and then open (default package). As a starting point you should use ProgramWithIOAndStaticMethod.java. Rename it Newton1 and delete the other files from the project (if needed, see Creating a Program from a Skeleton (also Renaming a Java Program) for details).
Edit Newton1.java to satisfy the problem requirements stated above, including updating comments appropriately. Estimating the square root should be done in a static method declared as follows:
/**
 * Computes estimate of square root of x to within relative error 0.01%.
 *
 * @param x
 *            positive number to compute square root of
 * @return estimate of square root
 */
private static double sqrt(double x) {
    ...
}
Copy Newton1.java to create Newton2.java. Change sqrt (including its Javadoc comments) so it also works when x = 0. Note: if your code from Newton1 appears to work without any changes, but it is such that it might execute a division by 0, then it is not correct. Division by 0, in general, is undefined and you should not write code that attempts to compute it.
Copy Newton2.java to create Newton3.java. Change it so the main program prompts the user to input the value of ε (rather than assuming it is 0.0001), just once as the program begins, and so this value is also passed to sqrt.
Copy Newton3.java to create Newton4.java. Change it so the main program does not ask the user whether they wish to calculate a square root, but instead simply asks for a new value of x each time and interprets a negative value as an indication that it's time to quit.
Select your Eclipse project Newton (not just some of the files, but the whole project), create a zip archive of it, and submit the zip archive to the Carmen dropbox for this project, as described in Submitting a Project.
Additional Activities
Here are some possible additional activities related to this project. Any extra work is strictly optional, for your own benefit, and will not directly affect your grade.

Prove/argue rigorously that when the loop in sqrt terminates, the required precision for the result has been obtained. Note that this is not obvious, because the loop condition should be about a bound on the relative error between the square of the result and x, not about a bound on the relative error between the result and x1/2.
Copy Newton4.java to create Newton5.java. Change it so, in addition to asking for x, it asks the user for a root k (an integer greater than or equal to 2), and reports the k-th root of x as computed by Newton iteration.
Copy Newton5.java to create Newton6.java. Change it so that it checks that the inputs provided by the user are valid, that is, the input for epsilon is a positive real number and the input for x is a real number. Note that you cannot assume the user will provide a number; the user can type pretty much anything. So your method should read the input as a String (use SimpleReader.nextLine()), then make sure that the input is a real number (use FormatChecker.canParseDouble()), and finally convert the string to a double (use Double.parseDouble()).

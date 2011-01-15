/*
Copyright 2011 Karl-Michael Schneider

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.jwatter.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.jwatter.util.ReservoirSampler;

public class RunTestSuite
        extends TestSuite {

    public static final int STATUS_OK = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_COMMANDLINE_ERROR = 2;
    public static final int STATUS_TEST_ERROR = 254;
    public static final int STATUS_TEST_FAILURE = 255;

    protected static final String NAME = "Unnamed test suite";

    protected static final Logger logger = Logger.getLogger(RunTestSuite.class.getName());

    protected int failureCount;
    protected int errorCount;

    /**
     * Creates an empty test suite with the specified name.
     * 
     * @param name
     *        the name of the test suite
     */
    public RunTestSuite () {
        super();
    }

    /**
     * Adds default tests to this test suite. Subclasses can override this method.
     */
    public void addDefaultTests ()
            throws RunTestSuiteException {}

    /**
     * Adds all tests in the specified test class to this test suite.
     * 
     * @param testClass
     *        the test class
     * @throws IllegalArgumentException
     *         if a test class is null
     */
    public void addTests (Class<? extends TestCase> testClass)
            throws IllegalArgumentException {
        if (testClass == null) {
            throw new IllegalArgumentException("test class must not be null");
        }
        if ((testClass.getModifiers() & Modifier.ABSTRACT) > 0) {
            throw new IllegalArgumentException("test class must not be abstract");
        }
        this.addTests(testClass, this.getTestNamesForClass(testClass));
    }

    /**
     * Adds all tests in the specified test classes to this test suite.
     * 
     * @param testClasses
     *        the test classes
     * @throws IllegalArgumentException
     *         if a test class is null
     */
    public void addTests (Class<? extends TestCase>[] testClasses)
            throws IllegalArgumentException {
        for (Class<? extends TestCase> testClass : testClasses) {
            this.addTests(testClass);
        }
    }

    /**
     * Adds the specified test to a given test suite.
     * 
     * @param suite
     *        the test suite to add the test to
     * @param testClass
     *        the class containing the test
     * @param testName
     *        the name of the test method
     * @throws IllegalArgumentException
     *         if the test name is null
     */
    protected void addTest (TestSuite suite, Class<? extends TestCase> testClass, String testName)
            throws IllegalArgumentException {
        if (testName == null) {
            throw new IllegalArgumentException("test name must not be null");
        }
        suite.addTest(TestSuite.createTest(testClass, testName));
    }

    /**
     * Adds the specified tests to this test suite.
     * 
     * @param testClass
     *        the class containing the tests to run
     * @param testNames
     *        the tests to run
     * @throws IllegalArgumentException
     *         if the test class or any test name is null
     */
    public void addTests (Class<? extends TestCase> testClass, String[] testNames)
            throws IllegalArgumentException {
        if (testClass == null) {
            throw new IllegalArgumentException("test class must not be null");
        }
        if ((testClass.getModifiers() & Modifier.ABSTRACT) > 0) {
            throw new IllegalArgumentException("test class must not be abstract");
        }
        TestSuite suite = new TestSuite(testClass.getName());
        for (String testName : testNames) {
            this.addTest(suite, testClass, testName);
        }
        this.addTest(suite);
    }

    protected String[] getTestNamesForClass (Class<? extends TestCase> testClass) {
        List<String> testNames = new ArrayList<String>();
        for (Method method : testClass.getMethods()) {
            if (isTestMethod(method)) {
                testNames.add(method.getName());
            }
        }
        return testNames.toArray(new String[0]);
    }

    protected boolean isTestMethod (Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0
            && method.getReturnType().equals(Void.TYPE) && method.getName().startsWith("test");
    }

    protected boolean listTests;
    protected Integer randomTests;
    protected String testClassName = null;
    protected String[] testMethodNames = null;

    /**
     * Parses the arguments as command line arguments and creates a test suite with the specified
     * tests.
     * 
     * @param args
     *        the command line arguments
     * @throws RunTestSuiteException
     *         if an invalid command line argument is encountered or a test cannot be found
     */
    public RunTestSuite (String[] args)
            throws RunTestSuiteException {
        this();
        this.addName();
        this.parseCommandLine(args);
        this.verifyCommandLine();
        this.addTests();
    }

    /**
     * Sets the name of this test suite. Subclasses can override this method to set their own name.
     */
    protected void addName () {
        this.setName(RunTestSuite.NAME);
    }

    /**
     * Adds the tests specified on the command line to this test suite.
     * 
     * @throws RunTestSuiteException
     *         if an error occurs while adding tests
     */
    protected void addTests ()
            throws RunTestSuiteException {
        Class<? extends TestCase> testClass = null;
        if (this.testClassName != null) {
            try {
                testClass = this.getTestClass(this.testClassName);
            }
            catch (ClassNotFoundException e) {
                throw new RunTestSuiteException("Class not found: " + this.testClassName,
                                                STATUS_ERROR);
            }
        }

        if (testClass == null) {
            this.addDefaultTests();
        }

        else if (this.testMethodNames == null) {
            try {
                this.addTests(testClass);
            }
            catch (IllegalArgumentException e) {
                throw new RunTestSuiteException(e.getMessage(), STATUS_ERROR);
            }
        }

        else {
            for (String methodName : this.testMethodNames) {
                try {
                    testClass.getMethod(methodName);
                }
                catch (NoSuchMethodException e) {
                    throw new RunTestSuiteException("Class " + this.testClassName
                        + " has no method " + methodName, STATUS_ERROR);
                }
            }
            try {
                this.addTests(testClass, this.testMethodNames);
            }
            catch (IllegalArgumentException e) {
                throw new RunTestSuiteException(e.getMessage(), STATUS_ERROR);
            }
        }
    }

    /**
     * Runs the tests in this test suite and logs the results, or lists the results.
     */
    public void run () {

        if (this.listTests) {
            this.listTests();
        }
        else if (this.randomTests != null) {
            this.runRandomTest();
        }
        else {
            this.runTests();
        }
    }

    /**
     * Runs the tests in this test suite and logs the results.
     */
    public void runTests () {
        logger.info("Running " + this.countTestCases() + " tests in \"" + this.getName() + "\"");

        TestResult result = new TestResult();
        this.run(result);
        this.failureCount = result.failureCount();
        this.errorCount = result.errorCount();

        logger.info("Tests run: " + result.runCount() + ", Errors: " + result.errorCount()
            + ", Failures: " + result.failureCount());
        this.logErrors(result.errors(), Level.SEVERE);
        this.logErrors(result.failures(), Level.WARNING);
    }

    public void runRandomTest () {
        ReservoirSampler<TestCase> sampler = new ReservoirSampler<TestCase>(this.randomTests);
        this.sampleTests(this, sampler);
        if (sampler.getCurrentSampleSize() == 0) {
            logger.info("No tests to run");
            return;
        }

        TestSuite tests = new TestSuite();
        for (TestCase test : sampler.getSample()) {
            tests.addTest(test);
        }
        TestResult result = new TestResult();
        tests.run(result);

        this.failureCount = result.failureCount();
        this.errorCount = result.errorCount();

        logger.info("Tests run: " + result.runCount() + ", Errors: " + result.errorCount()
            + ", Failures: " + result.failureCount());
        this.logErrors(result.errors(), Level.SEVERE);
        this.logErrors(result.failures(), Level.WARNING);
    }

    public void sampleTests (Test test, ReservoirSampler<TestCase> sampler) {
        if (test instanceof TestSuite) {
            for (Enumeration<Test> tests = ((TestSuite)test).tests(); tests.hasMoreElements();) {
                sampleTests(tests.nextElement(), sampler);
            }
        }
        else if (test instanceof TestCase) {
            sampler.read((TestCase)test);
        }
        else {
            System.out.println("Not a test: " + test.getClass().getName());
        }
    }

    protected void logErrors (Enumeration<TestFailure> errors, Level loglevel) {
        while (errors.hasMoreElements()) {
            TestFailure error = errors.nextElement();
            logger.log(loglevel, error.failedTest().getClass().getName() + ": "
                + ((TestCase)error.failedTest()).getName() + ": "
                + error.thrownException().getClass().getName() + ": " + error.exceptionMessage());
            System.err.print(error.trace());
        }

    }

    /**
     * Returns the number of failures after running the tests in this test suite.
     * 
     * @return the number of failures
     */
    public int getFailureCount () {
        return failureCount;
    }

    /**
     * Returns the number of errors after running the tests in this test suite.
     * 
     * @return the number of errors
     */
    public int getErrorCount () {
        return errorCount;
    }

    /**
     * Prints a list of the tests contained in this test suite to standard output.
     */
    public void listTests () {
        this.listTests(this, 0);
    }

    protected void listTests (Test test, int level) {
        if (test instanceof TestSuite) {
            System.out.println("Suite[" + level + "]: " + ((TestSuite)test).getName() + " ("
                + ((TestSuite)test).countTestCases() + " tests)");
            for (Enumeration<Test> tests = ((TestSuite)test).tests(); tests.hasMoreElements();) {
                listTests(tests.nextElement(), level + 1);
            }
        }
        else if (test instanceof TestCase) {
            System.out.println("Test: " + ((TestCase)test).getName());
        }
        else {
            System.out.println("Not a test: " + test.getClass().getName());
        }
    }

    protected void parseCommandLine (String[] args)
            throws RunTestSuiteException {
        this.listTests = false;
        this.randomTests = null;

        int p = 0;
        int handled;
        for (; p < args.length; p++) {
            if (args[p].equals("-l")) {
                this.listTests = true;
            }
            else if (args[p].equals("-r")) {
                if (++p < args.length) {
                    try {
                        this.randomTests = Integer.parseInt(args[p]);
                    }
                    catch (NumberFormatException e) {
                        throw new RunTestSuiteException("option -r requires an integer argument",
                                                        STATUS_COMMANDLINE_ERROR);
                    }
                }
                else {
                    throw new RunTestSuiteException("option -r requires an argument",
                                                    STATUS_COMMANDLINE_ERROR);
                }
                if (this.randomTests < 1) {
                    throw new RunTestSuiteException("must run at least one random test",
                                                    STATUS_COMMANDLINE_ERROR);
                }
            }
            else if ((handled = handleCommandLineOption(args, p)) > 0) {
                p += handled - 1;
            }
            else if (args[p].equals("-h") || args[p].equals("--help")) {
                throw new RunTestSuiteException(getUsageMessage(), STATUS_OK);
            }
            else if (args[p].startsWith("-")) {
                throw new RunTestSuiteException("unhandled option " + args[p],
                                                STATUS_COMMANDLINE_ERROR);
            }
            else {
                break;
            }
        }

        int m = 0;
        for (; p < args.length; p++) {
            if (args[p].startsWith("-")) {
                throw new RunTestSuiteException("options must precede commandline arguments",
                                                STATUS_COMMANDLINE_ERROR);
            }
            if (this.testClassName == null) {
                this.testClassName = args[p];
                if (p < args.length - 1) {
                    this.testMethodNames = new String[args.length - p - 1];
                }
            }
            else {
                if (!args[p].startsWith("test")) {
                    throw new RunTestSuiteException("Invalid test method name " + args[p]
                        + ", method name must start with 'test'", STATUS_COMMANDLINE_ERROR);
                }
                this.testMethodNames[m++] = args[p];
            }
        }
    }

    /**
     * Default command line option handler. Subclasses can override this method to define new
     * options.
     * 
     * @param args
     *        the command line arguments
     * @param argp
     *        the current position in the command line
     * @return the number of command line arguments handled by this call
     * @throws RunTestSuiteException
     *         if an option value is missing or an illegal option value is passed
     */
    protected int handleCommandLineOption (String[] args, int argp)
            throws RunTestSuiteException {
        return 0;
    }

    /**
     * Verify that all required command line options and arguments have been specified. Subclasses
     * can override this to do specific checks.
     * 
     * @throws RunTestSuiteException
     *         if a required option or command line argument is missing, or an illegal value has
     *         been passed.
     */
    protected void verifyCommandLine ()
            throws RunTestSuiteException {}

    @SuppressWarnings("unchecked")
    protected Class<? extends TestCase> getTestClass (String className)
            throws ClassNotFoundException {
        return (Class<? extends TestCase>)Class.forName(className);
    }

    protected String getUsageMessage () {
        return "Usage: " + RunTestSuite.class.getSimpleName()
            + " [Options] [testClass [testName]]\n" + "  -l        list tests, do not run\n"
            + "  -r NUM    run NUM random tests";
    }

    /**
     * Runs the tests specified in the arguments and exits. The status code upon exit is
     * <ul>
     * <li>{@link #STATUS_ERROR} if an error occurred during construction of the test suite
     * <li>{@link #STATUS_COMMANDLINE_ERROR} if an invalid command line argument is encountered
     * <li>{@link #STATUS_TEST_ERROR} if one or more tests had errors
     * <li>{@link #STATUS_TEST_FAILURE} if one or more tests fail
     * <li>{@link #STATUS_OK} if all tests pass
     * </ul>
     * 
     * @param args
     *        the command line arguments
     */
    public static void main (String[] args) {
        try {
            runTestSuite(new RunTestSuite(args));
        }
        catch (RunTestSuiteException e) {
            System.err.println(e.getMessage());
            System.exit(e.getStatus());
        }
    }

    /**
     * Runs the tests in the specified test suite and exits. The status code upon exit is
     * <ul>
     * <li>{@link #STATUS_TEST_ERROR} if one or more tests had errors
     * <li>{@link #STATUS_TEST_FAILURE} if one or more tests fail
     * <li>{@link #STATUS_OK} if all tests pass
     * </ul>
     * 
     * @param suite
     *        the test suite
     */
    protected static void runTestSuite (RunTestSuite suite) {
        suite.run();
        if (suite.getErrorCount() > 0) {
            System.exit(STATUS_TEST_ERROR);
        }
        else if (suite.getFailureCount() > 0) {
            System.exit(STATUS_TEST_FAILURE);
        }
        else {
            System.exit(STATUS_OK);
        }
    }

    /**
     * Exception thrown when an error occurs during construction of the test suite.
     */
    public class RunTestSuiteException
            extends Exception {

        private static final long serialVersionUID = 1L;

        protected int status;

        public RunTestSuiteException (String message, int status) {
            super(message);
            this.status = status;
        }

        public int getStatus () {
            return this.status;
        }
    }
}

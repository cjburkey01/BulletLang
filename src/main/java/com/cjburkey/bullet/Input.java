package com.cjburkey.bullet;

import picocli.CommandLine;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings({"WeakerAccess", "DefaultAnnotationParam"})
@CommandLine.Command(name = "bullet")
class Input implements Runnable {
    
    public static final boolean LENIENT = true;
    
    boolean valid = false;
    
    @CommandLine.Option(names = { "-h", "-help" }, usageHelp = true, description = "Display a list of parameters")
    boolean help = false;
    
    @CommandLine.Option(names = { "-d", "-debug" }, description = "Display compilation debug information")
    boolean debug = false;
    
    @CommandLine.Option(names = { "-e", "-errors" }, description = "Display full stacktraces of errors")
    boolean fullErrors;
    
    @CommandLine.Option(names = { "-v", "-version" }, description = "Display the version of the current BulletLang compiler")
    boolean printVersion;
    
    @CommandLine.Option(required = !LENIENT, names = { "-o", "--of", "--output" }, description = "The output *.c file", paramLabel = "OutputFile")
    String outputFile;
    
    @CommandLine.Option(required = !LENIENT, names = { "-i", "--if", "--input" }, description = "The input *.blt file", paramLabel = "InputFile")
    String inputFile;
    
    @CommandLine.Option(names = { "-s", "--src", "--sourceDir" }, description = "The base directory for required files in the script", paramLabel = "SourceDirectory")
    String sourceDirectory;
    
    private Input() {
    }
    
    public void run() {
        valid = true;
    }
    
    static Input run(String[] args) {
        Input input = new Input();
        CommandLine.run(input, args);
        return input;
    }
    
}

package com.cjburkey.bullet;

import picocli.CommandLine;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
class Input implements Runnable {
    
    public static final boolean LENIENT = true;
    
    boolean valid = false;
    
    @CommandLine.Option(names = { "-h", "-help" }, usageHelp = true, description = "Display a list of parameters")
    boolean help = false;
    
    @CommandLine.Option(names = { "-d", "-debug" }, description = "Display compilation debug information")
    boolean debug = false;
    
    @CommandLine.Option(names = { "-v", "-version" }, description = "Display the version of the current BulletLang compiler")
    boolean printVersion;
    
    @CommandLine.Option(required = !LENIENT, names = { "-o", "--of", "--output" }, description = "The output *.c file", paramLabel = "OutputFile")
    String outputFile;
    
    @CommandLine.Option(required = !LENIENT, names = { "-i", "--if", "--input" }, description = "The input *.blt file", paramLabel = "InputFile")
    String inputFile;
    
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
